package org.mule.me.launcher;


import org.mule.util.FileUtils;

import java.io.File;
import java.io.IOException;

import org.raml.model.ActionType;

public class GroovyScaffolding
{

    private StringBuilder result = new StringBuilder();

    public GroovyScaffolding init()
    {
        result.append("mule.declare(");
        return this;
    }

    public GroovyScaffolding api(String name)
    {
        result.append("\n\tapi(\"").append(name).append("\")");
        return this;
    }


    public GroovyScaffolding declareResourceAction(String description, String resourceEntryKey, ActionType actionType)
    {
        result.append("\n\t\t.on(\"" + resourceEntryKey + "\"," + actionType.name().toLowerCase() + ").then(log(\"" + description + "\"))");
        return this;
    }

    public GroovyScaffolding end()
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
