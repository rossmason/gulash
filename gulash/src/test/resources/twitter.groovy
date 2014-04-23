require "Twitter" version "3.1.0-SNAPSHOT"
require "Webcam" version "1.0-SNAPSHOT"


String consumerKey = "KWAiPYoPy60GYJSKYm5PuQ";
String consumerSecret = "Ag5VKkwbnoOiHBQ2Rh5J1sQdAh35YbeYVimqqMYp2E";
String accessKey = "2377668061-bgaWKawaIacFA2JNkXCA0jEyanUr3Vuzn7GRpbi";
String accessSecret = "prK4R3515VHu9PpD1hZyBoTz152H2tPs7k2jJNtXnvKyE";


declare Twitter.config(consumerKey, consumerSecret)
                .accessKey(accessKey)
                .accessSecret(accessSecret).as("twitterConfig");

declare Webcam.config().as("webcamConfig")


declare flow("main")
                 .on(endpoint("http://localhost:8081"))
                 .then(enrich("#[flowVars['picture']]").with(Webcam.takePicture().using("webcamConfig")))
                 .then(Twitter.updateStatus("My picture is!").mediaName("Me.png").media("#[flowVars['picture']]").using("twitterConfig"))



