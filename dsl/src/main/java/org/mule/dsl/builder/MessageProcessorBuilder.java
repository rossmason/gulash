package org.mule.dsl.builder;

import org.mule.api.processor.MessageProcessor;

import java.util.Map;


public interface MessageProcessorBuilder<T extends MessageProcessor> extends MuleBuilder<T>
{

    MessageProcessorBuilder<T> using(Map<String, Object> properties);

}
