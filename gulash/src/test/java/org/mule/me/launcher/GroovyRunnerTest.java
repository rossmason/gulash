package org.mule.me.launcher;

import org.mule.me.goulash.GroovyRunner;

import java.io.File;
import java.net.URL;

import org.junit.Test;

/**
 * Created by machaval on 2/11/14.
 */
public class GroovyRunnerTest
{

    @Test
    public void simpleTest() throws Exception
    {
        URL resource = getClass().getClassLoader().getResource("test.groovy");

        new GroovyRunner().run(new File(resource.toURI()),new File("."));
    }
}
