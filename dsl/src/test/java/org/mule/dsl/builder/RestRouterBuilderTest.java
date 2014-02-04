package org.mule.dsl.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.processor.LoggerMessageProcessor;
import org.mule.context.DefaultMuleContextFactory;

import com.google.common.collect.ImmutableMap;
import com.jayway.restassured.RestAssured;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.raml.model.ActionType;

/**
 * Created by machaval on 2/3/14.
 */
public class RestRouterBuilderTest
{


    @Before
    public void setup()
    {
        RestAssured.port = 8081;
    }


    @Test
    public void simple() throws MuleException
    {
        RestRouterBuilder routerBuilder = new RestRouterBuilderImpl();

        routerBuilder.declareApi("rover.raml")
                .using(ImmutableMap.<String, Object>of("consolePath", "/console","name","test"))
                .implementResourceAction(ActionType.PUT, "/forward")
                .addMessageProcessor(LoggerMessageProcessor.class)
                        //TODO machaval review how to configure
                .using(ImmutableMap.<String, Object>of("message", "hola"))
                .end();


        MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext();
        routerBuilder.build(muleContext);
        muleContext.start();


        RestAssured.given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(CoreMatchers.containsString("hola"))
                .header("Content-type", "text/plain").statusCode(200)
                .when().put("/api/forward");

    }


}
