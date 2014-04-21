package org.mule.dependency;

/**
 * Created by machaval on 4/21/14.
 */
public class Module
{

    private String name;
    private String version;
    private String url;

    public Module(String name, String version, String url)
    {
        this.name = name;
        this.version = version;
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public String getUrl()
    {
        return url;
    }

    @Override
    public String toString()
    {
        return "Module{" +
               "name='" + name + '\'' +
               ", version='" + version + '\'' +
               ", url='" + url + '\'' +
               '}';
    }
}
