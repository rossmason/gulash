package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.module.core.processor.IfMessageProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class IfBuilder implements MessageProcessorBuilder<IfMessageProcessor>
{

    private String condition;
    private List<Builder<MessageProcessor>> ifMessageProcessors;

    public IfBuilder(String condition)
    {
        this.condition = condition;
        this.ifMessageProcessors = new ArrayList<Builder<MessageProcessor>>();
    }

    public IfBuilder then(Builder<MessageProcessor>... messageProcessorBuilders)
    {
        ifMessageProcessors.addAll(Arrays.asList(messageProcessorBuilders));
        return this;
    }

    @Override
    public IfMessageProcessor create(MuleContext muleContext)
    {
        final IfMessageProcessor result = new IfMessageProcessor(condition);
        result.setMuleContext(muleContext);
        result.setIfProcessors(BuilderUtils.build(muleContext, ifMessageProcessors));
        return result;
    }

}
