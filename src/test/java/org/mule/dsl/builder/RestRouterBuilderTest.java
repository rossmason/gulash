package org.mule.dsl.builder;

import static org.mule.dsl.builder.Properties.properties;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.context.DefaultMuleContextFactory;

import com.jayway.restassured.RestAssured;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.raml.model.ActionType;

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
        ApikitBuilderImpl routerBuilder = new ApikitBuilderImpl();
        routerBuilder
                .declareApi("rover.raml")
                /**/.using(properties("consolePath", "/console", "name", "test"))
                /**/.on(ActionType.PUT, "/forward")
                /**//**/.chainLogger()
                /**//**//**/.using(properties("message", "#[payload]"))
                /**/.end()
                .end();


        final MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext();
        routerBuilder.build(muleContext);
        muleContext.start();


        RestAssured.given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(CoreMatchers.containsString("hola"))
                .statusCode(200)
                .when().put("/api/forward");

    }


}
