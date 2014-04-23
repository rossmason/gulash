package org.mule.module.core;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.client.MuleClient;
import org.mule.api.config.ConfigurationException;
import org.mule.api.config.MuleProperties;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.registry.RegistrationException;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.config.DefaultMuleConfiguration;
import org.mule.config.builders.DefaultsConfigurationBuilder;
import org.mule.config.dsl.Builder;
import org.mule.construct.Flow;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.dependency.DependencyManager;
import org.mule.dependency.MavenDependencyManager;
import org.mule.dependency.Module;
import org.mule.dependency.ModuleBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Mule
{

    private List<Builder<?>> builders = new ArrayList<Builder<?>>();
    private MuleContext muleContext;
    private List<StartListener> startListeners = new ArrayList<StartListener>();
    private File muleHome;
    private ModuleClassLoader muleClassLoader;
    private List<ModuleBuilder> moduleBuilders = new ArrayList<ModuleBuilder>();
    private DependencyManager resolver = new MavenDependencyManager();

    public Mule(File muleHome)
    {
        this.muleHome = muleHome;
        setup();
    }

    public Mule()
    {
        this(new File("."));
    }

    public ModuleBuilder require(String module)
    {
        final ModuleBuilder result = new ModuleBuilder(module);
        moduleBuilders.add(result);
        return result;
    }

    public Mule declare(Builder<?> builder)
    {
        builders.add(builder);
        return this;
    }


    public ModuleClassLoader getMuleClassLoader()
    {
        return muleClassLoader;
    }

    private void setup()
    {
        ClassLoader executionClassLoader = Thread.currentThread().getContextClassLoader();
        muleClassLoader = new ModuleClassLoader(executionClassLoader);
    }


    public File getLibDirectory()
    {
        return new File(getMuleHome(), ".lib");
    }

    public Mule start() throws MuleException
    {

        initMuleContext();
        build();

        muleContext.start();
        for (StartListener startListener : startListeners)
        {
            startListener.onStart(this);
        }
        return this;
    }

    public void build()
    {
        for (Builder<?> builder : builders)
        {
            builder.create(muleContext);
        }

        for (ModuleBuilder moduleBuilder : moduleBuilders)
        {
            final Module module = moduleBuilder.create();
            final String name = module.getName();
            getMuleClassLoader().installModule(name, resolver.installModule(module, this));
        }

        builders.clear();
        moduleBuilders.clear();
    }

    private void initMuleContext() throws InitialisationException, ConfigurationException
    {
        if (muleContext == null)
        {
            final Properties properties = new Properties();
            properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, muleHome.getAbsolutePath());
            muleContext = new DefaultMuleContextFactory().createMuleContext(new DefaultsConfigurationBuilder(), properties, new DefaultMuleConfiguration());
        }
    }


    public MuleContext getMuleContext()
    {
        return muleContext;
    }

    public Mule stop() throws MuleException
    {
        if (muleContext != null) {
            muleContext.stop();
        }
        return this;
    }

    public File getMuleHome()
    {
        return muleHome;
    }

    public Mule onStart(StartListener listener)
    {
        startListeners.add(listener);
        return this;
    }

    public MuleClient client()
    {
        return new DefaultLocalMuleClient(muleContext);
    }

    public MuleEvent callFlow(String name, Object payload) throws MuleException
    {
        Object targetObject = muleContext.getRegistry().lookupObject(name);
        if (targetObject instanceof Flow)
        {
            Flow flow = (Flow) targetObject;
            DefaultMuleMessage defaultMuleMessage = new DefaultMuleMessage(payload, muleContext);
            DefaultMuleEvent defaultMuleEvent = new DefaultMuleEvent(defaultMuleMessage, MessageExchangePattern.REQUEST_RESPONSE, flow);
            return flow.process(defaultMuleEvent);
        }
        throw new DefaultMuleException("Target flow was not found " + name);
    }

    public <T> T lookup(Class<T> clazz) throws RegistrationException
    {
        return muleContext.getRegistry().lookupObject(clazz);
    }

    public <T> T get(String key)
    {
        return muleContext.getRegistry().get(key);
    }
}
