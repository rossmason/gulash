package org.mule.dsl.builder.core;


import org.mule.api.processor.MessageProcessor;

public class Core
{

    public static LoggerBuilder logger()
    {
        return new LoggerBuilder();
    }

    public static FlowBuilder flow(String name)
    {
        return new FlowBuilderImpl(name);
    }

    public static InboundEndpointBuilder endpoint(String address)
    {
        return new InboundEndpointBuilder(address);
    }

    //Todo add Foreach, Poll and Choice

    public static <T extends MessageProcessor> CustomMessageProcessorBuilder<T> custom(Class<T> clazz)
    {
        return new CustomMessageProcessorBuilderImpl<T>(clazz);
    }

}
