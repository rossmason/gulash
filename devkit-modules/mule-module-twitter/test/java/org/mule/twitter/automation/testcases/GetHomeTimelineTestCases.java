/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.twitter.automation.TwitterTestUtils;

import twitter4j.ResponseList;
import twitter4j.Status;

public class GetHomeTimelineTestCases extends TwitterTestParent {
	
	private Status firstStatus;
	private Status secondStatus;
	
    @Before
    public void setUp() throws Exception { 	
    	firstStatus = runFlowAndGetPayload("update-status","aRandomStatus");
    	secondStatus = runFlowAndGetPayload("update-status","aRandomStatus");

    	Thread.sleep(TwitterTestUtils.SETUP_DELAY);
    	 
    }

    @After
    public void tearDown() throws Exception {
		upsertOnTestRunMessage("statusId", firstStatus.getId());
    	runFlowAndGetPayload("destroy-status");
    	upsertOnTestRunMessage("statusId", secondStatus.getId());
    	runFlowAndGetPayload("destroy-status");
   	
    }
	
    @Category({RegressionTests.class})
	@Test
	public void testGetHomeTimelineDefaultValues() {
    	Long expectedStatusId = firstStatus.getId();
    	
		try {
			ResponseList<Status> timeLine = runFlowAndGetPayload("get-home-timeline-default-values");
			
			assertTrue(TwitterTestUtils.isStatusIdOnTimeline(timeLine, expectedStatusId));

	        assertEquals(firstStatus.getText(), TwitterTestUtils.getStatusTextOnTimeline(timeLine, expectedStatusId));
	        assertTrue(timeLine.size() <= TwitterTestUtils.TIMELINE_DEFAULT_LENGTH);
	      
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
        
	} 
	
    @Category({RegressionTests.class})
	@Test
	public void testGetHomeTimelineParametrized() {
    	Long expectedStatusId = secondStatus.getId();
    	
    	initializeTestRunMessage("getHomeTimelineTestData");
		upsertOnTestRunMessage("sinceId", firstStatus.getId());
		
		try {
			ResponseList<Status> timeLine = runFlowAndGetPayload("get-home-timeline-parameterized");

			assertTrue(TwitterTestUtils.isStatusIdOnTimeline(timeLine, expectedStatusId));
	        assertEquals(secondStatus.getText(), TwitterTestUtils.getStatusTextOnTimeline(timeLine, expectedStatusId));        
	        assertTrue(timeLine.size() <= (Integer) getTestRunMessageValue("count"));
	      
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
        
	}
	
}
