package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.routing.Foreach;

import java.util.List;

import org.apache.commons.lang.StringUtils;


public class ForeachBuilder extends AbstractPipelineBuilder implements MessageProcessorBuilder<Foreach>
{

    private int batchSize = 1;
    private String collectionExpression;

    public ForeachBuilder()
    {
    }


    public ForeachBuilder usingBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
        return this;
    }

    public ForeachBuilder usingCollectionExpression(String collectionExpression)
    {
        this.collectionExpression = collectionExpression;
        return this;
    }


    public ForeachBuilder chain(MessageProcessorBuilder builder)
    {
        getMessageProcessorBuilders().add(builder);
        return this;
    }

    @Override
    public Foreach build(MuleContext muleContext) throws ConfigurationException, IllegalStateException
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
