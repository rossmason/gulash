package org.mule.gulash

import org.junit._
import org.mule.module.core.Mule
import org.mule.module.Core._
import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured._
import org.hamcrest.CoreMatchers._
import org.mule.module.ScalaCore._
import org.mule.module.core.builder.{PrivateFlowBuilder, FlowBuilder, FlowBuilderImpl}
import org.mule.config.dsl.Builder
import org.mule.api.processor.MessageProcessor
import org.mule.api.source.MessageSource
import org.mule.api.exception.MessagingExceptionHandler
import org.mule.api.MuleContext

class ScalaCoreTest {

    var mule: Mule = _

    @Before
    def before {
        RestAssured.port = 8081
        mule = new Mule
    }

    @After
    def after {
        mule.stop()
    }

    @Test
    def payloadIsModifiedWithClosure {
        mule.declare {
            flow("Test")
                .on(endpoint("http://localhost:8081"))
                .then(call(message => "cool!"))
        }
        mule.start()

        given.header("Accept", "*/*").expect.response.body(containsString("cool!")).statusCode(200).when.get("/hola")
    }

    @Test
    def payloadIsNotModifiedWhenClosureReturnsNoValue {
        mule.declare {
            flow("Test").on(endpoint("http://localhost:8081")).then(call(message => println("this shouldn't be in the payload")))
        }
        mule.start()

        given.header("Accept", "*/*").expect.response.body(containsString("/hola")).statusCode(200).when.get("/hola")
    }

    @Test
    def usingArrowInsteadOfThen {
        mule.declare {
            flow("Test").on(endpoint("http://localhost:8081")) --> log("pepe")
        }
        mule.start()

        given.header("Accept", "*/*").expect.response.body(containsString("/hola")).statusCode(200).when.get("/hola")
    }

    @Test
    def usingArrowWithMoreProcessors {
        mule.declare {
            flow("Test").on(endpoint("http://localhost:8081")) --> (log("pepe"), log("pepe2"), call(message => "yay!"))
        }
        mule.start()

        given.header("Accept", "*/*").expect.response.body(containsString("yay!")).statusCode(200).when.get("/hola")
    }

    implicit def toScalaFlowBuilder(builder: PrivateFlowBuilder): ScalaFlowBuilderImpl = new ScalaFlowBuilderImpl(builder)

    class ScalaFlowBuilderImpl(val builder: PrivateFlowBuilder) {
        def -->(nextBuilder: Builder[_ <: MessageProcessor]*) = builder.then(nextBuilder: _*)
    }

}


