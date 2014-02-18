package org.mule.dsl.builder.apikit;


import org.mule.construct.Flow;
import org.mule.dsl.builder.core.MuleBuilder;
import org.mule.dsl.builder.core.PrivateFlowBuilder;

import java.util.Map;

public interface RestRouterBuilder extends MuleBuilder<Flow>
{

    RestRouterBuilder using(Map<String, Object> properties);

    RestRouterBuilder on(PrivateFlowBuilder flowBuilder);
}
