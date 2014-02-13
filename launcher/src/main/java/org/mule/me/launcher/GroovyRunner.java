package org.mule.me.launcher;

import org.mule.dsl.builder.apikit.Apikit;
import org.mule.dsl.builder.core.Core;
import org.mule.dsl.builder.core.Mule;
import org.mule.dsl.builder.core.Properties;
import org.mule.dsl.builder.core.StartListener;

import java.io.File;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;


public class GroovyRunner
{

    public void run(File groovyFile) throws Exception
    {
        final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        final ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addStaticStars(Core.class.getName(), Apikit.class.getName(), Properties.class.getName());
        importCustomizer.addImports(StartListener.class.getName());
        compilerConfiguration.addCompilationCustomizers(importCustomizer);
        final Binding binding = new Binding();

        Mule mule = new Mule();
        binding.setVariable("mule", mule);
        final GroovyShell shell = new GroovyShell(MuleLauncher.class.getClassLoader(), binding, compilerConfiguration);
        shell.evaluate(groovyFile);
        mule.start();
    }

}
