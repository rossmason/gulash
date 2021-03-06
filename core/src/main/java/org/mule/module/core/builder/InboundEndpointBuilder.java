package org.mule.module.core.builder;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.endpoint.EndpointException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.config.dsl.Builder;
import org.mule.endpoint.EndpointURIEndpointBuilder;


public class InboundEndpointBuilder implements Builder<InboundEndpoint>
{

    private String url;
    private MessageExchangePattern exchangePattern;

    public InboundEndpointBuilder(String url)
    {
        this.url = url;
    }

    public InboundEndpointBuilder usingExchangePattern(MessageExchangePattern exchangePattern)
    {
        this.exchangePattern = exchangePattern;
        return this;
    }

    @Override
    public InboundEndpoint create(MuleContext muleContext)
    {
        final EndpointURIEndpointBuilder endpointURIEndpointBuilder = new EndpointURIEndpointBuilder(url, muleContext);
        try
        {
            if (exchangePattern != null)
            {
                endpointURIEndpointBuilder.setExchangePattern(exchangePattern);
            }
            final InboundEndpoint inboundEndpoint = endpointURIEndpointBuilder.buildInboundEndpoint();
            muleContext.getRegistry().registerEndpoint(inboundEndpoint);
            return inboundEndpoint;
        }
        catch (EndpointException e)
        {
            throw new IllegalStateException(e);
        }
        catch (InitialisationException e)
        {
            throw new IllegalStateException(e);
        }
        catch (MuleException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
