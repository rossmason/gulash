package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;

/**
 * Created by machaval on 2/17/14.
 */
public interface PrivateFlowBuilder extends MessageProcessorBuilder<Flow>
{

    PrivateFlowBuilder then(Builder<? extends MessageProcessor>... builder);

    @Override
    Flow create(MuleContext muleContext);
}
