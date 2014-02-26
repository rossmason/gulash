package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.exception.MessagingExceptionHandler;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;

/**
 * Created by machaval on 2/17/14.
 */
public interface PrivateFlowBuilder extends MessageProcessorBuilder<Flow>
{

    PrivateFlowBuilder then(Builder<? extends MessageProcessor>... builder);

    PrivateFlowBuilder onException(Builder<MessagingExceptionHandler> exceptionBuilder);

    @Override
    Flow create(MuleContext muleContext);
}
