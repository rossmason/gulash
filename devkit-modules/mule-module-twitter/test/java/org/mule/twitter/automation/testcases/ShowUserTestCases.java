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

import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import twitter4j.User;



public class ShowUserTestCases extends TwitterTestParent {
     
    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testShowUser() {
    	
    	Map<String,Object> sandbox = getBeanFromContext("sandbox");
    	
		try {
	        User user = runFlowAndGetPayload("show-user");
	        
	        assertEquals(sandbox.get("userId").toString(), user.getId()); 
	        assertEquals( sandbox.get("userName").toString(), user.getName()); 
	        assertEquals( sandbox.get("userScreenName").toString(), user.getScreenName()); 

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}