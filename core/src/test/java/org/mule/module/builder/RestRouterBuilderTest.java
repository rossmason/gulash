package org.mule.module.builder;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mule.module.Apikit.api;
import static org.mule.module.Core.log;
import org.mule.api.MuleException;
import org.mule.module.core.Mule;

import com.jayway.restassured.RestAssured;

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
        startApp();
        given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(containsString("hola"))
                .statusCode(200)
                .when().put("/api/forward");
    }

    @Test
    public void notAcceptable() throws MuleException
    {
        startApp();
        given().header("Accept", "application/json").body("hola")
                .expect()
                .response().body(containsString(""))
                .statusCode(406)
                .when().put("/api/forward");
    }

    @Test
    public void notFound() throws MuleException
    {
        startApp();
        given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(containsString(""))
                .statusCode(404)
                .when().put("/api/notfound");
    }

    @Test
    public void methodNotAllowed() throws MuleException
    {
        startApp();
        given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(containsString(""))
                .statusCode(405)
                .when().post("/api/forward");
    }

    private void startApp() throws MuleException
    {
        Mule mule = new Mule();
        mule.declare(
                api("rover.raml")
                        .on("/forward", ActionType.PUT)
                        .then(
                                log("#[payload]")
                        )
        );
        mule.start();
    }

}
