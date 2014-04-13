package org.mule.gulash;


import org.mule.util.FileUtils;

import java.io.File;
import java.io.IOException;

import org.raml.model.ActionType;

public class Scaffolding
{

    private StringBuilder result = new StringBuilder();

    public Scaffolding init()
    {
        result.append("mule.declare(");
        return this;
    }

    public Scaffolding api(String name)
    {
        result.append("\n\tapi(\"").append(name).append("\")");
        return this;
    }


    public Scaffolding declareResourceAction(String description, String resourceEntryKey, ActionType actionType)
    {
        result.append("\n\t\t.on(\"" + resourceEntryKey + "\"," + actionType.name().toLowerCase() + ").then(log(\"" + description + "\"))");
        return this;
    }

    public Scaffolding end()
    {
        result.append("\n);");
        return this;
    }

    public void generate(File groovyFile)
    {
        try
        {
            FileUtils.writeStringToFile(groovyFile, result.toString(), "UTF-8");
        }
        catch (IOException e)
        {

        }
    }
}
