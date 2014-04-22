declare(
        flow("Hello")
                .then(log("#[payload]"))
                .then(process(
                {
                    message ->
                        println("function called");
                        println(message.payload)
                        return "OK"
                }
        )
        )
);

onStart({

    def result = callFlow("Hello", "Hello");
    def payload = result.getMessage().getPayload()
    println "payload = $payload"
})
