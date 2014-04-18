declare(
        flow("Hello")
                .then(log("#[payload]"),
                      call(
                              {
                                  message ->
                                      println("function called");
                                      println(message.payload)
                                      return "OK"
                              }
                      )
        )
);

onStart(
        { mule ->
            def result = mule.callFlow("Hello", "Hello");
            def payload = result.getMessage().getPayload()
            println "payload = $payload"
        } as StartListener)
