package org.mule.me.launcher;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.module.Apikit;
import org.mule.module.Core;
import org.mule.module.core.Mule;
import org.mule.module.core.StartListener;
import org.mule.module.core.TimePeriod;
import org.mule.module.core.builder.PropertiesBuilder;
import org.mule.util.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
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


public class AbstractGroovyRunner
{

    protected GroovyShell createGroovyShell(Mule mule) throws MalformedURLException
    {
        final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        final ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addStaticStars(Core.class.getName(), Apikit.class.getName(), PropertiesBuilder.class.getName(), TimePeriod.class.getName());
        importCustomizer.addImports(StartListener.class.getName());
        importCustomizer.addImports(MuleEvent.class.getName());
        importCustomizer.addImports(MuleMessage.class.getName());
        importCustomizer.addImports(ActionType.class.getName());
        compilerConfiguration.addCompilationCustomizers(importCustomizer);
        final Binding binding = createBinding(mule);
        ClassLoader executionClassLoader = createClassLoader(mule);
        return new GroovyShell(executionClassLoader, binding, compilerConfiguration);
    }

    protected ClassLoader createClassLoader(Mule mule) throws MalformedURLException
    {
        ClassLoader executionClassLoader = MuleLauncher.class.getClassLoader();
        final File lib = new File(mule.getMuleHome(), "lib");
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
        return executionClassLoader;
    }

    protected Binding createBinding(Mule mule)
    {
        final Binding binding = new Binding();
        binding.setVariable("mule", mule);
        return binding;
    }
}
