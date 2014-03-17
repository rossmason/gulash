package org.mule.me.launcher;

import org.mule.module.core.Mule;

import java.io.File;
import java.io.IOException;

import groovy.lang.Binding;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;


public class InteractiveGroovyRunner extends AbstractGroovyRunner
{


    public void run(File muleHome) throws IOException
    {
        final Binding binding = new Binding();
        final Mule mule = new Mule(muleHome);
        binding.setVariable("mule", mule);
        final Groovysh interactiveShell = new Groovysh(createClassLoader(mule), createBinding(mule), new IO());
        interactiveShell.run();
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
