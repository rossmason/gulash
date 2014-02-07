package org.mule.dsl.builder.core;

import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.dsl.builder.apikit.MessageProcessorBuilder;

import java.util.Map;


public interface FlowBuilder<P> extends MessageProcessorBuilder<Flow>
{

    FlowBuilder<P> chainLogger();

    FlowBuilder<P> chain(Class<? extends MessageProcessor> clazz);

    FlowBuilder<P> chain(MessageProcessorBuilder builder);

    FlowBuilder<P> using(Map<String, Object> properties);

    P end();


}
