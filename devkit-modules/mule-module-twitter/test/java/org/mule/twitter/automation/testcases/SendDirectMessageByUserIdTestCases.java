/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import twitter4j.DirectMessage;

public class SendDirectMessageByUserIdTestCases extends TwitterTestParent {
    	
    @Category({RegressionTests.class})
	@Test
	public void testSendDirectMessageByUserId() {
    	initializeTestRunMessage("sendDirectMessageByUserIdTestData");	
		try {
			DirectMessage directMessage = runFlowAndGetPayload("send-direct-message-by-user-id");
			
			assertEquals(getTestRunMessageValue("message").toString(), directMessage.getText());
			assertEquals(getTestRunMessageValue("senderSandboxScreenName").toString(), directMessage.getSenderId());
			assertEquals(getTestRunMessageValue("recipientSandboxScreenName").toString(), directMessage.getRecipientId());
	      
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
        
	} 
	
}
