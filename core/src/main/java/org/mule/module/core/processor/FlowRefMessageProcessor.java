package org.mule.module.core.processor;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.context.MuleContextAware;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;


public class FlowRefMessageProcessor implements MessageProcessor, MuleContextAware
{

    private MuleContext context;
    private String flowReference;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        final FlowConstruct flowConstruct = context.getRegistry().lookupFlowConstruct(flowReference);
        final Flow flow = (Flow) flowConstruct;
        return flow.process(event);
    }

    public void setFlowReference(String flowReference)
    {
        this.flowReference = flowReference;
    }

    @Override
    public void setMuleContext(MuleContext context)
    {
        this.context = context;
    }
}
