package org.mule.dsl.builder;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


public class SimpleMessageProcessorBuilder<T extends MessageProcessor> implements MessageProcessorBuilder<T>
{

    private Class<? extends MessageProcessor> messageProcessorClazz;
    private Map<String, Object> properties;

    public SimpleMessageProcessorBuilder(Class<? extends MessageProcessor> messageProcessorClazz)
    {
        this.messageProcessorClazz = messageProcessorClazz;
        this.properties = new HashMap<String, Object>();
    }

    public SimpleMessageProcessorBuilder(Class<? extends MessageProcessor> messageProcessorClazz, Map<String, Object> properties)
    {
        this.messageProcessorClazz = messageProcessorClazz;
        this.properties = properties;
    }

    public void using(Map<String, Object> properties)
    {
        this.properties = properties;
    }

    public T build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {

        MessageProcessor mp = null;
        try
        {
            mp = messageProcessorClazz.newInstance();
            Map<String, Object> mpProperties = properties;
            BeanUtils.populate(mp, mpProperties);
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return (T) mp;
    }
}
