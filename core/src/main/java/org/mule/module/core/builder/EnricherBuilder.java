package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.enricher.MessageEnricher;

import java.util.ArrayList;
import java.util.List;


public class EnricherBuilder implements MessageProcessorBuilder<MessageEnricher>
{

    private List<MessageEnricher.EnrichExpressionPair> enrichmentExpressions = new ArrayList<MessageEnricher.EnrichExpressionPair>();
    private MessageProcessorChainBuilder chainBuilder = new MessageProcessorChainBuilder();

    public EnricherBuilder(String target)
    {
        this.enrichmentExpressions.add(new MessageEnricher.EnrichExpressionPair(target));
    }

    public EnricherBuilder(String target, String source)
    {
        this.enrichmentExpressions.add(new MessageEnricher.EnrichExpressionPair(target, source));
    }

    public EnricherBuilder with(Builder<MessageProcessor>... mps)
    {
        chainBuilder.chain(mps);
        return this;
    }

    public EnricherBuilder then(Builder<MessageProcessor>... mps)
    {
        return with(mps);
    }

    @Override
    public MessageEnricher create(MuleContext muleContext)
    {
        final MessageEnricher result = new MessageEnricher();
        for (MessageEnricher.EnrichExpressionPair enrichmentExpression : enrichmentExpressions)
        {
            result.addEnrichExpressionPair(enrichmentExpression);
        }
        result.setEnrichmentMessageProcessor(chainBuilder.create(muleContext));
        return result;
    }
}
