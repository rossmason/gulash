package org.mule.module.core;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.client.MuleClient;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.RegistrationException;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.config.DefaultMuleConfiguration;
import org.mule.config.builders.DefaultsConfigurationBuilder;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;
import org.mule.context.DefaultMuleContextFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Mule
{

    private List<Builder<?>> builders = new ArrayList<Builder<?>>();
    private MuleContext muleContext;
    private List<StartListener> startListeners = new ArrayList<StartListener>();
    private File muleHome;

    public Mule(File muleHome)
    {
        this.muleHome = muleHome;
    }

    public Mule()
    {
        this(new File("."));
    }

    public Mule declare(Builder<?> builder)
    {
        builders.add(builder);
        return this;
    }

    public Mule start() throws MuleException
    {

        final Properties properties = new Properties();
        properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, muleHome.getAbsolutePath());
        muleContext = new DefaultMuleContextFactory().createMuleContext(new DefaultsConfigurationBuilder(), properties, new DefaultMuleConfiguration());

        for (Builder<?> builder : builders)
        {
            builder.create(muleContext);
        }
        muleContext.start();
        for (StartListener startListener : startListeners)
        {
            startListener.onStart(this);
        }
        return this;
    }

    public Mule down() throws MuleException
    {
        muleContext.stop();
        return this;
    }

    public File getMuleHome()
    {
        return muleHome;
    }

    public Mule onStart(StartListener listener)
    {
        startListeners.add(listener);
        return this;
    }

    public MuleClient client()
    {
        return new DefaultLocalMuleClient(muleContext);
    }

    public MuleEvent callFlow(String name, Object payload) throws MuleException
    {
        Object targetObject = muleContext.getRegistry().lookupObject(name);
        if (targetObject instanceof Flow)
        {
            Flow flow = (Flow) targetObject;
            DefaultMuleMessage defaultMuleMessage = new DefaultMuleMessage(payload, muleContext);
            DefaultMuleEvent defaultMuleEvent = new DefaultMuleEvent(defaultMuleMessage, MessageExchangePattern.REQUEST_RESPONSE, flow);
            return flow.process(defaultMuleEvent);
        }
        throw new DefaultMuleException("Target flow was not found " + name);
    }

    public <T> T lookup(Class<T> clazz) throws RegistrationException
    {
        return muleContext.getRegistry().lookupObject(clazz);
    }
}
