package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.registry.RegistrationException;

import org.apache.commons.lang.StringUtils;

/**
 * Created by machaval on 2/17/14.
 */
public class CustomGlobalElementBuilder<T> implements GlobalElementBuilder<T>
{

    private Class<T> globalElementClazz;
    private String name;

    public CustomGlobalElementBuilder(Class<T> globalElementClazz)
    {
        this.globalElementClazz = globalElementClazz;
    }

    @Override
    public GlobalElementBuilder<T> as(String name)
    {
        this.name = name;
        return this;
    }

    @Override
    public T create(MuleContext muleContext)
    {
        try
        {
            T newInstance = globalElementClazz.newInstance();
            if (!StringUtils.isEmpty(name))
            {
                muleContext.getRegistry().registerObject(name, newInstance);
            }
            else
            {
                muleContext.getRegistry().registerObject(globalElementClazz.getName(), newInstance);
            }

            return newInstance;
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (RegistrationException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
