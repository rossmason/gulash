package org.mule.dsl.builder.apikit;


import org.mule.construct.Flow;
import org.mule.dsl.builder.core.FlowBuilder;
import org.mule.dsl.builder.core.MuleBuilder;

import java.util.Map;

public interface RestRouterBuilder extends MuleBuilder<Flow>
{

    RestRouterBuilder using(Map<String, Object> properties);

    RestRouterBuilder when(FlowBuilder flowBuilder);
}
