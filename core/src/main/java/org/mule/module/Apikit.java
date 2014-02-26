package org.mule.module;

import org.mule.module.apikit.builder.RestRouterBuilder;


public class Apikit
{

    public static RestRouterBuilder api(String ramlPath)
    {
        return new RestRouterBuilder(ramlPath);
    }



}
