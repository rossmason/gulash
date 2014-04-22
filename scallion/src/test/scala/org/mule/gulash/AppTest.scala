package org.mule.gulash

import org.junit._
import Assert._
import org.mule.module.core.Mule
import org.mule.module.Core._
import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured._
import org.hamcrest.CoreMatchers._
import org.mule.transformer.AbstractTransformer
import org.mule.module.core.builder.TransformerBuilderImpl

class AppTest {

    @Test
    def test {
        val mule = new Mule()
        mule.declare(flow("shoki")
            .on(endpoint("http://localhost:8081"))
            .then(log("lalala"))
        )
        mule.start()


    }

    class Trans(x: Object => Object) extends AbstractTransformer {
        override def doTransform(src: Object, enc: String) = x(src, "UTF-8")
    }

    class TransBuilder(x: Object => Object) extends TransformerBuilderImpl {
        override def create() = new Trans(x)
    }

    @Test def transformerTest {
        implicit def toClass(x: Object => Object): TransformerBuilderImpl = new TransBuilder(x)

        RestAssured.port = 8081
        val mule = new Mule
        mule.declare(flow("Test").on(endpoint("http://localhost:8081")).then(transformer((payload: Object) => "sarasa")))
        mule.start()

        given.header("Accept", "*/*").expect.response.body(containsString("sarasa")).statusCode(200).when.get("/hola")
    }

}


