package org.mule.module;

import org.mule.module.Core;
import org.mule.module.builder.apikit.RestRouterBuilder;
import org.mule.module.builder.apikit.RestRouterBuilderImpl;
import org.mule.module.builder.core.PrivateFlowBuilder;

import org.raml.model.ActionType;


public class Apikit
{

    public static RestRouterBuilder api(String ramlPath)
    {
        return new RestRouterBuilderImpl(ramlPath);
    }

    public static PrivateFlowBuilder request(String resource, ActionType action)
    {
        return Core.flow(action.name().toLowerCase() + ":" + resource);
    }


}
