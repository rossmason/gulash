package org.mule.module.raspberrypi;

import org.mule.api.MuleException;
import org.mule.module.Core;
import org.mule.module.core.Mule;
import org.mule.module.RaspberryPi;
import org.mule.module.raspberrypi.rover.Direction;
import org.mule.module.raspberrypi.rover.MockRover;
import org.mule.module.raspberrypi.rover.Rover;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;


public class RoverTest
{
    @Test
    public void testRover() throws MuleException
    {
        Mule mule = new Mule();
        mule.declare(Core.bean(MockRover.class).as("TestRover"));
        mule.declare(Core.flow("test").then(RaspberryPi.rover().backward()));
        mule.start();
        mule.callFlow("test","test");
        Assert.assertThat(mule.lookup(Rover.class).getCurrentDirection(), CoreMatchers.is(Direction.BACKWARDS));
    }
}
