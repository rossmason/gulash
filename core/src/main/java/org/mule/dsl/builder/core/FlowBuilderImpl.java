package org.mule.dsl.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.source.MessageSource;
import org.mule.construct.Flow;

import java.util.Arrays;
import java.util.List;


public class FlowBuilderImpl extends AbstractPipelineBuilder implements FlowBuilder
{


    private MessageSourceBuilder<?> messageSourceBuilder;
    private String name;

    FlowBuilderImpl(String name)
    {
        this.name = name;
    }

    public PrivateFlowBuilder on(MessageSourceBuilder messageSourceBuilder)
    {
        this.messageSourceBuilder = messageSourceBuilder;
        return this;
    }

    public PrivateFlowBuilder then(MessageProcessorBuilder... builder)
    {
        getMessageProcessorBuilders().addAll(Arrays.<MessageProcessorBuilder<?>>asList(builder));
        return this;
    }


    public Flow build(MuleContext muleContext) throws ConfigurationException, IllegalStateException
    {

        final List<MessageProcessor> messageProcessorList = buildPipelineMessageProcessors(muleContext);

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
