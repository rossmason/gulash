package org.mule.dsl.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.LoggerMessageProcessor;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.dsl.builder.apikit.MessageProcessorBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.raml.model.ActionType;


public class FlowBuilderImpl<P> implements FlowBuilder<P>
{

    private P parent;
    private final ActionType actionType;
    private final String resourceName;
    private List<MessageProcessorBuilder<?>> messageProcessorBuilders = new ArrayList<MessageProcessorBuilder<?>>();


    public FlowBuilderImpl(P parent, ActionType actionType, String resourceName)
    {
        this.parent = parent;
        this.actionType = actionType;
        this.resourceName = resourceName;
    }


    public FlowBuilder<P> chain(Class<? extends MessageProcessor> clazz)
    {
        messageProcessorBuilders.add(new SimpleMessageProcessorBuilder(clazz));
        return this;
    }

    public FlowBuilder<P> chain(MessageProcessorBuilder builder)
    {
        messageProcessorBuilders.add(builder);
        return this;
    }

    public FlowBuilder<P> chainLogger()
    {
        this.messageProcessorBuilders.add(new SimpleMessageProcessorBuilder(LoggerMessageProcessor.class));
        return this;
    }

    public FlowBuilder<P> using(Map<String, Object> properties)
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
