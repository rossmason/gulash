package org.mule.module.core.processor;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.processor.MessageProcessorChain;
import org.mule.processor.AbstractMessageProcessorOwner;
import org.mule.processor.chain.SimpleMessageProcessorChainBuilder;
import org.mule.util.AttributeEvaluator;

import java.util.List;


public class IfMessageProcessor extends AbstractMessageProcessorOwner implements MessageProcessor
{

    private List<MessageProcessor> ifProcessors;
    private String condition;
    private MessageProcessorChain ifChain;


    public IfMessageProcessor(String condition)
    {
        this.condition = condition;
    }

    @Override
    public void start() throws MuleException
    {
        super.start();
        this.ifChain = new SimpleMessageProcessorChainBuilder().chain(ifProcessors).build();
    }

    @Override
    public MuleEvent process(final MuleEvent event) throws MuleException
    {
        MuleEvent result = event;
        AttributeEvaluator evaluator = new AttributeEvaluator(condition);
        evaluator.initialize(getMuleContext().getExpressionManager());
        Object conditionResult = evaluator.resolveValue(event.getMessage());

        if (conditionResult instanceof Boolean)
        {
            if ((Boolean) conditionResult)
            {
                result = ifChain.process(event);
            }

        }
        else if (conditionResult != null)
        {
            result = ifChain.process(event);
        }


        return result;
    }

    public void setIfProcessors(List<MessageProcessor> ifProcessors)
    {
        this.ifProcessors = ifProcessors;
    }


    @Override
    protected List<MessageProcessor> getOwnedMessageProcessors()
    {
        return ifProcessors;
    }
}
