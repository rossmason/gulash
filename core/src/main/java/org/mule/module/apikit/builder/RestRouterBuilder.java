package org.mule.module.apikit.builder;

import static org.mule.module.Core.endpoint;
import static org.mule.module.Core.flow;
import static org.mule.module.Core.invoke;
import static org.mule.module.core.builder.PropertiesBuilder.properties;

import org.mule.api.MuleContext;
import org.mule.api.exception.MessagingExceptionHandler;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;
import org.mule.module.Core;
import org.mule.module.apikit.Configuration;
import org.mule.module.apikit.MappingExceptionListener;
import org.mule.module.apikit.RestMappingExceptionStrategy;
import org.mule.module.apikit.Router;
import org.mule.module.core.builder.MessageProcessorBuilder;
import org.mule.module.core.builder.PrivateFlowBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.raml.model.ActionType;


public class RestRouterBuilder implements Builder<Flow>
{


    private String ramlPath;
    private List<PrivateFlowBuilder> resourceActionBuilders = new ArrayList<PrivateFlowBuilder>();
    private String baseUri = "http://0.0.0.0:8081/api";
    private boolean consoleEnabled = true;
    private String consolePath = "console";

    public RestRouterBuilder(String ramlPath)
    {
        this.ramlPath = ramlPath;
    }


    public RestRouterBuilder baseUri(String baseUri)
    {
        this.baseUri = baseUri;
        return this;
    }

    public RestRouterBuilder enableConsole()
    {
        this.consoleEnabled = true;
        return this;
    }

    public RestRouterBuilder disableConsole()
    {
        this.consoleEnabled = false;
        return this;
    }

    public RestRouterBuilder consolePath(String consolePath)
    {
        this.consolePath = consolePath;
        return this;
    }

    public RestRouterBuilder on(String resource, ActionType action)
    {
        resourceActionBuilders.add(Core.flow(action.name().toLowerCase() + ":" + resource));
        return this;
    }


    public RestRouterBuilder then(MessageProcessorBuilder<?>... messageProcessorBuilder)
    {
        if (resourceActionBuilders.isEmpty())
        {
            throw new IllegalStateException("On method must be called before then.");
        }
        resourceActionBuilders.get(resourceActionBuilders.size() - 1).then(messageProcessorBuilder);
        return this;
    }

    public Flow create(MuleContext muleContext)
    {

        for (PrivateFlowBuilder resourceActionBuilder : resourceActionBuilders)
        {
            resourceActionBuilder.create(muleContext);
        }
        String address = getAddress();
        final Configuration config = new Configuration();
        config.setConsoleEnabled(consoleEnabled);
        config.setConsolePath(consolePath);

        config.setRaml(ramlPath);
        final PrivateFlowBuilder restRouter = flow("RestRouterFlow")
                .on(endpoint(address))
                .then(invoke(Router.class).using(properties("config", config)))
                .onException(getExceptionBuilder());
        return restRouter.create(muleContext);
    }

    private Builder<MessagingExceptionHandler> getExceptionBuilder()
    {
        return new Builder<MessagingExceptionHandler>()
        {
            @Override
            public MessagingExceptionHandler create(MuleContext muleContext)
            {
                RestMappingExceptionStrategy es = new RestMappingExceptionStrategy();
                es.setGlobalName("rest-router-es");
                List<MappingExceptionListener> exceptionListeners = new ArrayList<MappingExceptionListener>();
                exceptionListeners.add(createExceptionListener(404, "org.mule.module.apikit.exception.NotFoundException"));
                exceptionListeners.add(createExceptionListener(405, "org.mule.module.apikit.exception.MethodNotAllowedException"));
                exceptionListeners.add(createExceptionListener(415, "org.mule.module.apikit.exception.UnsupportedMediaTypeException"));
                exceptionListeners.add(createExceptionListener(406, "org.mule.module.apikit.exception.NotAcceptableException"));
                exceptionListeners.add(createExceptionListener(400, "org.mule.module.apikit.exception.BadRequestException"));
                es.setExceptionListeners(exceptionListeners);
                return es;
            }

            private MappingExceptionListener createExceptionListener(int status, String... exception)
            {
                MappingExceptionListener mappingExceptionListener = new MappingExceptionListener();
                mappingExceptionListener.setStatusCode(status);
                mappingExceptionListener.setExceptions(Arrays.asList(exception));
                return mappingExceptionListener;
            }
        };
    }

    private String getAddress()
    {
        return baseUri;
    }


}
