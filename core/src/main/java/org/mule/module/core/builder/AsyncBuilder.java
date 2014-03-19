package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.processor.MessageProcessorChain;
import org.mule.config.dsl.Builder;
import org.mule.construct.flow.DefaultFlowProcessingStrategy;
import org.mule.module.core.BuilderConfigurationException;
import org.mule.processor.AsyncDelegateMessageProcessor;
import org.mule.processor.chain.SimpleMessageProcessorChainBuilder;

import java.util.List;

//todo processing strategy???
public class AsyncBuilder implements MessageProcessorBuilder<AsyncDelegateMessageProcessor>
{

    private List<Builder<MessageProcessor>> messageProcessors;

    public AsyncBuilder(List<Builder<MessageProcessor>> messageProcessors)
    {
        this.messageProcessors = messageProcessors;
    }

    @Override
    public AsyncDelegateMessageProcessor create(MuleContext muleContext)
    {
        try
        {
            final MessageProcessorChain chain = new SimpleMessageProcessorChainBuilder().chain(BuilderUtils.build(muleContext, messageProcessors)).build();
            final AsyncDelegateMessageProcessor asyncDelegateMessageProcessor = new AsyncDelegateMessageProcessor(chain, new DefaultFlowProcessingStrategy(), "");
            asyncDelegateMessageProcessor.setMuleContext(muleContext);
            return asyncDelegateMessageProcessor;
        }
        catch (MuleException e)
        {
            throw new BuilderConfigurationException("Error while configuring Async", e);
        }


    }
}
