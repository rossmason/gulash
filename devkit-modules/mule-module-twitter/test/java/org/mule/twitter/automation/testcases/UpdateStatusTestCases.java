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

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import twitter4j.Status;


public class UpdateStatusTestCases extends TwitterTestParent {
	
	private Status status;
	
    @After
    public void tearDown() throws Exception {
    	upsertOnTestRunMessage("statusId", status.getId());
    	runFlowAndGetPayload("destroy-status");
   	
    }
    
    @Category({SmokeTests.class, RegressionTests.class})
	@Test
    public void testUpdateStatus() {
    	try {
    		
    		initializeTestRunMessage("aRandomStatus");
    		
    		status = runFlowAndGetPayload("update-status");
            
            assertEquals(getTestRunMessageValue("status"), status.getText()); 
            upsertOnTestRunMessage("statusId", status.getId());
        	
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	 
    }
	
}