package org.mule.gulash;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.client.MuleClient;
import org.mule.api.registry.RegistrationException;
import org.mule.config.dsl.Builder;
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
        return getMule().down();
    }

    public <T> T lookup(Class<T> clazz) throws RegistrationException
    {
        return getMule().lookup(clazz);
    }

    public Mule getMule()
    {
        return (Mule) getBinding().getVariable("mule");
    }


}
