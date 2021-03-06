package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.routing.Foreach;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class ForeachBuilder extends AbstractPipelineBuilder implements MessageProcessorBuilder<Foreach>
{

    private int batchSize = 1;
    private String collectionExpression;

    public ForeachBuilder()
    {
    }

    public ForeachBuilder(String collectionExpression)
    {
        this.collectionExpression = collectionExpression;
    }

    public ForeachBuilder usingBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
        return this;
    }


    public ForeachBuilder then(Builder<? extends MessageProcessor>... builders)
    {
        getMessageProcessorBuilders().addAll(Arrays.asList(builders));
        return this;
    }

    @Override
    public Foreach create(MuleContext muleContext)
    {

        Foreach foreach = new Foreach();
        foreach.setBatchSize(batchSize);
        if (!StringUtils.isEmpty(collectionExpression))
        {
            foreach.setCollectionExpression(collectionExpression);
        }
        List<MessageProcessor> messageProcessors = buildPipelineMessageProcessors(muleContext);
        try
        {
            foreach.setMessageProcessors(messageProcessors);
        }
        catch (MuleException e)
        {
            e.printStackTrace();
        }
        return foreach;
    }

}
