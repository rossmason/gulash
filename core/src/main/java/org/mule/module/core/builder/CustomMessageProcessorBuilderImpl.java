package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.processor.MessageProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


public class CustomMessageProcessorBuilderImpl<T extends MessageProcessor> implements CustomMessageProcessorBuilder<T>
{

    private Class<? extends MessageProcessor> messageProcessorClazz;
    private Map<String, Object> properties;

    public CustomMessageProcessorBuilderImpl(Class<? extends MessageProcessor> messageProcessorClazz)
    {
        this.messageProcessorClazz = messageProcessorClazz;
        this.properties = new HashMap<String, Object>();
    }

    public CustomMessageProcessorBuilderImpl(Class<? extends MessageProcessor> messageProcessorClazz, Map<String, Object> properties)
    {
        this.messageProcessorClazz = messageProcessorClazz;
        this.properties = properties;
    }

    public CustomMessageProcessorBuilder<T> using(Map<String, Object> properties)
    {
        this.properties = properties;
        return this;
    }

    public T create(MuleContext muleContext)
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
