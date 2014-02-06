package org.mule.dsl.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by machaval on 2/5/14.
 */
public class Properties
{

    public static Map<String, Object> properties(Object... properties)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (properties.length % 2 != 0)
        {
            throw new IllegalArgumentException("Properties should be key value pair");
        }
        for (int i = 0; i < properties.length; i = i + 2)
        {
            Object key = properties[i];
            Object value = properties[i + 1];
            if (!(key instanceof String))
            {
                throw new IllegalArgumentException(key + "Should be key String");
            }
            result.put((String) key, value);
        }

        return result;

    }


}
