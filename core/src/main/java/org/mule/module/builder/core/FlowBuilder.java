package org.mule.module.builder.core;


public interface FlowBuilder extends PrivateFlowBuilder
{

    PrivateFlowBuilder on(MessageSourceBuilder messageSourceBuilder);

}
