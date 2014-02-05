package org.mule.dsl.builder;


import org.mule.construct.Flow;

import java.util.Map;

import org.raml.model.ActionType;

public interface RestRouterBuilder extends MuleBuilder<Flow>
{


    RestRouterBuilder declareApi(String ramlPath);

    RestRouterBuilder using(Map<String,Object> propertoes);

    MessageProcessorChainBuilder<RestRouterBuilder> on(ActionType action, String resource);


}
