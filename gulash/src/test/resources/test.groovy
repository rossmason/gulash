import org.mule.module.core.StartListener

declare
        flow "Hello"
                 then log "#[payload]"
                 then call {
                                  message ->
                                      println("function called");
                                      println(message.payload)
                                      return "Hello From here"
                            }




onStart
        { mule ->
            def result = mule.callFlow("Hello", "Hello");
            def payload = result.getMessage().getPayload()
            println "payload = $payload"
        } as StartListener
