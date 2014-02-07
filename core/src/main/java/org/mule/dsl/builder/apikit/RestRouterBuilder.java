package org.mule.dsl.builder.apikit;


import org.mule.construct.Flow;
import org.mule.dsl.builder.core.FlowBuilder;
import org.mule.dsl.builder.core.MuleBuilder;

import java.util.Map;

import org.raml.model.ActionType;

public interface RestRouterBuilder<P> extends MuleBuilder<Flow>
{


    RestRouterBuilder<P> using(Map<String, Object> propertoes);

    FlowBuilder<RestRouterBuilder<P>> on(ActionType action, String resource);

    P end();


}
