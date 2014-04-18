package org.mule.gulash;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.module.Apikit;
import org.mule.module.Core;
import org.mule.module.Groovy;
import org.mule.module.core.Mule;
import org.mule.module.core.StartListener;
import org.mule.module.core.TimePeriod;
import org.mule.module.core.builder.PropertiesBuilder;

import java.net.MalformedURLException;

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
        compilerConfiguration.setScriptBaseClass(MuleBaseScript.class.getName());
        final ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addStaticStars(Core.class.getName(), Groovy.class.getName(), Apikit.class.getName(), PropertiesBuilder.class.getName(), TimePeriod.class.getName());
        importCustomizer.addImports(StartListener.class.getName());
        importCustomizer.addImports(MuleEvent.class.getName());
        importCustomizer.addImports(MuleMessage.class.getName());
        importCustomizer.addImports(ActionType.class.getName());
        compilerConfiguration.addCompilationCustomizers(importCustomizer);
        final Binding binding = createBinding(mule);
        ClassLoader executionClassLoader = mule.getMuleClassLoader();
        return new GroovyShell(executionClassLoader, binding, compilerConfiguration);
    }


    protected Binding createBinding(Mule mule)
    {
        final Binding binding = new Binding();
        binding.setVariable("mule", mule);
        return binding;
    }
}
