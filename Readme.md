GUle
==========
![Gule](https://github.com/machaval/mule-light/blob/master/logo.png)
Groovy + Mule    = GUle 
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


Features
--------

* Supports some mule core element
 * Foreach/Choice/Flow/Exception Strategy
 * Logger/Set Payload
 * Poll
* Supports APIKit
 * Basic Scaffolding from raml file.
* Devkit dsl generation from @Module and @Connectors


Building & Installation
--------

Just use maven

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
mvn clean install
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

And a distro will be generated under laucher/target/mule-me.zip

Just unzip it and you have it installed

Running a script
-----------

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
mule-me/bin/mule.sh example.groovy
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
usage: mule <MuleFile>
 -c <raml file>   Create an escafolder based on .
 -help            Print this message
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
