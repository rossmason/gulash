package org.mule.module.builder;

import com.jayway.restassured.RestAssured;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.transformer.TransformerException;
import org.mule.module.core.Mule;
import org.mule.transformer.AbstractTransformer;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mule.module.Core.*;

public class TransformerTest
{
    @Test
    public void transformerTest() throws MuleException, InterruptedException
    {
        RestAssured.port = 8080;
        Mule mule = new Mule();
        mule.declare(
            flow("Test")
                .on(endpoint("http://localhost:8080"))
                .then(transformer(DummyTransformer.class))
        );
        mule.start();

        given().header("Accept", "*/*")
            .expect()
            .response().body(containsString("sarasa"))
            .statusCode(200)
            .when().get("/hola");
    }

    public static class DummyTransformer extends AbstractTransformer
    {

        @Override
        protected Object doTransform(Object src, String enc) throws TransformerException
        {
            return "sarasa";
        }
    }
}
