package org.mule.dsl.builder.apikit;

import static org.mule.dsl.builder.core.Core.custom;
import static org.mule.dsl.builder.core.Core.flow;
import static org.mule.dsl.builder.core.Core.endpoint;
import static org.mule.dsl.builder.core.Properties.properties;
import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.construct.Flow;
import org.mule.dsl.builder.core.FlowBuilder;
import org.mule.module.apikit.Configuration;
import org.mule.module.apikit.Router;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


public class RestRouterBuilderImpl implements RestRouterBuilder
{


    private String ramlPath;

    private Map<String, Object> properties;
    private List<FlowBuilder> resourceActionBuilders = new ArrayList<FlowBuilder>();

    RestRouterBuilderImpl(String ramlPath)
    {
        this.ramlPath = ramlPath;

        this.properties = new HashMap<String, Object>();
    }

    public RestRouterBuilder using(Map<String, Object> properties)
    {
        this.properties = properties;
        return this;
    }

    public RestRouterBuilder when(FlowBuilder flowBuilder)
    {
        resourceActionBuilders.add(flowBuilder);
        return this;
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

    public Flow build(MuleContext muleContext) throws ConfigurationException, IllegalStateException
    {

        for (FlowBuilder resourceActionBuilder : resourceActionBuilders)
        {
            resourceActionBuilder.build(muleContext);
        }
        String address = getAddress();
        final Configuration config = new Configuration();
        if (properties != null)
        {
            applyProperties(config);
        }
        config.setRaml(ramlPath);
        final FlowBuilder restRouter = flow("RestRouterFlow")
                .from(endpoint(address))
                .chain(custom(Router.class).using(properties("config", config)));
        return restRouter.build(muleContext);
    }

    private void applyProperties(Configuration config)
    {
        try
        {
            BeanUtils.populate(config, properties);
        }
        catch (IllegalAccessException e)
        {

        }
        catch (InvocationTargetException e)
        {

        }
    }

    private String getAddress()
    {
        final String host = getProperty("host", "localhost");
        final String port = getProperty("port", "8081");
        final String path = getProperty("path", "api");
        return "http://" + host + ":" + port + "/" + path;
    }


}
