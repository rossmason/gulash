package org.mule.module.raspberrypi;


import static org.mule.dsl.builder.core.Core.flow;
import static org.mule.dsl.builder.core.Core.logger;
import static org.mule.module.raspberrypi.builder.RaspberryPi.exec;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.dsl.builder.core.Mule;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest
{

    @Test
    public void testCommandLine() throws MuleException
    {
        Mule mule = new Mule();
        mule.declare(
                flow("testFlow")
                        .chain(exec("ls"))
                        .chain(logger().usingMessage("#[payload]"))
        );

        mule.start();

        MuleEvent muleEvent = mule.callFlow("testFlow", "hello");
        Object payload = muleEvent.getMessage().getPayload();
        Assert.assertThat(payload, CoreMatchers.instanceOf(String.class));
        Assert.assertThat(((String) payload).isEmpty(), CoreMatchers.is(false));


    }

}
