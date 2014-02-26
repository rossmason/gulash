package org.mule.module;


import groovy.lang.Closure;

import org.mule.api.processor.MessageProcessor;
import org.mule.module.core.builder.ChoiceBuilder;
import org.mule.module.core.builder.JavaBeanElementBuilder;
import org.mule.module.core.builder.CustomMessageProcessorBuilder;
import org.mule.module.core.builder.CustomMessageProcessorBuilderImpl;
import org.mule.module.core.builder.FlowBuilder;
import org.mule.module.core.builder.FlowBuilderImpl;
import org.mule.module.core.builder.ForeachBuilder;
import org.mule.module.core.builder.GroovyBuilder;
import org.mule.module.core.builder.InboundEndpointBuilder;
import org.mule.module.core.builder.LoggerBuilder;

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

    public static <T> JavaBeanElementBuilder<T> bean(Class<T> globalElementClass)
    {
        return new JavaBeanElementBuilder<T>(globalElementClass);
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
