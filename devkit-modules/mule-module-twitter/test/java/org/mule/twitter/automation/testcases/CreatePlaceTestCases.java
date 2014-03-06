/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.mule.modules.tests.ConnectorTestUtils;

import twitter4j.Place;
import twitter4j.ResponseList;

public class CreatePlaceTestCases extends TwitterTestParent {
	    
	
	@Before
	public void setUp() throws Exception {
		initializeTestRunMessage("createPlaceTestData");
	}

	public void testCreatePlace() {	 	
		try {
        	ResponseList<Place> placesList = runFlowAndGetPayload("search-places-by-ip");
        	assertNotNull(placesList);
        	
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}       	

	}
    

	public void testCreatePlacePassingCoordinates() {
		try {
        	ResponseList<Place> placesList = runFlowAndGetPayload("search-places-by-coordinates");
        	assertNotNull(placesList);
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}
    

}
