import static org.mule.module.Twitter.*;

String consumerKey = "";
String consumerSecret = "";
String accessKey = "";
String accessSecret = "";


declare twitterConfig(consumerKey, consumerSecret)
                     .accessKey(accessKey)
                     .accessSecret(accessSecret).as("twitterConfig");

declare flow "TestTwitter"
                     on(endpoint("http://localhost:8081"))
                     then(updateStatus("#['Hello From Mule Light']").with("twitterConfig"))
                     then(setPayload("OK"));