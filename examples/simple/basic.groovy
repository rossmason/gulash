declare(
        flow("Simple")
                .on(endpoint("http://localhost:8081"))
                .then(log("Welcome to Mule"))
        );
