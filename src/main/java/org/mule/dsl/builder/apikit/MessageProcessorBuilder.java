package org.mule.dsl.builder.apikit;

import org.mule.api.processor.MessageProcessor;
import org.mule.dsl.builder.core.MuleBuilder;

import java.util.Map;


public interface MessageProcessorBuilder<T extends MessageProcessor> extends MuleBuilder<T>
{

    MessageProcessorBuilder<T> using(Map<String, Object> properties);

}
