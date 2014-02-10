package org.mule.dsl.builder.core;

import org.mule.api.processor.MessageProcessor;
import org.mule.dsl.builder.apikit.MessageProcessorBuilder;

import java.util.Map;


public interface CustomMessageProcessorBuilder<T extends MessageProcessor> extends MessageProcessorBuilder<T>
{

    CustomMessageProcessorBuilder<T> using(Map<String, Object> properties);
}
