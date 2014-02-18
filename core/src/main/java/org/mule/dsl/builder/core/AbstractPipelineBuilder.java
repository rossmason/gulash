package org.mule.dsl.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by machaval on 2/12/14.
 */
public class AbstractPipelineBuilder
{

    private List<MessageProcessorBuilder<?>> messageProcessorBuilders = new ArrayList<MessageProcessorBuilder<?>>();

    protected List<MessageProcessor> buildPipelineMessageProcessors(MuleContext muleContext) throws ConfigurationException
    {
        List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();
        for (MessageProcessorBuilder<?> messageProcessorBuilder : getMessageProcessorBuilders())
        {
            messageProcessors.add(messageProcessorBuilder.build(muleContext));
        }
        return messageProcessors;
    }

    protected List<MessageProcessorBuilder<?>> getMessageProcessorBuilders()
    {
        return messageProcessorBuilders;
    }

    protected void setMessageProcessorBuilders(List<MessageProcessorBuilder<?>> messageProcessorBuilders)
    {
        this.messageProcessorBuilders = messageProcessorBuilders;
    }
}
