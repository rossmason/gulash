package org.mule.dsl.builder;

import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;

import java.util.Map;


public interface MessageProcessorChainBuilder<P> extends MessageProcessorBuilder<Flow>
{

    MessageProcessorChainBuilder<P> chainLogger();

    MessageProcessorChainBuilder<P> chain(Class<? extends MessageProcessor> clazz);

    MessageProcessorChainBuilder<P> chain(MessageProcessorBuilder builder);

    MessageProcessorChainBuilder<P> using(Map<String, Object> properties);

    P end();


}
