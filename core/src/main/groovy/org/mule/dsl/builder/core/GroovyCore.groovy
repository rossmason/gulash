package org.mule.dsl.builder.core



class GroovyCore {

    public static groovy(closure){
        return new GroovyBuilder(closure);
    }

}
