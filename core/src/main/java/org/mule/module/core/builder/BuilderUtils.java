package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by machaval on 2/26/14.
 */
public class BuilderUtils
{


    public static List<MessageProcessor> build(MuleContext muleContext, List<Builder<MessageProcessor>> messageProcessorsBuilders)
    {
        List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();
        for (Builder<? extends MessageProcessor> messageProcessorBuilder : messageProcessorsBuilders)
        {
            messageProcessors.add(messageProcessorBuilder.create(muleContext));
        }
        return messageProcessors;
    }
}
