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

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import twitter4j.Status;

public class DestroyStatusTestCases extends TwitterTestParent {
	
    @Before
    public void setUp() throws Exception {
    	initializeTestRunMessage("aStatus");
        upsertOnTestRunMessage("statusId", ((Status) runFlowAndGetPayload("update-status")).getId());
        
    }
    
    @Category({SmokeTests.class, RegressionTests.class})
    @Test
    public void testDestroyStatus() {
    	try {
    		
    		Status destroyedStatus = runFlowAndGetPayload("destroy-status");
    		
        	Long expectedStatusId = getTestRunMessageValue("statusId");
	        Long actualStatusId = destroyedStatus.getId();
			
			String expectedStatusText = getTestRunMessageValue("text");
			String actualStatusText = destroyedStatus.getText();
			
	        assertEquals(expectedStatusId, actualStatusId);
	        assertEquals(expectedStatusText, actualStatusText);  

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}     

    }

}