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
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.twitter.automation.TwitterTestUtils;

import twitter4j.Status;



public class RetweetStatusTestCases extends TwitterTestParent {
	
	@Before
    public void setUp() throws Exception {
		initializeTestRunMessage("aRandomStatus");
		upsertOnTestRunMessage("statusId", ((Status) runFlowAndGetPayload("update-status-aux-sandbox")).getId());

    	Thread.sleep(TwitterTestUtils.SETUP_DELAY);
    	 
    }
    
    @After
    public void tearDown() throws Exception {	
    	runFlowAndGetPayload("destroy-status-aux-sandbox");

    }
    
    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testRetweetStatus() {
    	try {
    		Status retweetedStatus = ((Status) runFlowAndGetPayload("retweet-status")).getRetweetedStatus();

	        assertEquals((Long) getTestRunMessageValue("statusId"), (Long) retweetedStatus.getId());
	        assertEquals(getTestRunMessageValue("text").toString(), retweetedStatus.getText());  
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}