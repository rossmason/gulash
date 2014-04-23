package org.mule.module.builder;

import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.module.core.Mule;
import org.raml.model.ActionType;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mule.module.Apikit.api;
import static org.mule.module.Core.log;

public class RestRouterBuilderTest
{
    private Mule mule;

    @Before
    public void setup() throws MuleException
    {
        RestAssured.port = 8081;
        startApp();
    }

    @After
    public void tearDown() throws MuleException
    {
        mule.stop();
    }


    @Test
    public void simple() throws MuleException
    {
        given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(containsString("hola"))
                .statusCode(200)
                .when().put("/api/forward");
    }

    @Test
    public void notAcceptable() throws MuleException
    {
        given().header("Accept", "application/json").body("hola")
                .expect()
                .response().body(containsString(""))
                .statusCode(406)
                .when().put("/api/forward");
    }

    @Test
    public void notFound() throws MuleException
    {
        given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(containsString(""))
                .statusCode(404)
                .when().put("/api/notfound");
    }

    @Test
    public void methodNotAllowed() throws MuleException
    {
        given().header("Accept", "*/*").body("hola")
                .expect()
                .response().body(containsString(""))
                .statusCode(405)
                .when().post("/api/forward");
    }

    private void startApp() throws MuleException
    {
        mule = new Mule();
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
