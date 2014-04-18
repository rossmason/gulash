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
import org.mule.util.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.Version;


public class Mule
{


    public static final String DEFAULT_GROUP_ID = "org.mule.modules";
    public static final String DEFAULT_MODULE_PREFIX = "mule-module-";
    public static final String DEFAULT_CLASSIFIER = "plugin";
    public static final String DEFAULT_EXTENSION = "zip";
    private List<Builder<?>> builders = new ArrayList<Builder<?>>();
    private MuleContext muleContext;
    private List<StartListener> startListeners = new ArrayList<StartListener>();
    private File muleHome;
    private ModuleClassLoader muleClassLoader;
    private ModuleResolver moduleResolver;

    public Mule(File muleHome)
    {
        this.muleHome = muleHome;
        this.moduleResolver = new ModuleResolver();
        setup();
    }

    public Mule()
    {
        this(new File("."));
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
        final File lib = getLibDirectory();

        if (lib.exists())
        {
            File[] files = lib.listFiles();
            for (File module : files)
            {
                installModule(module);
            }

        }

    }

    public void require(String module)
    {
        //Todo we need to add version on the classloader
        if (!muleClassLoader.isModuleInstalled(module))
        {
            try
            {
                final Version highestVersion = this.moduleResolver.getHighestVersion(DEFAULT_GROUP_ID, DEFAULT_MODULE_PREFIX + module, DEFAULT_CLASSIFIER, DEFAULT_EXTENSION);
                require(module, highestVersion.toString());
            }
            catch (VersionRangeResolutionException e)
            {

            }
        }
    }

    public void require(String module, String version)
    {
        try
        {
            final File moduleDirectory = new File(getLibDirectory(), module);
            moduleResolver.installModule(DEFAULT_GROUP_ID, DEFAULT_MODULE_PREFIX + module, DEFAULT_CLASSIFIER, DEFAULT_EXTENSION, version, moduleDirectory);
            installModule(moduleDirectory);
        }
        catch (ArtifactResolutionException e)
        {

        }
    }

    private void installModule(File module)
    {
        final List<URL> moduleJars = new ArrayList<URL>();
        final String name = module.getName();
        if (module.isDirectory())
        {
            File moduleLib = new File(module, "lib");
            if (moduleLib.exists() && moduleLib.isDirectory())
            {
                Collection<File> jars = FileUtils.listFiles(moduleLib, new String[] {"jar"}, true);
                for (File file : jars)
                {
                    try
                    {
                        moduleJars.add(file.toURI().toURL());
                    }
                    catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            File moduleClasses = new File(module, "classes");
            if (moduleClasses.exists() && moduleClasses.isDirectory())
            {
                try
                {
                    moduleJars.add(moduleClasses.toURI().toURL());
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        muleClassLoader.installModule(name, new URLClassLoader(moduleJars.toArray(new URL[moduleJars.size()])));
    }

    private File getLibDirectory()
    {
        return new File(getMuleHome(), "lib");
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
        builders.clear();
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
        muleContext.stop();
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
