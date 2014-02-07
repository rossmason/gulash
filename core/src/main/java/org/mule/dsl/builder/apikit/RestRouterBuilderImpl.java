package org.mule.dsl.builder.apikit;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.endpoint.EndpointException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.dsl.builder.core.FlowBuilder;
import org.mule.dsl.builder.core.FlowBuilderImpl;
import org.mule.endpoint.EndpointURIEndpointBuilder;
import org.mule.module.apikit.Configuration;
import org.mule.module.apikit.Router;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.raml.model.ActionType;


public class RestRouterBuilderImpl<P> implements RestRouterBuilder<P>
{


    private String ramlPath;
    private P parent;
    private Map<String, Object> properties;
    private List<FlowBuilder<?>> resourceActionBuilders = new ArrayList<FlowBuilder<?>>();

    RestRouterBuilderImpl(String ramlPath, P parent)
    {
        this.ramlPath = ramlPath;
        this.parent = parent;
        this.properties = new HashMap<String, Object>();
    }

    public RestRouterBuilder<P> using(Map<String, Object> properties)
    {
        this.properties = properties;
        return this;
    }

    public FlowBuilder<RestRouterBuilder<P>> on(ActionType action, String resource)
    {
        FlowBuilderImpl<RestRouterBuilder<P>> restRouterBuilderResourceActionBuilder = new FlowBuilderImpl<RestRouterBuilder<P>>(this, action, resource);
        resourceActionBuilders.add(restRouterBuilderResourceActionBuilder);
        return restRouterBuilderResourceActionBuilder;
    }

    public P end()
    {
        return parent;
    }

    public <T> T getProperty(String name, T defaultValue)
    {
        if (properties.containsKey(name))
        {
            return (T) properties.get(name);
        }
        else
        {
            return defaultValue;
        }
    }

    public Flow build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {

        final Flow routerFlow = new Flow("RestRouterFlow", muleContext);
        final String host = getProperty("host", "localhost");
        final String port = getProperty("port", "8081");
        final String path = getProperty("path", "api");
        final EndpointURIEndpointBuilder endpointURIEndpointBuilder = new EndpointURIEndpointBuilder("http://" + host + ":" + port + "/" + path, muleContext);
        final InboundEndpoint inboundEndpoint;
        try
        {
            inboundEndpoint = endpointURIEndpointBuilder.buildInboundEndpoint();
            muleContext.getRegistry().registerEndpoint(inboundEndpoint);
            routerFlow.setMessageSource(inboundEndpoint);
            final Router apikitRouter = configureApikitRouter(muleContext);
            routerFlow.setMessageProcessors(Arrays.<MessageProcessor>asList(apikitRouter));
            for (FlowBuilder<?> resourceActionBuilder : resourceActionBuilders)
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
