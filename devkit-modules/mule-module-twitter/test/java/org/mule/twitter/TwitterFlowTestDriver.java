/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter;

import org.mule.api.MuleMessage;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.tck.FunctionalTestCase;

public class TwitterFlowTestDriver extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "twitter-flow.xml";
    }

    public void testAuthenticationFlow() throws Exception {
        DefaultLocalMuleClient client = new DefaultLocalMuleClient(muleContext);
        
        MuleMessage response = client.request("http://localhost:9002/public", 10000);
        String responseString = response.getPayloadAsString();
        
        System.out.println(responseString);
    }
}
