package org.mule.module.builder;

import static org.mule.module.Core.*;

import org.mule.api.MuleException;
import org.mule.module.Core;
import org.mule.module.core.Mule;


import org.junit.Test;

/**
 * Created by machaval on 2/20/14.
 */
public class CoreTest
{

    @Test
    public void testChoice() throws MuleException
    {

        Mule mule = new Mule();
        mule.declare(
                flow("Test").then(choice().on("#[payload.name == 'mariano']").then(log("mariano"))
                                          .on("#[payload.name == 'martin']").then(log("Martin"))
                                          .otherwise().then(log("otherwise"))
                )
        );
        mule.start();




        Mule mule2 = new Mule();
        mule2.declare(Core.flow("shoki")
                                .on(Core.endpoint("http://localhost:8081"))
                                    .then(Core.log("Shoki sos un genio"))
                                    .then(Core.log("Shoki sos un genio"))
        );


    }

}
