package org.mule.me.launcher;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.module.Apikit;
import org.mule.module.Core;
import org.mule.module.builder.core.Mule;
import org.mule.module.builder.core.PropertiesBuilder;
import org.mule.module.builder.core.StartListener;

import java.io.File;

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

        Mule mule = new Mule(muleHome);
        binding.setVariable("mule", mule);
        final GroovyShell shell = new GroovyShell(MuleLauncher.class.getClassLoader(), binding, compilerConfiguration);
        shell.evaluate(groovyFile);
        mule.start();
    }

}
