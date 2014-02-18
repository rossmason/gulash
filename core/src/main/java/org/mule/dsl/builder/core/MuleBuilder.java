package org.mule.dsl.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;

/**
 * Builder
 */
public interface MuleBuilder<T> {

    /**
     * Builds the parameterized type based on builder internal state and the given parameters.
     *
     * @param muleContext the mule context
     * @return an instance of parameterized type
     * @throws ConfigurationException if any problem building the parameterized type
     * @throws IllegalStateException  if the actual builder state is invalid
     */
    T build(MuleContext muleContext) throws  ConfigurationException, IllegalStateException;
}