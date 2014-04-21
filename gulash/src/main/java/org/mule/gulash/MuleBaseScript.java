package org.mule.gulash;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.client.MuleClient;
import org.mule.api.config.ConfigurationException;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.registry.RegistrationException;
import org.mule.common.Result;
import org.mule.common.TestResult;
import org.mule.common.Testable;
import org.mule.config.dsl.Builder;
import org.mule.dependency.ModuleBuilder;
import org.mule.module.core.Mule;
import org.mule.module.core.StartListener;

import java.io.File;

import groovy.lang.Script;


/**
 * Created by machaval on 4/13/14.
 */
public abstract class MuleBaseScript extends Script
{


    public Mule declare(Builder<?> builder)
    {
        return getMule().declare(builder);
    }

    public MuleClient client()
    {
        return getMule().client();
    }

    public MuleEvent callFlow(String name, Object payload) throws MuleException
    {
        return getMule().callFlow(name, payload);
    }

    public File getMuleHome()
    {
        return getMule().getMuleHome();
    }

    public Mule onStart(StartListener listener)
    {
        return getMule().onStart(listener);
    }

    public Mule start() throws MuleException
    {
        return getMule().start();
    }

    public Mule down() throws MuleException
    {
        return getMule().stop();
    }

    public <T> T lookup(Class<T> clazz) throws RegistrationException
    {
        return getMule().lookup(clazz);
    }

    public ModuleBuilder require(String moduleName)
    {
        return getMule().require(moduleName);
    }

    public Mule getMule()
    {
        return (Mule) getBinding().getVariable("mule");
    }


    public void test(String configName)
    {
        try
        {
            getMule().build();
            final Object config = getMule().get(configName);
            if (config instanceof Testable)
            {
                getMule().getMuleContext().getRegistry().applyLifecycle(config, Initialisable.PHASE_NAME);
                try
                {
                    final TestResult test = ((Testable) config).test();
                    if (test.getStatus() == Result.Status.SUCCESS)
                    {
                        System.out.println("The configuration '" + configName + "' was successfully configured.");
                    }
                    else
                    {
                        System.out.println("The configuration '" + configName + "' has an error : \n" + test.getMessage() + ".");
                    }
                }
                finally
                {
                    getMule().getMuleContext().getRegistry().applyLifecycle(config, Stoppable.PHASE_NAME);
                }

            }

        }
        catch (InitialisationException e)
        {
            System.out.println("Error while initializing configuration " + e.getMessage());
        }
        catch (ConfigurationException e)
        {
            System.out.println("Error while initializing configuration " + e.getMessage());
        }
        catch (MuleException e)
        {
            System.out.println("Error while initializing configuration " + e.getMessage());
        }
        System.out.println("Configuration " + configName + " is not testable.");
    }

}
