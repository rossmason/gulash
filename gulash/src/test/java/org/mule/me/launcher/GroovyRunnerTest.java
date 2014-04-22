package org.mule.me.launcher;


import org.mule.gulash.GroovyRunner;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Ignore;
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

        new GroovyRunner().run(new File(resource.toURI()), new String[0], new File("."));
    }

    @Test
    @Ignore
    public void twitterTest() throws Exception
    {
        URL resource = getClass().getClassLoader().getResource("twitter.groovy");

        new GroovyRunner().run(new File(resource.toURI()), new String[]{"Probando 1 2 3 4"}, new File("."));
    }
}
