package org.mule.dsl.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.raml.model.ActionType;


public class ResourceActionBuilderImpl<P> implements ResourceActionBuilder<P>, MuleBuilder<Flow>
{

    private P parent;
    private final ActionType actionType;
    private final String resourceName;
    private List<Class<? extends MessageProcessor>> messageProcessors = new ArrayList<Class<? extends MessageProcessor>>();
    private List<Map<String, Object>> properties = new ArrayList<Map<String, Object>>();

    public ResourceActionBuilderImpl(P parent, ActionType actionType, String resourceName)
    {
        this.parent = parent;
        this.actionType = actionType;
        this.resourceName = resourceName;
    }


    public ResourceActionBuilder<P> addMessageProcessor(Class<? extends MessageProcessor> clazz)
    {
        messageProcessors.add(clazz);
        properties.add(new HashMap<String, Object>());
        return this;
    }

    public ResourceActionBuilder<P> using(Map<String, Object> properties)
    {

        this.properties.get(properties.size() - 1).putAll(properties);
        return this;
    }

    public P end()
    {
        return parent;
    }

    public Flow build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {

        List<MessageProcessor> messageProcessorList = new ArrayList<MessageProcessor>();
        for (int i = 0; i < messageProcessors.size(); i++)
        {

            try
            {
                Class<? extends MessageProcessor> messageProcessor = messageProcessors.get(i);
                MessageProcessor mp = messageProcessor.newInstance();
                Map<String, Object> mpProperties = properties.get(i);
                BeanUtils.populate(mp, mpProperties);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
        }
        Flow flow = new Flow(actionType.name().toLowerCase() + ":" + resourceName, muleContext);
        flow.setMessageProcessors(messageProcessorList);
        try
        {
            muleContext.getRegistry().registerFlowConstruct(flow);
        }
        catch (MuleException e)
        {
            e.printStackTrace();
        }
        return flow;
    }
}
