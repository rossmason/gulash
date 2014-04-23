package org.mule.module;


import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.module.core.builder.*;

import java.util.Arrays;

public class Core
{

    public static LoggerBuilder log(String message)
    {
        return new LoggerBuilder(message);
    }

    public static ForeachBuilder foreach()
    {
        return new ForeachBuilder();
    }

    public static ForeachBuilder foreach(String collectionExpression)
    {
        return new ForeachBuilder(collectionExpression);
    }

    public static FlowBuilder flow(String name)
    {
        return new FlowBuilderImpl(name);
    }

    public static EnricherBuilder enrich(String target)
    {
        return new EnricherBuilder(target);
    }

    public static SetPayloadBuilder setPayload(String expression)
    {
        return new SetPayloadBuilder(expression);
    }

    public static PollBuilder poll(Builder<MessageProcessor> pollOver)
    {
        return new PollBuilder(pollOver);
    }

    public static ChoiceBuilder choice()
    {
        return new ChoiceBuilder();
    }

    public static IfBuilder when(String condition)
    {
        return new IfBuilder(condition);
    }

    public static AsyncBuilder async(Builder<MessageProcessor>... messageProcessors)
    {
        return new AsyncBuilder(Arrays.asList(messageProcessors));
    }

    public static <T> JavaBeanElementBuilder<T> bean(Class<T> globalElementClass)
    {
        return new JavaBeanElementBuilder<T>(globalElementClass);
    }

    public static FlowRefBuilder ref(String flowName)
    {
        return new FlowRefBuilder(flowName);
    }

    public static InboundEndpointBuilder endpoint(String address)
    {
        return new InboundEndpointBuilder(address);
    }

    public static <T extends MessageProcessor> CustomMessageProcessorBuilder<T> invoke(Class<T> clazz)
    {
        return new CustomMessageProcessorBuilderImpl<T>(clazz);
    }



}
