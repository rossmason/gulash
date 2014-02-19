package org.mule.module;

import org.mule.module.builder.apikit.RestRouterBuilder;


public class Apikit
{

    public static RestRouterBuilder api(String ramlPath)
    {
        return new RestRouterBuilder(ramlPath);
    }



}
