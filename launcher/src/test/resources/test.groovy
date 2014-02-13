import org.mule.dsl.builder.core.StartListener

mule.declare(
                flow("Hello")
                        .chain(logger().withMessage("#[payload]"))

        );

mule.onStart(
    { mule ->
        def result = mule.callFlow("Hello", "Hello");
        def payload = result.getMessage().getPayload()
        println "payload = $payload"
    } as StartListener)
