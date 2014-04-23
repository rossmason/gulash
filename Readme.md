Gulash
==========

The goal of this project is to create a small java/groovy dsl on top of mule
esb. And that it should be very easy to contribute and extend.

Java Example

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Mule mule = new Mule();
mule.declare( 
    flow("Test")
            .then(choice()
                        .on("#[payload.name == 'mariano']").then(log("mariano"))
                        .on("#[payload.name == 'martin']").then(log("Martin"))
                        .otherwise().then(log("otherwise"))
                ) 
); 
mule.start();
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Groovy Example

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mule.declare(
                api("rover.raml")
                        .on("/motion/forward" , ActionType.PUT).then(log("#[payload]"))
                        .on("/motion/backard" , ActionType.PUT).then(log("#[payload]"))
                        .on("/motion/left" , ActionType.PUT).then(log("#[payload]"))
                        .on("/motion/right" , ActionType.PUT).then(log("#[payload]"))

        );
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Using a Cloud Connector

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
require "Twitter"


String consumerKey = "";
String consumerSecret = "";
String accessKey = "";
String accessSecret = "";


declare Twitter.config(consumerKey, consumerSecret)
                .accessKey(accessKey)
                .accessSecret(accessSecret).as("twitterConfig");


declare flow("main")
                .on(endpoint("http://localhost:8081"))
                .then(Twitter.updateStatus("#[payload]").using("twitterConfig"))
                .then(setPayload("Your twitter status updated!"))





~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Features
--------

* Supports some mule core element
 * Foreach/Choice/Flow/Exception Strategy
 * Logger/Set Payload
 * Poll
* Supports APIKit
 * Basic Scaffolding from raml file.
* Devkit dsl generation from @Module and @Connectors
* Dependency support through require expression


Building & Installation
--------

Just use maven

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
mvn clean install
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

And a distribution will be generated under goulash/target/goulash.zip

Just unzip it and you have it installed

Running a script
-----------

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
goulash/bin/goulash.sh example.groovy
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
usage: gulash.sh <File>
 -c <RAML file>                           Create a script based on the
                                          specified RAML file.
 -get <Dependency Module Name.>           Downloads the required
                                          dependency so it is available at
                                          runtime.
 -help                                    Print this message
 -i                                       Starts an interactive console
                                          for fast testing.
 -list <Dependency Module Version.>       The version of the dependency.
                                          If not specified use the latest
                                          one.
 -version <Dependency Module  Version.>   The version of the dependency.
                                          If not specified use the latest
                                          one.
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
