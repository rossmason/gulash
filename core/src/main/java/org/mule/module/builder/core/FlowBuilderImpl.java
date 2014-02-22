package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.exception.MessagingExceptionHandler;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.source.MessageSource;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;

import java.util.Arrays;
import java.util.List;


public class FlowBuilderImpl extends AbstractPipelineBuilder implements FlowBuilder
{


    private MessageSourceBuilder<?> messageSourceBuilder;
    private String name;
    private Builder<MessagingExceptionHandler> exceptionBuilder;

    public FlowBuilderImpl(String name)
    {
        this.name = name;
    }

    public PrivateFlowBuilder on(MessageSourceBuilder messageSourceBuilder)
    {
        this.messageSourceBuilder = messageSourceBuilder;
        return this;
    }

    @Override
    public PrivateFlowBuilder onException(Builder<MessagingExceptionHandler> exceptionBuilder)
    {
        this.exceptionBuilder = exceptionBuilder;
        return this;
    }

    public PrivateFlowBuilder then(Builder<? extends MessageProcessor>... builder)
    {
        getMessageProcessorBuilders().addAll(Arrays.asList(builder));
        return this;
    }


    public Flow create(MuleContext muleContext)
    {

        final List<MessageProcessor> messageProcessorList = buildPipelineMessageProcessors(muleContext);

        final Flow flow = new Flow(name, muleContext);
        if (messageSourceBuilder != null)
        {
            MessageSource messageSource = messageSourceBuilder.create(muleContext);
            flow.setMessageSource(messageSource);
        }
        flow.setMessageProcessors(messageProcessorList);
        if (exceptionBuilder != null)
        {
            flow.setExceptionListener(exceptionBuilder.create(muleContext));
        }
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
