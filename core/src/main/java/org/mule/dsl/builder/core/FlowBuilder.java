package org.mule.dsl.builder.core;


public interface FlowBuilder extends PrivateFlowBuilder
{

    PrivateFlowBuilder on(MessageSourceBuilder messageSourceBuilder);

}
