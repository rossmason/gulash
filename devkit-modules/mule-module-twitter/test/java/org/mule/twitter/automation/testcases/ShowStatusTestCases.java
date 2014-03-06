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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import twitter4j.Status;



public class ShowStatusTestCases extends TwitterTestParent {
    
	private Map<String,Object> aStatus = getBeanFromContext("aRandomStatus");
	
	@Before
    public void setUp() throws Exception {
		initializeTestRunMessage(aStatus);
        upsertOnTestRunMessage("statusId", ((Status) runFlowAndGetPayload("update-status")).getId());

    }
    
    @After
    public void tearDown() throws Exception {
    	initializeTestRunMessage(aStatus);
		runFlowAndGetPayload("destroy-status");
   	
    }
    
    @Category({RegressionTests.class})
	@Test
	public void testShowStatus() {
		try {
			
			Status status = runFlowAndGetPayload("show-status");
			
			Long expectedStatusId = (Long) aStatus.get("statusId");
	        Long actualStatusId = status.getId();
			
			String expectedStatusText = aStatus.get("text").toString();
			String actualStatusText = status.getText();

	        assertEquals(expectedStatusId, actualStatusId);
	        assertEquals(expectedStatusText, actualStatusText);  
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}