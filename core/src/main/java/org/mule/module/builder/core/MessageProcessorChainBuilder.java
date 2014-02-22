package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.processor.MessageProcessorChain;
import org.mule.config.dsl.Builder;
import org.mule.processor.chain.DefaultMessageProcessorChainBuilder;

import java.util.Arrays;
import java.util.List;


public class MessageProcessorChainBuilder extends AbstractPipelineBuilder implements MessageProcessorBuilder<MessageProcessorChain>
{


    public MessageProcessorBuilder chain(Builder<? extends MessageProcessor>... messageProcessors)
    {
        getMessageProcessorBuilders().addAll(Arrays.asList(messageProcessors));
        return this;
    }

    @Override
    public MessageProcessorChain create(MuleContext muleContext)
    {
        List<MessageProcessor> messageProcessors = buildPipelineMessageProcessors(muleContext);
        try
        {
            return new DefaultMessageProcessorChainBuilder().chain(messageProcessors).build();
        }
        catch (MuleException e)
        {
            throw new BuilderConfigurationException("Exception while creating chain.", e);
        }
    }
}
