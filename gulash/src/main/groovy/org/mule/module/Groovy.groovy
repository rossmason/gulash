package org.mule.module;


import org.mule.module.groovy.builder.GroovyBuilder

/**
 * Created by machaval on 4/6/14.
 */
public class Groovy
{

    public static GroovyBuilder process(Closure closure)
    {
        return new GroovyBuilder(closure);
    }

}
