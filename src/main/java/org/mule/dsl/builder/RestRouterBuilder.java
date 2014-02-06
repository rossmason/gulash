package org.mule.dsl.builder;


import org.mule.construct.Flow;

import java.util.Map;

import org.raml.model.ActionType;

public interface RestRouterBuilder<P> extends MuleBuilder<Flow>
{


    RestRouterBuilder<P> using(Map<String, Object> propertoes);

    MessageProcessorChainBuilder<RestRouterBuilder<P>> on(ActionType action, String resource);

    P end();


}
