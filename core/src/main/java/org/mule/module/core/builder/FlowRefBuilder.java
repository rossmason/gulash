package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.module.core.processor.FlowRefMessageProcessor;


public class FlowRefBuilder implements MessageProcessorBuilder<FlowRefMessageProcessor>
{

    private String flowReference;

    public FlowRefBuilder(String flowReference)
    {
        this.flowReference = flowReference;
    }

    @Override
    public FlowRefMessageProcessor create(MuleContext muleContext)
    {
        FlowRefMessageProcessor flowRefMessageProcessor = new FlowRefMessageProcessor();
        flowRefMessageProcessor.setFlowReference(flowReference);
        return flowRefMessageProcessor;
    }
}
