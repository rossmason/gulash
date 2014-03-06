/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter.automation;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.twitter.automation.testcases.CreatePlaceTestCases;
import org.mule.twitter.automation.testcases.DestroyStatusTestCases;
import org.mule.twitter.automation.testcases.GetAvailableTrendsTestCases;
import org.mule.twitter.automation.testcases.GetGeoDetailsTestCases;
import org.mule.twitter.automation.testcases.GetHomeTimelineTestCases;
import org.mule.twitter.automation.testcases.GetLocationTrendsTestCases;
import org.mule.twitter.automation.testcases.GetMentionsTestCases;
import org.mule.twitter.automation.testcases.GetRetweetsOfMeTestCases;
import org.mule.twitter.automation.testcases.GetRetweetsTestCases;
import org.mule.twitter.automation.testcases.GetUserTimelineByScreenNameTestCases;
import org.mule.twitter.automation.testcases.GetUserTimelineByUserIdTestCases;
import org.mule.twitter.automation.testcases.GetUserTimelineTestCases;
import org.mule.twitter.automation.testcases.RegressionTests;
import org.mule.twitter.automation.testcases.RetweetStatusTestCases;
import org.mule.twitter.automation.testcases.ReverseGeoCodeTestCases;
import org.mule.twitter.automation.testcases.SearchPlacesTestCases;
import org.mule.twitter.automation.testcases.SearchTestCases;
import org.mule.twitter.automation.testcases.SendDirectMessageByScreenNameTestCases;
import org.mule.twitter.automation.testcases.SendDirectMessageByUserIdTestCases;
import org.mule.twitter.automation.testcases.ShowStatusTestCases;
import org.mule.twitter.automation.testcases.ShowUserTestCases;
import org.mule.twitter.automation.testcases.UpdateStatusTestCases;

@RunWith(Categories.class)
@IncludeCategory(RegressionTests.class)
@SuiteClasses({ 
		CreatePlaceTestCases.class, DestroyStatusTestCases.class,
		GetAvailableTrendsTestCases.class,
		GetGeoDetailsTestCases.class, GetHomeTimelineTestCases.class,
		GetLocationTrendsTestCases.class, GetMentionsTestCases.class,
		GetRetweetsOfMeTestCases.class, GetRetweetsTestCases.class,
		GetUserTimelineByScreenNameTestCases.class,
		GetUserTimelineByUserIdTestCases.class, GetUserTimelineTestCases.class,
		RetweetStatusTestCases.class,
		ReverseGeoCodeTestCases.class, SearchPlacesTestCases.class,
		SearchTestCases.class, SendDirectMessageByScreenNameTestCases.class,
		SendDirectMessageByUserIdTestCases.class, ShowStatusTestCases.class,
		ShowUserTestCases.class, UpdateStatusTestCases.class 
		})
public class RegressionTestSuite {
}
