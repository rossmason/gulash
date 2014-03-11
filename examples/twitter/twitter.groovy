import static org.mule.module.Twitter.*;

String consumerKey = "";
String consumerSecret = "";
String accessKey = "";
String accessSecret = "";


mule.declare(twitterConfig(consumerKey, consumerSecret)
                     .accessKey(accessKey)
                     .accessSecret(accessSecret).as("twitter"));

mule.declare(flow("TestTwitter")
                     .on(endpoint("http://localhost:8081"))
                     .then(updateStatus("#['Hello From Mule Light']").with("twitter"))
                     .then(setPayload("OK")));