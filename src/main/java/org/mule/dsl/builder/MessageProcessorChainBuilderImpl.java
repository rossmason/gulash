package org.mule.dsl.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.LoggerMessageProcessor;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.raml.model.ActionType;


public class MessageProcessorChainBuilderImpl<P> implements MessageProcessorChainBuilder<P>
{

    private P parent;
    private final ActionType actionType;
    private final String resourceName;
    private List<MessageProcessorBuilder<?>> messageProcessorBuilders = new ArrayList<MessageProcessorBuilder<?>>();


    public MessageProcessorChainBuilderImpl(P parent, ActionType actionType, String resourceName)
    {
        this.parent = parent;
        this.actionType = actionType;
        this.resourceName = resourceName;
    }


    public MessageProcessorChainBuilder<P> chain(Class<? extends MessageProcessor> clazz)
    {
        messageProcessorBuilders.add(new SimpleMessageProcessorBuilder(clazz));
        return this;
    }

    public MessageProcessorChainBuilder<P> chain(MessageProcessorBuilder builder)
    {
        messageProcessorBuilders.add(builder);
        return this;
    }

    public MessageProcessorChainBuilder<P> chainLogger()
    {
        this.messageProcessorBuilders.add(new SimpleMessageProcessorBuilder(LoggerMessageProcessor.class));
        return this;
    }

    public MessageProcessorChainBuilder<P> using(Map<String, Object> properties)
    {
        this.messageProcessorBuilders.get(this.messageProcessorBuilders.size() - 1).using(properties);
        return this;
    }

    public P end()
    {
        return parent;
    }

    public Flow build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {

        final List<MessageProcessor> messageProcessorList = new ArrayList<MessageProcessor>();

        for (MessageProcessorBuilder<?> messageProcessorBuilder : messageProcessorBuilders)
        {
            messageProcessorList.add(messageProcessorBuilder.build(muleContext));
        }

        final Flow flow = new Flow(actionType.name().toLowerCase() + ":" + resourceName, muleContext);
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