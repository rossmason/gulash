import org.mule.api.MuleEvent
import org.mule.dsl.builder.core.StartListener




mule.declare(
        flow("Hello")
                .then(log("#[payload]"),
                      call(
                              {
                                  MuleEvent event, payload ->
                                      println("function called");
                                      println(payload)
                                      return event
                              }
                      ).withParam("must evaluate #[payload]")
        )
);

mule.onStart(
        { mule ->
            def result = mule.callFlow("Hello", "Hello");
            def payload = result.getMessage().getPayload()
            println "payload = $payload"
        } as StartListener)
