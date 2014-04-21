package org.mule.dependency;


public class ModuleBuilder
{

    private String name;
    private String version;
    private String url;


    public ModuleBuilder(String name)
    {
        this.name = name;
    }

    public ModuleBuilder version(String version)
    {
        this.version = version;
        return this;
    }

    public ModuleBuilder from(String url)
    {
        this.url = url;
        return this;
    }


    public Module create()
    {
        return new Module(name, version, url);
    }
}
