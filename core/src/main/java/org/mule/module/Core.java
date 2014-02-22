package org.mule.module;


import groovy.lang.Closure;

import org.mule.api.processor.MessageProcessor;
import org.mule.module.builder.core.ChoiceBuilder;
import org.mule.module.builder.core.CustomGlobalElementBuilder;
import org.mule.module.builder.core.CustomMessageProcessorBuilder;
import org.mule.module.builder.core.CustomMessageProcessorBuilderImpl;
import org.mule.module.builder.core.FlowBuilder;
import org.mule.module.builder.core.FlowBuilderImpl;
import org.mule.module.builder.core.ForeachBuilder;
import org.mule.module.builder.core.GlobalElementBuilder;
import org.mule.module.builder.core.GroovyBuilder;
import org.mule.module.builder.core.InboundEndpointBuilder;
import org.mule.module.builder.core.LoggerBuilder;

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

    public static ChoiceBuilder choice()
    {
        return new ChoiceBuilder();
    }

    public static <T> GlobalElementBuilder<T> global(Class<T> globalElementClass)
    {
        return new CustomGlobalElementBuilder<T>(globalElementClass);
    }

    public static InboundEndpointBuilder endpoint(String address)
    {
        return new InboundEndpointBuilder(address);
    }

    //Todo add Poll and Choice

    public static <T extends MessageProcessor> CustomMessageProcessorBuilder<T> invoke(Class<T> clazz)
    {
        return new CustomMessageProcessorBuilderImpl<T>(clazz);
    }

    public static GroovyBuilder call(Closure closure)
    {
        return new GroovyBuilder(closure);
    }

}
