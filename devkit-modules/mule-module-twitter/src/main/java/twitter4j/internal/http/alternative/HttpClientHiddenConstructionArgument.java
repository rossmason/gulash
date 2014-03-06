/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package twitter4j.internal.http.alternative;

public class HttpClientHiddenConstructionArgument
{
    private static final ThreadLocal<Boolean> useMule = new ThreadLocal<Boolean>();

    public static void setUseMule(boolean b)
    {
        useMule.set(b);
    }
    
    public static boolean useMule()
    {
        return useMule.get();
    }
}
