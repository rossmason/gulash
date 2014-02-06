package org.mule.dsl.builder;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;

import java.util.ArrayList;
import java.util.List;


public class ApikitBuilderImpl implements ApikitBuilder
{

    private List<RestRouterBuilder> routerBuilders = new ArrayList<RestRouterBuilder>();

    @Override
    public RestRouterBuilder<ApikitBuilder> declareApi(String ramlPath)
    {
        RestRouterBuilderImpl<ApikitBuilder> restRouter = new RestRouterBuilderImpl<ApikitBuilder>(ramlPath, this);
        routerBuilders.add(restRouter);
        return restRouter;
    }

    @Override
    public Object build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {
        for (RestRouterBuilder routerBuilder : routerBuilders)
        {
            routerBuilder.build(muleContext);
        }
        return null;
    }
}
