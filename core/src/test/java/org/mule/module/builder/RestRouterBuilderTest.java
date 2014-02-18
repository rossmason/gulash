package org.mule.module.builder;

import static org.mule.module.Apikit.api;
import static org.mule.module.Apikit.request;
import static org.mule.module.Core.log;
import static org.mule.module.builder.core.PropertiesBuilder.properties;
import org.mule.api.MuleException;
import org.mule.module.builder.core.Mule;

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

        Mule mule = new Mule();
        mule.declare(
                api("rover.raml")
                        .using(properties("consolePath", "/console", "name", "test"))
                        .on(request("/forward", ActionType.PUT)
                                    .then(log("#[payload]"))
                        )
        );


        mule.start();
        RestAssured.given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(CoreMatchers.containsString("hola"))
                .statusCode(200)
                .when().put("/api/forward");

    }


}
