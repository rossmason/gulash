package org.mule.dsl.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.dsl.builder.apikit.MessageProcessorBuilder;

import java.util.Map;


public interface FlowBuilder extends MessageProcessorBuilder<Flow>
{

    FlowBuilder chain(MessageProcessorBuilder builder);

    FlowBuilder from(MessageSourceBuilder messageSourceBuilder);

    @Override
    Flow build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException;
}
