package org.mule.gulash;

import org.mule.module.core.Mule;

import java.io.File;
import java.io.IOException;

import groovy.lang.Binding;
import org.codehaus.groovy.tools.shell.IO;
import org.mule.gulash.Gulash;


public class InteractiveGroovyRunner extends AbstractGroovyRunner
{


    public void run(File muleHome) throws IOException
    {
        final Binding binding = new Binding();
        final Mule mule = new Mule(muleHome);
        binding.setVariable("mule", mule);
        final Gulash interactiveShell = new Gulash(createGroovyShell(mule), new IO());
        interactiveShell.run(new String[0]);
    }


    public static void main(String[] args)
    {
        try
        {
            new InteractiveGroovyRunner().run(new File("."));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
