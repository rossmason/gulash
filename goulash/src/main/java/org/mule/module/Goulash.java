package org.mule.module;

import org.mule.me.goulash.builder.GroovyBuilder;

import groovy.lang.Closure;

/**
 * Created by machaval on 4/6/14.
 */
public class Goulash
{

    public static GroovyBuilder call(Closure closure)
    {
        return new GroovyBuilder(closure);
    }

}
