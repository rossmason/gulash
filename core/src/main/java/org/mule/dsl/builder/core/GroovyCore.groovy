package org.mule.dsl.builder.core


/**
 * Created with IntelliJ IDEA.
 * User: ivan
 * Date: 2/13/14
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
class GroovyCore {

    public static groovy(closure){
        return new GroovyBuilder(closure);
    }

}
