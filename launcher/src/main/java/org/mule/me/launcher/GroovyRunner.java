package org.mule.me.launcher;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.module.Apikit;
import org.mule.module.Core;
import org.mule.module.core.Mule;
import org.mule.module.core.builder.PropertiesBuilder;
import org.mule.module.core.StartListener;
import org.mule.util.FileUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.raml.model.ActionType;


public class GroovyRunner
{

    public void run(File groovyFile, File muleHome) throws Exception
    {
        final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        final ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addStaticStars(Core.class.getName(), Apikit.class.getName(), PropertiesBuilder.class.getName());
        importCustomizer.addImports(StartListener.class.getName());
        importCustomizer.addImports(MuleEvent.class.getName());
        importCustomizer.addImports(MuleMessage.class.getName());
        importCustomizer.addImports(ActionType.class.getName());
        compilerConfiguration.addCompilationCustomizers(importCustomizer);
        final Binding binding = new Binding();
        ClassLoader executionClassLoader = MuleLauncher.class.getClassLoader();
        final File lib = new File(muleHome, "lib");
        if (lib.exists())
        {
            List<URL> jarsUrl = new ArrayList<URL>();
            Collection<File> jars = FileUtils.listFiles(lib, new String[] {"jar"}, true);
            for (File file : jars)
            {
                jarsUrl.add(file.toURI().toURL());
            }
            if (!jarsUrl.isEmpty())
            {
                executionClassLoader = new URLClassLoader(jarsUrl.toArray(new URL[jarsUrl.size()]), executionClassLoader);
            }
        }

        final Mule mule = new Mule(muleHome);
        binding.setVariable("mule", mule);

        final GroovyShell shell = new GroovyShell(executionClassLoader, binding, compilerConfiguration);
        shell.evaluate(groovyFile);
        mule.start();
    }

}
