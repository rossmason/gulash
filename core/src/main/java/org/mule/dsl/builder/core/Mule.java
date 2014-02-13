package org.mule.dsl.builder.core;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.client.MuleClient;
import org.mule.api.processor.MessageProcessor;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.config.builders.SimpleConfigurationBuilder;
import org.mule.construct.Flow;
import org.mule.context.DefaultMuleContextFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Mule
{

    private List<MuleBuilder<?>> builders = new ArrayList<MuleBuilder<?>>();
    private MuleContext muleContext;
    private List<StartListener> startListeners = new ArrayList<StartListener>();

    public Mule declare(MuleBuilder<? extends MessageProcessor> builder)
    {
        builders.add(builder);
        return this;
    }


    public Mule start() throws MuleException
    {

        muleContext = new DefaultMuleContextFactory().createMuleContext();
        for (MuleBuilder<?> builder : builders)
        {
            builder.build(muleContext);
        }
        muleContext.start();
        for (StartListener startListener : startListeners)
        {
            startListener.onStart(this);
        }
        return this;
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
}
