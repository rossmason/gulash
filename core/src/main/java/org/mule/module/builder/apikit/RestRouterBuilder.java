package org.mule.module.builder.apikit;


import org.mule.construct.Flow;
import org.mule.module.builder.core.MuleBuilder;
import org.mule.module.builder.core.PrivateFlowBuilder;

import java.util.Map;

public interface RestRouterBuilder extends MuleBuilder<Flow>
{

    RestRouterBuilder using(Map<String, Object> properties);

    RestRouterBuilder on(PrivateFlowBuilder flowBuilder);
}
