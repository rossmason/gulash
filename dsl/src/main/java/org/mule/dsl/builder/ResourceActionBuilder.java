package org.mule.dsl.builder;

import org.mule.api.processor.MessageProcessor;

import java.util.Map;


public interface ResourceActionBuilder<P>
{


    ResourceActionBuilder<P> addMessageProcessor(Class<? extends MessageProcessor> clazz);

    ResourceActionBuilder<P> using(Map<String, Object> properties);

    P end();


}
