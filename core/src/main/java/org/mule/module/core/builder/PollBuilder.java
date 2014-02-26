package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.endpoint.EndpointException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.endpoint.EndpointURIEndpointBuilder;
import org.mule.endpoint.URIBuilder;
import org.mule.module.core.BuilderConfigurationException;
import org.mule.module.core.TimePeriod;
import org.mule.transport.AbstractConnector;
import org.mule.transport.polling.MessageProcessorPollingMessageReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by machaval on 2/26/14.
 */
public class PollBuilder implements MessageSourceBuilder<InboundEndpoint>
{

    private Builder<MessageProcessor> messageProcessorBuilder;
    private long frequency = TimePeriod.MINUTES.toMillis(2);

    public PollBuilder(Builder<MessageProcessor> messageProcessorBuilder)
    {
        this.messageProcessorBuilder = messageProcessorBuilder;
    }

    public PollBuilder every(long time, TimePeriod period)
    {
        this.frequency = period.toMillis(time);
        return this;
    }

    @Override
    public InboundEndpoint create(MuleContext muleContext)
    {

        final EndpointBuilder internalEndpointBuilder = new EndpointURIEndpointBuilder(new URIBuilder("polling://" + hashCode(), muleContext));
        final Map<Object, Object> properties = new HashMap<Object, Object>();
        properties.put(AbstractConnector.PROPERTY_POLLING_FREQUENCY, frequency);
        properties.put(MessageProcessorPollingMessageReceiver.SOURCE_MESSAGE_PROCESSOR_PROPERTY_NAME, messageProcessorBuilder.create(muleContext));
        internalEndpointBuilder.setProperties(properties);

        try
        {
            return internalEndpointBuilder.buildInboundEndpoint();
        }
        catch (EndpointException e)
        {
            throw new BuilderConfigurationException("Error while building poll", e);
        }
        catch (InitialisationException e)
        {
            throw new BuilderConfigurationException("Error while building poll", e);
        }
    }
}
