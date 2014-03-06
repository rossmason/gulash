/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.twitter.automation.TwitterTestUtils;

import twitter4j.QueryResult;
import twitter4j.Status;



public class SearchTestCases extends TwitterTestParent {
	
	private Map<String,Object> aTweetToQueryFor = getBeanFromContext("aTweetToQueryFor");
	
    @Before
    public void setUp() throws Exception {
		aTweetToQueryFor.put("statusId", ((Status) runFlowAndGetPayload("update-status")).getId());
		Thread.sleep(TwitterTestUtils.SETUP_DELAY);
    	 
    }
    
    @After
    public void tearDown() throws Exception {
    	initializeTestRunMessage(aTweetToQueryFor);
    	runFlowAndGetPayload("destroy-status");
    }
    
    @Category({RegressionTests.class})
	@Test
	public void testGetRetweetsByIdsDefaultValues() {
		try {
			QueryResult responseList = runFlowAndGetPayload("search-default-values");
			List<Status> tweets = (List<Status>) responseList.getTweets();
			assertTrue(tweets.size() != 0);
			
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
        
	} 
   
}