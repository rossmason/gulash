package org.mule.module;


import org.mule.module.groovy.builder.GroovyBuilder

/**
 * Created by machaval on 4/6/14.
 */
public class Groovy
{

    public static GroovyBuilder call(Closure closure)
    {
        return new GroovyBuilder(closure);
    }

}
