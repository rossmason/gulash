package org.mule.dsl.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.endpoint.EndpointException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.endpoint.EndpointURIEndpointBuilder;
import org.mule.module.apikit.Configuration;
import org.mule.module.apikit.Router;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.raml.model.ActionType;


public class RestRouterBuilderImpl<P> implements RestRouterBuilder<P>
{


    private String ramlPath;
    private P parent;
    private Map<String, Object> properties;
    private List<MessageProcessorChainBuilder<?>> resourceActionBuilders = new ArrayList<MessageProcessorChainBuilder<?>>();

    RestRouterBuilderImpl(String ramlPath, P parent)
    {
        this.ramlPath = ramlPath;
        this.parent = parent;
    }

    public RestRouterBuilder using(Map<String, Object> properties)
    {
        this.properties = properties;
        return this;
    }

    public MessageProcessorChainBuilder<RestRouterBuilder<P>> on(ActionType action, String resource)
    {
        MessageProcessorChainBuilderImpl<RestRouterBuilder<P>> restRouterBuilderResourceActionBuilder = new MessageProcessorChainBuilderImpl<RestRouterBuilder<P>>(this, action, resource);
        resourceActionBuilders.add(restRouterBuilderResourceActionBuilder);
        return restRouterBuilderResourceActionBuilder;
    }

    public P end()
    {
        return parent;
    }

    public Flow build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {

        final Flow routerFlow = new Flow("RestRouterFlow", muleContext);
        String host = "localhost";
        String port = "8081";
        String path = "api";
        final EndpointURIEndpointBuilder endpointURIEndpointBuilder = new EndpointURIEndpointBuilder("http://" + host + ":" + port + "/" + path, muleContext);
        final InboundEndpoint inboundEndpoint;
        try
        {
            inboundEndpoint = endpointURIEndpointBuilder.buildInboundEndpoint();
            muleContext.getRegistry().registerEndpoint(inboundEndpoint);
            routerFlow.setMessageSource(inboundEndpoint);
            final Router apikitRouter = configureApikitRouter(muleContext);
            routerFlow.setMessageProcessors(Arrays.<MessageProcessor>asList(apikitRouter));
            for (MessageProcessorChainBuilder<?> resourceActionBuilder : resourceActionBuilders)
            {
                resourceActionBuilder.build(muleContext);
            }

            muleContext.getRegistry().registerFlowConstruct(routerFlow);
        }
        catch (EndpointException e)
        {
            throw new ConfigurationException(e);

        }
        catch (InitialisationException e)
        {
            throw new ConfigurationException(e);
        }
        catch (MuleException e)
        {
            throw new ConfigurationException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new ConfigurationException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigurationException(e);
        }

        return routerFlow;
    }

    private Router configureApikitRouter(MuleContext muleContext) throws IllegalAccessException, InvocationTargetException
    {
        final Router apikitRouter = new Router();
        final Configuration config = new Configuration();
        config.setRaml(ramlPath);


        if (properties != null)
        {
            BeanUtils.populate(config, properties);
        }
        apikitRouter.setConfig(config);
        apikitRouter.setMuleContext(muleContext);
        return apikitRouter;
    }
}
