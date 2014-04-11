package org.mule.me.goulash;

import org.mule.module.core.Mule;

import java.io.File;

import groovy.lang.GroovyShell;


public class GroovyRunner extends AbstractGroovyRunner
{

    public void run(File groovyFile, File muleHome) throws Exception
    {
        long start = System.currentTimeMillis();
        final Mule mule = new Mule(muleHome);
        final GroovyShell shell = createGroovyShell(mule);
        shell.evaluate(groovyFile);
        mule.start();
        System.out.println("Starting time was " + (System.currentTimeMillis() - start) + "ms");
    }

}
