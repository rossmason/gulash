package org.mule.module.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleClassLoader extends ClassLoader
{

    private Map<String, ClassLoader> modules;


    public ModuleClassLoader(ClassLoader parent)
    {
        super(parent);
        this.modules = new HashMap<String, ClassLoader>();
    }


    @Override
    public URL getResource(String name)
    {
        URL resource = super.getResource(name);
        if (resource == null)
        {
            for (ClassLoader loader : modules.values())
            {
                if (loader != null)
                {
                    resource = loader.getResource(name);
                    if (resource != null)
                    {
                        return resource;
                    }
                }
            }
        }
        return resource;
    }

    @Override
    public InputStream getResourceAsStream(String name)
    {
        InputStream is = super.getResourceAsStream(name);
        if (is == null)
        {
            for (ClassLoader loader : modules.values())
            {
                if (loader != null)
                {
                    is = loader.getResourceAsStream(name);
                    if (is != null)
                    {
                        return is;
                    }
                }
            }
        }
        return is;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException
    {
        final List<URL> urls = new ArrayList<URL>();
        Enumeration<URL> resources = super.getResources(name);
        addAll(urls, resources);
        for (ClassLoader loader : modules.values())
        {
            if (loader != null)
            {
                try
                {
                    resources = loader.getResources(name);
                    addAll(urls, resources);
                }
                catch (IOException ioe)
                {
                    // Ignoring
                }
            }
        }
        return Collections.enumeration(urls);
    }

    private void addAll(List<URL> urls, Enumeration<URL> resources)
    {
        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            if (resource != null && !urls.contains(resource))
            {
                urls.add(resource);
            }
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        try
        {
            //call directly the parent to avoid stack overflow
            return getParent().loadClass(name);
        }
        catch (ClassNotFoundException cnfe)
        {
            // Ignoring
        }
        for (ClassLoader loader : modules.values())
        {
            if (loader != null)
            {
                try
                {
                    return loader.loadClass(name);
                }
                catch (ClassNotFoundException cnfe)
                {
                    // Ignoring
                }
            }
        }
        throw new ClassNotFoundException(name);
    }

    public void installModule(String name, ClassLoader classLoader)
    {
        modules.put(name, classLoader);
    }

    public void removeModule(String name)
    {
        modules.remove(name);
    }

    public void removeAll()
    {
        modules.clear();
    }

    public Set<String> getInstalledModules(){
        return modules.keySet();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        // loader.loadClass(name, resolve) is not visible!
        return loadClass(name);
    }


    public boolean isModuleInstalled(String module)
    {
        return modules.containsKey(module);
    }
}