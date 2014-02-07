package org.mule.dsl.builder.apikit;

import org.mule.dsl.builder.core.MuleBuilder;

/**
 * Created by machaval on 2/5/14.
 */
public interface ApikitBuilder extends MuleBuilder
{

    RestRouterBuilder<ApikitBuilder> declareApi(String ramlPath);

}
