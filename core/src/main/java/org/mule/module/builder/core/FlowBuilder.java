package org.mule.module.builder.core;


import org.mule.api.exception.MessagingExceptionHandler;
import org.mule.config.dsl.Builder;

public interface FlowBuilder extends PrivateFlowBuilder
{

    PrivateFlowBuilder on(MessageSourceBuilder messageSourceBuilder);

    PrivateFlowBuilder onException(Builder<MessagingExceptionHandler> exceptionBuilder);

}
