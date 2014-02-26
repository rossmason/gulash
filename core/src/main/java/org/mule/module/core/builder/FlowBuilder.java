package org.mule.module.core.builder;


public interface FlowBuilder extends PrivateFlowBuilder
{

    PrivateFlowBuilder on(MessageSourceBuilder messageSourceBuilder);

}
