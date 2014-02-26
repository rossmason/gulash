package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.registry.RegistrationException;
import org.mule.module.core.BuilderConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;


public class JavaBeanElementBuilder<T> implements GlobalElementBuilder<T>
{

    private Class<T> globalElementClazz;
    private String name;
    private Map<String, Object> properties;

    public JavaBeanElementBuilder(Class<T> globalElementClazz)
    {
        this.globalElementClazz = globalElementClazz;
    }

    @Override
    public JavaBeanElementBuilder<T> as(String name)
    {
        this.name = name;
        return this;
    }

    public JavaBeanElementBuilder using(Map<String, Object> properties)
    {
        this.properties = properties;
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

            if (properties != null)
            {
                BeanUtils.populate(newInstance, properties);
            }

            return newInstance;
        }
        catch (InstantiationException e)
        {
            throw new BuilderConfigurationException("Exception while building bean " + globalElementClazz, e);
        }
        catch (IllegalAccessException e)
        {
            throw new BuilderConfigurationException("Exception while building bean " + globalElementClazz, e);
        }
        catch (RegistrationException e)
        {
            throw new BuilderConfigurationException("Exception while building bean " + globalElementClazz, e);
        }
        catch (InvocationTargetException e)
        {
            throw new BuilderConfigurationException("Exception while building bean " + globalElementClazz, e);
        }
    }
}
