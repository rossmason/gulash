package org.mule.module.raspberrypi;


import static org.mule.module.Core.flow;
import static org.mule.module.Core.log;
import static org.mule.module.RaspberryPi.exec;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.module.core.Mule;

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
                        .then(exec("ls"),
                              log("#[payload]"))
        );

        mule.start();

        MuleEvent muleEvent = mule.callFlow("testFlow", "hello");
        Object payload = muleEvent.getMessage().getPayload();
        Assert.assertThat(payload, CoreMatchers.instanceOf(String.class));
        Assert.assertThat(((String) payload).isEmpty(), CoreMatchers.is(false));
    }




}
