package org.mule.dsl.builder;


import org.mule.construct.Flow;

import java.util.Map;

import org.raml.model.ActionType;

public interface RestRouterBuilder extends MuleBuilder<Flow>
{


    RestRouterBuilder declareApi(String ramlPath);

    RestRouterBuilder using(Map<String,Object> propertoes);

    ResourceActionBuilder<RestRouterBuilder> implementResourceAction(ActionType action,String resource);


}
