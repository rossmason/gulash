package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.construct.Flow;

/**
 * Created by machaval on 2/17/14.
 */
public interface PrivateFlowBuilder extends MessageProcessorBuilder<Flow>
{

    PrivateFlowBuilder then(MessageProcessorBuilder... builder);

    @Override
    Flow build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException;
}
