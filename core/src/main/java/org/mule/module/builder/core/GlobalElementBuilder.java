package org.mule.module.builder.core;


import org.mule.config.dsl.Builder;

public interface GlobalElementBuilder<T> extends Builder<T>
{

    GlobalElementBuilder<T> as(String name);

}
