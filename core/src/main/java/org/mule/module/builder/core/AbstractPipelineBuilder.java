package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by machaval on 2/12/14.
 */
public class AbstractPipelineBuilder
{

    private List<Builder<? extends MessageProcessor>> messageProcessorBuilders = new ArrayList<Builder<? extends MessageProcessor>>();

    protected List<MessageProcessor> buildPipelineMessageProcessors(MuleContext muleContext)
    {
        List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();
        for (Builder<? extends MessageProcessor> messageProcessorBuilder : getMessageProcessorBuilders())
        {
            messageProcessors.add(messageProcessorBuilder.create(muleContext));
        }
        return messageProcessors;
    }

    protected List<Builder<? extends MessageProcessor>> getMessageProcessorBuilders()
    {
        return messageProcessorBuilders;
    }

    protected void setMessageProcessorBuilders(List<Builder<? extends MessageProcessor>> messageProcessorBuilders)
    {
        this.messageProcessorBuilders = messageProcessorBuilders;
    }
}
