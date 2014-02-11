package org.mule.dsl.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.source.MessageSource;
import org.mule.construct.Flow;
import org.mule.dsl.builder.apikit.MessageProcessorBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FlowBuilderImpl implements FlowBuilder
{


    private MessageSourceBuilder<?> messageSourceBuilder;
    private List<MessageProcessorBuilder<?>> messageProcessorBuilders = new ArrayList<MessageProcessorBuilder<?>>();
    private String name;


    FlowBuilderImpl(String name)
    {
        this.name = name;
    }


    public FlowBuilder from(MessageSourceBuilder<?> messageSourceBuilder)
    {
        this.messageSourceBuilder = messageSourceBuilder;
        return this;
    }

    public FlowBuilder chain(MessageProcessorBuilder builder)
    {
        messageProcessorBuilders.add(builder);
        return this;
    }


    public Flow build(MuleContext muleContext) throws  ConfigurationException, IllegalStateException
    {

        final List<MessageProcessor> messageProcessorList = new ArrayList<MessageProcessor>();

        for (MessageProcessorBuilder<?> messageProcessorBuilder : messageProcessorBuilders)
        {
            messageProcessorList.add(messageProcessorBuilder.build(muleContext));
        }

        final Flow flow = new Flow(name, muleContext);
        if (messageSourceBuilder != null)
        {
            MessageSource messageSource = messageSourceBuilder.build(muleContext);
            flow.setMessageSource(messageSource);
        }
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
