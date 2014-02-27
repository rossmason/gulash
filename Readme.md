Mule Light
==========

The goal of this project is to create a small java/groovy dsl on top of mule
esb.

Example

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 Mule mule = new Mule(); 
mule.declare( 
    flow("Test")
            .then(choice()
                        .on("#[payload.name == 'mariano']").then(log("mariano"))
                        .on("#[payload.name == 'martin']").then(log("Martin"))     .otherwise().then(log("otherwise")) 
                ) 
); 
mule.start();
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




