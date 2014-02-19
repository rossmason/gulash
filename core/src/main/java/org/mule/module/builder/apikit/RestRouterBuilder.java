package org.mule.module.builder.apikit;

import static org.mule.module.Core.endpoint;
import static org.mule.module.Core.flow;
import static org.mule.module.Core.invoke;
import static org.mule.module.builder.core.PropertiesBuilder.properties;
import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;
import org.mule.module.Core;
import org.mule.module.apikit.Configuration;
import org.mule.module.apikit.Router;
import org.mule.module.builder.core.MessageProcessorBuilder;
import org.mule.module.builder.core.PrivateFlowBuilder;

import java.util.ArrayList;
import java.util.List;

import org.raml.model.ActionType;


public class RestRouterBuilder  implements Builder<Flow>
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


    public RestRouterBuilder usingBaseUri(String baseUri)
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

    public RestRouterBuilder usingConsolePath(String consolePath){
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
                .then(invoke(Router.class).using(properties("config", config)));
        return restRouter.create(muleContext);
    }

    private String getAddress()
    {
        return baseUri;
    }


}
