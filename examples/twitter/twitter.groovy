require "Twitter"


String consumerKey = "KWAiPYoPy60GYJSKYm5PuQ";
String consumerSecret = "Ag5VKkwbnoOiHBQ2Rh5J1sQdAh35YbeYVimqqMYp2E";
String accessKey = "2377668061-bgaWKawaIacFA2JNkXCA0jEyanUr3Vuzn7GRpbi";
String accessSecret = "prK4R3515VHu9PpD1hZyBoTz152H2tPs7k2jJNtXnvKyE";


declare
    Twitter.twitterConfig(consumerKey, consumerSecret)
                     .accessKey(accessKey)
                     .accessSecret(accessSecret).as("twitterConfig");

declare
    flow "TestTwitter"
        on endpoint "http://localhost:8081"
            then Twitter.updateStatus "#['Hello From Mule Light']" using "twitterConfig"
            then setPayload "OK"