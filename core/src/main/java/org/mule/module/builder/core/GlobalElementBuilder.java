package org.mule.module.builder.core;


public interface GlobalElementBuilder<T> extends MuleBuilder<T>
{

    GlobalElementBuilder<T> as(String name);

}
