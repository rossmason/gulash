[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Initial setup of the Mule project](#initial-setup-of-the-mule-project)  
[First flow](#first-flow)  
[Tweeting and sending direct messages](#tweeting-and-sending-direct-messages)  
[Getting timelines](#getting-timelines)  
[Resources](#other-resources)

###Purpose

I'll be displaying how to meet the configuration requirements for using MuleSoft's Twitter connector in a Mule project and how to use the most common operations.

###Prerequisites

In order to build and run this project you'll need:

*  Have a Twitter account.  
*  A retweet from a user you're following and a tweet with a mention.  
*  To download and install MuleStudio.  
*  Browser to make the requests and view the operations payloads.  

###Initial setup of the Mule project

#### Step 1: Get connector credentials

You will also need to enable your Twitter account for API access with write privileges.
Please see [Configure Twitter for API Access](http://www.mulesoft.org/documentation/display/CLOUDHUB/Configure+Twitter+for+API+Access) for instructions on doing so.

#### Step 2: Create a new Mule project

To begin building this application, start Mule Studio and create a new project:

1. Select **File -\> New -\> Mule Project**  
2. In the New Mule Project configuration menu, provide a name for this project: **twitter-demo**  
3. Click **Next** and provide a name for the flow: **twitter-demo**  
4. Click **Finish**.  

#### Step 3: Store the credentials

In src/main/app/mule-app.properties file that's on your project put the following key/value pairs and replace what's after `=` sign with your credentials values. Make sure you don't have any surrounding white spaces.

	twitter.accessKey=<accessKeyFromTwitterDeveloperSite>
	twitter.accessSecret=<accessSecretFromTwitterDeveloperSite>
	twitter.consumerKey=<consumerKeyFromTwitterDeveloperSite>
	twitter.consumerSecret=consumerSecretFromTwitterDeveloperSite>
 

![](images/image001.png)

#### Step 4: Create a Twitter Global element

1. Click on "Global Elements" tab at the bottom of the Flow editor window.  
2. Click on "Create" to bring up Global Type dialog box.  
3. Filter by "Twitter".  
4. Select "Twitter" from "Cloud Connectors" section.  
5. Populate the fields with property placeholders.  

	${twitter.accessKey}  
	${twitter.accessSecret}  
	${twitter.consumerKey}  
	${twitter.consumerSecret}  

6. Click Ok

![](images/image002.png)

###First flow

Let's create a small Mule flow that will return user information for the authenticated user.

#### Creating the Mule Flow

1. Drag and drop an HTTP Endpoint from Mule Studio palette to the Message Flow canvas.  
2. Double click the flow element and set show-user in the Name field.  
3. Double click the HTTP icon to bring up the properties dialog.  
4. Specify "showUser" in the Path field and click OK.  

![](images/image003.png)  
  

5. Drag and drop Twitter connector next to HTTP Inbound Endpoint.  
6. Double click on Twitter icon to bring up properties dialog.  
7. Set "Show user" as Display Name.  
8. Select Twitter from the Config Reference dropdown.  
9. Select "Show user" as Operation value and click OK.  

![](images/image004.png)  


10. Finally drag and drop an "Object to JSON" transformer next to the "Twitter" connector.  
11. Save the project  

If you click the **Configuration XML** tab this is how the code should look like  

	<?xml version="1.0" encoding="UTF-8"?>
	<mule xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:json="http://www.mulesoft.org/schema/mule/json" xmlns:twitter="http://www.mulesoft.org/schema/mule/twitter" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation" xmlns:spring="http://www.springframework.org/schema/beans" version="EE-3.4.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
	http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
	http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd
	http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
	http://www.mulesoft.org/schema/mule/twitter http://www.mulesoft.org/schema/mule/twitter/current/mule-twitter.xsd
	http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
	    <twitter:config name="Twitter" accessKey="${twitter.accessKey}" accessSecret="${twitter.accessSecret}" consumerKey="${twitter.consumerKey}" consumerSecret="${twitter.consumerSecret}" doc:name="Twitter">
	        <twitter:connection-pooling-profile initialisationPolicy="INITIALISE_ONE" exhaustedAction="WHEN_EXHAUSTED_GROW"/>
	    </twitter:config>
	    <flow name="show-user" doc:name="show-user">
	        <http:inbound-endpoint exchange-pattern="request-response" host="localhost" port="8081" path="showUser" doc:name="HTTP"/>
	        <twitter:show-user config-ref="Twitter" doc:name="Show user"/>
	        <json:object-to-json-transformer doc:name="Object to JSON"/>
	    </flow>
	</mule>

#### Run the application

1. Right click on twitter-demo.mflow and select Run As Mule Application.  
2. Check the console to see when the application starts.

		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		+ Started app 'twitter-demo'                               +
		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

3. Hit the endpoint at <http://localhost:8081/showUser> and check the operation payload formatted as JSON.

		{
		  "id": 1591226834,
		  "name": "MuleConnectorTest",
		  "screenName": "MuleConnector",
		  "location": "",
		  "description": "",
		  "descriptionURLEntities": [],
		  "profileImageUrlHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		  "url": null,
		  "followersCount": 1,
		  "status": {
		    "createdAt": 1373820948000,
		    "id": 356457009333288962,
		    "text": "This is twitter status from Mule",
		    "source": "<a href=\"http://www.mulesoft.org/connectors/twitter#sample\" rel=\"nofollow\">MuleSoft Connector Test</a>",
		    "inReplyToStatusId": -1,
		    "inReplyToUserId": -1,
		    "inReplyToScreenName": null,
		    "geoLocation": null,
		    "place": null,
		    "retweetCount": 0,
		    "retweetedStatus": null,
		    "userMentionEntities": [],
		    "hashtagEntities": [],
		    "mediaEntities": [],
		    "currentUserRetweetId": -1,
		    "user": null,
		    "contributors": [],
		    "urlentities": [],
		    "truncated": false,
		    "favorited": false,
		    "retweet": false,
		    "retweetedByMe": false,
		    "possiblySensitive": false,
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  "profileBackgroundColor": "C0DEED",
		  "profileTextColor": "333333",
		  "profileLinkColor": "0084B4",
		  "profileSidebarFillColor": "DDEEF6",
		  "profileSidebarBorderColor": "C0DEED",
		  "profileUseBackgroundImage": true,
		  "showAllInlineMedia": false,
		  "friendsCount": 2,
		  "createdAt": 1373728616000,
		  "favouritesCount": 0,
		  "utcOffset": -1,
		  "timeZone": null,
		  "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		  "profileBackgroundImageUrlHttps": "https://si0.twimg.com/images/themes/theme1/bg.png",
		  "profileBackgroundTiled": false,
		  "lang": "en",
		  "statusesCount": 3,
		  "translator": false,
		  "listedCount": 0,
		  "protected": false,
		  "geoEnabled": false,
		  "contributorsEnabled": false,
		  "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		  "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		  "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		  "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		  "profileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		  "biggerProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		  "miniProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		  "originalProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		  "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		  "profileBannerURL": null,
		  "profileBannerRetinaURL": null,
		  "profileBannerIPadURL": null,
		  "profileBannerIPadRetinaURL": null,
		  "profileBannerMobileURL": null,
		  "profileBannerMobileRetinaURL": null,
		  "verified": false,
		  "followRequestSent": false,
		  "urlentity": {
		    "start": 0,
		    "end": 0,
		    "url": "",
		    "expandedURL": "",
		    "displayURL": ""
		  },
		  "rateLimitStatus": {
		    "remaining": 179,
		    "limit": 180,
		    "resetTimeInSeconds": 1373905159,
		    "secondsUntilReset": 899,
		    "remainingHits": 179
		  },
		  "accessLevel": 3
		}	
From this response we're going to save the id and screenName of the authenticated user for later use.

###Tweeting and sending direct messages

This next set of operations is related to statuses and direct messages. We're going to be using the user id and screen name we obtained from the Show user response above.

The creation of the flow for each of the next three operations is the same as the one described in the First flow section:

1. Drag and drop an HTTP Inbound Endpoint to the canvas, double click on it and specify "operationName" in the Path field.  
2. Double click the flow element and set "operation-name" in the Name field.  
3. Drag and drop Twitter connector next to HTTP Inbound Endpoint.  
4. Finally drag and drop an "Object to JSON" transformer next to the "Twitter" connector.

![](images/image005.png)

Some customization depending on the operation and its input values will have to be performed. Let's go over each individually.

####Update status

1. Double click on Twitter icon to bring up properties dialog.  
2. Set "Update status" as Display Name.  
3. Select Twitter from the Config Reference dropdown.  
4. Select "Update status" as Operation value.  
5. In the Status field enter "This is a twitter status" and click OK.  

![](images/image006.png)


####Send direct message by screen name

1.  Double click on Twitter icon to bring up properties dialog.  
2.  Set "Send direct message by screen name" as Display Name.  
3.  Select Twitter from the Config Reference dropdown.  
4.  Select "Send direct message by screen name" as Operation value.  
5.  In the Message field enter "\#[header:INBOUND:message]", this expression extracts the "message" parameter value from HTTP request.  
6.  The same goes for the Screen Name field, enter "\#[header:INBOUND:screenName]" and click OK.  
7.  Save the project    

![](images/image007.png)




![](images/image008.png)  

##### Run the application

1. Right click on twitter-demo.mflow and select Run As Mule Application, if it's not already running.  
2. Check the console to see when the application starts.  

		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		+ Started app 'twitter-demo'                               +
		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
3. Hit the endpoint at <http://localhost:8081/updateStatus>. Check the text field of the JSON object returned as payload.

		{
		  "createdAt": 1373820948000,
		  "id": 356457009333288962,
		  "text": "This is twitter status from Mule",
		  "source": "<a href=\"http://www.mulesoft.org/connectors/twitter#sample\" rel=\"nofollow\">MuleSoft Connector Test</a>",
		  "inReplyToStatusId": -1,
		  "inReplyToUserId": -1,
		  "inReplyToScreenName": null,
		  "geoLocation": null,
		  "place": null,
		  "retweetCount": 0,
		  "retweetedStatus": null,
		  "userMentionEntities": [],
		  "hashtagEntities": [],
		  "mediaEntities": [],
		  "currentUserRetweetId": -1,
		  "user": {
		    "id": 1591226834,
		    "name": "MuleConnectorTest",
		    "screenName": "MuleConnector",
		    "location": null,
		    "description": null,
		    "descriptionURLEntities": [],
		    "profileImageUrlHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_normal.png",
		    "url": null,
		    "followersCount": 1,
		    "status": null,
		    "profileBackgroundColor": "C0DEED",
		    "profileTextColor": "333333",
		    "profileLinkColor": "0084B4",
		    "profileSidebarFillColor": "DDEEF6",
		    "profileSidebarBorderColor": "C0DEED",
		    "profileUseBackgroundImage": true,
		    "showAllInlineMedia": false,
		    "friendsCount": 2,
		    "createdAt": 1373728616000,
		    "favouritesCount": 0,
		    "utcOffset": -1,
		    "timeZone": null,
		    "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		    "profileBackgroundImageUrlHttps": "https://twimg0-a.akamaihd.net/images/themes/theme1/bg.png",
		    "profileBackgroundTiled": false,
		    "lang": "en",
		    "statusesCount": 3,
		    "translator": false,
		    "listedCount": 0,
		    "protected": false,
		    "geoEnabled": false,
		    "contributorsEnabled": false,
		    "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		    "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		    "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		    "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		    "profileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_normal.png",
		    "biggerProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_bigger.png",
		    "miniProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_mini.png",
		    "originalProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1.png",
		    "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		    "profileBannerURL": null,
		    "profileBannerRetinaURL": null,
		    "profileBannerIPadURL": null,
		    "profileBannerIPadRetinaURL": null,
		    "profileBannerMobileURL": null,
		    "profileBannerMobileRetinaURL": null,
		    "verified": false,
		    "followRequestSent": false,
		    "urlentity": {
		      "start": 0,
		      "end": 0,
		      "url": "",
		      "expandedURL": "",
		      "displayURL": ""
		    },
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  "contributors": [],
		  "urlentities": [],
		  "truncated": false,
		  "favorited": false,
		  "retweet": false,
		  "retweetedByMe": false,
		  "possiblySensitive": false,
		  "rateLimitStatus": null,
		  "accessLevel": 3
		}

Also if you go to your Twitter homepage a new tweet appears containing the text you tweeted through the connector.

4. Replace the message and the screenName values in this URL <http://localhost:8081/sendDirectMessageByScreenName?message=MessageValue&screenName=ScreenNameValue> and hit the endpoint.  
This operations Sends a new direct message to the specified user from the authenticating user.  
Pay attention to the sender, receiver and message fields of the response.  

		{
		  "id": 356811610226032640,
		  "text": "TestDirectMessageByScreenName",
		  "senderId": 1591226834,
		  "recipientId": 1591226834,
		  "createdAt": 1373905491000,
		  "senderScreenName": "MuleConnector",
		  "recipientScreenName": "MuleConnector",
		  "userMentionEntities": [],
		  "hashtagEntities": [],
		  "mediaEntities": [],
		  "sender": {
		    "id": 1591226834,
		    "name": "MuleConnectorTest",
		    "screenName": "MuleConnector",
		    "location": null,
		    "description": null,
		    "descriptionURLEntities": [],
		    "profileImageUrlHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_normal.png",
		    "url": null,
		    "followersCount": 1,
		    "status": null,
		    "profileBackgroundColor": "C0DEED",
		    "profileTextColor": "333333",
		    "profileLinkColor": "0084B4",
		    "profileSidebarFillColor": "DDEEF6",
		    "profileSidebarBorderColor": "C0DEED",
		    "profileUseBackgroundImage": true,
		    "showAllInlineMedia": false,
		    "friendsCount": 2,
		    "createdAt": 1373728616000,
		    "favouritesCount": 0,
		    "utcOffset": -1,
		    "timeZone": null,
		    "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		    "profileBackgroundImageUrlHttps": "https://twimg0-a.akamaihd.net/images/themes/theme1/bg.png",
		    "profileBackgroundTiled": false,
		    "lang": "en",
		    "statusesCount": 4,
		    "translator": false,
		    "listedCount": 0,
		    "protected": false,
		    "geoEnabled": false,
		    "contributorsEnabled": false,
		    "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		    "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		    "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		    "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		    "profileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_normal.png",
		    "biggerProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_bigger.png",
		    "miniProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_mini.png",
		    "originalProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1.png",
		    "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		    "profileBannerURL": null,
		    "profileBannerRetinaURL": null,
		    "profileBannerIPadURL": null,
		    "profileBannerIPadRetinaURL": null,
		    "profileBannerMobileURL": null,
		    "profileBannerMobileRetinaURL": null,
		    "verified": false,
		    "followRequestSent": false,
		    "urlentity": {
		      "start": 0,
		      "end": 0,
		      "url": "",
		      "expandedURL": "",
		      "displayURL": ""
		    },
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  "recipient": {
		    "id": 1591226834,
		    "name": "MuleConnectorTest",
		    "screenName": "MuleConnector",
		    "location": null,
		    "description": null,
		    "descriptionURLEntities": [],
		    "profileImageUrlHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_normal.png",
		    "url": null,
		    "followersCount": 1,
		    "status": null,
		    "profileBackgroundColor": "C0DEED",
		    "profileTextColor": "333333",
		    "profileLinkColor": "0084B4",
		    "profileSidebarFillColor": "DDEEF6",
		    "profileSidebarBorderColor": "C0DEED",
		    "profileUseBackgroundImage": true,
		    "showAllInlineMedia": false,
		    "friendsCount": 2,
		    "createdAt": 1373728616000,
		    "favouritesCount": 0,
		    "utcOffset": -1,
		    "timeZone": null,
		    "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		    "profileBackgroundImageUrlHttps": "https://twimg0-a.akamaihd.net/images/themes/theme1/bg.png",
		    "profileBackgroundTiled": false,
		    "lang": "en",
		    "statusesCount": 4,
		    "translator": false,
		    "listedCount": 0,
		    "protected": false,
		    "geoEnabled": false,
		    "contributorsEnabled": false,
		    "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		    "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		    "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		    "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		    "profileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_normal.png",
		    "biggerProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_bigger.png",
		    "miniProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1_mini.png",
		    "originalProfileImageURLHttps": "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_1.png",
		    "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		    "profileBannerURL": null,
		    "profileBannerRetinaURL": null,
		    "profileBannerIPadURL": null,
		    "profileBannerIPadRetinaURL": null,
		    "profileBannerMobileURL": null,
		    "profileBannerMobileRetinaURL": null,
		    "verified": false,
		    "followRequestSent": false,
		    "urlentity": {
		      "start": 0,
		      "end": 0,
		      "url": "",
		      "expandedURL": "",
		      "displayURL": ""
		    },
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  "urlentities": [],
		  "rateLimitStatus": null,
		  "accessLevel": 3
		}

If you go to your Twitter homepage and check your Direct Messages it can be found among them.

###Getting timelines

Like in the previous section we're going to be using the same flow pattern but for timeline and mentions related operations.


####Get user timeline by user id

1. Double click on Twitter icon to bring up properties dialog.  
2. Set "Get user timeline by user id" as Display Name.  
3. Select Twitter from the Config Reference dropdown.  
4. Select "Get user timeline by user id" as Operation value.  
5. In the User Id field, enter "\#[header:INBOUND:userId]" and click OK.  
6. Save the project    

![](images/image009.png)  

##### Run the application

1. Right click on twitter-demo.mflow and select Run As Mule Application, if it's not already running.
2. Check the console to see when the application starts.

		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		+ Started app 'twitter-demo'                               +
		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

3. Replace the userId value in this URL <http://localhost:8081/getUserTimelineByUserId?userId=userIdValue> and hit the endpoint.

		[
		  {
		    "createdAt": 1373904493000,
		    "id": 356807422012174336,
		    "text": "This is twitter status from Mule",
		    "source": "<a href=\"http://www.mulesoft.org/connectors/twitter#sample\" rel=\"nofollow\">MuleSoft Connector Test</a>",
		    "inReplyToStatusId": -1,
		    "inReplyToUserId": -1,
		    "inReplyToScreenName": null,
		    "geoLocation": null,
		    "place": null,
		    "retweetCount": 0,
		    "retweetedStatus": null,
		    "userMentionEntities": [],
		    "hashtagEntities": [],
		    "mediaEntities": [],
		    "currentUserRetweetId": -1,
		    "user": {
		      "id": 1591226834,
		      "name": "MuleConnectorTest",
		      "screenName": "MuleConnector",
		      "location": "",
		      "description": "",
		      "descriptionURLEntities": [],
		      "profileImageUrlHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "url": null,
		      "followersCount": 1,
		      "status": null,
		      "profileBackgroundColor": "C0DEED",
		      "profileTextColor": "333333",
		      "profileLinkColor": "0084B4",
		      "profileSidebarFillColor": "DDEEF6",
		      "profileSidebarBorderColor": "C0DEED",
		      "profileUseBackgroundImage": true,
		      "showAllInlineMedia": false,
		      "friendsCount": 2,
		      "createdAt": 1373728616000,
		      "favouritesCount": 0,
		      "utcOffset": -1,
		      "timeZone": null,
		      "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundImageUrlHttps": "https://si0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundTiled": false,
		      "lang": "en",
		      "statusesCount": 4,
		      "translator": false,
		      "listedCount": 0,
		      "protected": false,
		      "geoEnabled": false,
		      "contributorsEnabled": false,
		      "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBannerURL": null,
		      "profileBannerRetinaURL": null,
		      "profileBannerIPadURL": null,
		      "profileBannerIPadRetinaURL": null,
		      "profileBannerMobileURL": null,
		      "profileBannerMobileRetinaURL": null,
		      "verified": false,
		      "followRequestSent": false,
		      "urlentity": {
		        "start": 0,
		        "end": 0,
		        "url": "",
		        "expandedURL": "",
		        "displayURL": ""
		      },
		      "rateLimitStatus": null,
		      "accessLevel": 0
		    },
		    "contributors": [],
		    "urlentities": [],
		    "truncated": false,
		    "favorited": false,
		    "retweet": false,
		    "retweetedByMe": false,
		    "possiblySensitive": false,
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  {
		    "createdAt": 1373820948000,
		    "id": 356457009333288962,
		    "text": "This is twitter status from Mule",
		    "source": "<a href=\"http://www.mulesoft.org/connectors/twitter#sample\" rel=\"nofollow\">MuleSoft Connector Test</a>",
		    "inReplyToStatusId": -1,
		    "inReplyToUserId": -1,
		    "inReplyToScreenName": null,
		    "geoLocation": null,
		    "place": null,
		    "retweetCount": 0,
		    "retweetedStatus": null,
		    "userMentionEntities": [],
		    "hashtagEntities": [],
		    "mediaEntities": [],
		    "currentUserRetweetId": -1,
		    "user": {
		      "id": 1591226834,
		      "name": "MuleConnectorTest",
		      "screenName": "MuleConnector",
		      "location": "",
		      "description": "",
		      "descriptionURLEntities": [],
		      "profileImageUrlHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "url": null,
		      "followersCount": 1,
		      "status": null,
		      "profileBackgroundColor": "C0DEED",
		      "profileTextColor": "333333",
		      "profileLinkColor": "0084B4",
		      "profileSidebarFillColor": "DDEEF6",
		      "profileSidebarBorderColor": "C0DEED",
		      "profileUseBackgroundImage": true,
		      "showAllInlineMedia": false,
		      "friendsCount": 2,
		      "createdAt": 1373728616000,
		      "favouritesCount": 0,
		      "utcOffset": -1,
		      "timeZone": null,
		      "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundImageUrlHttps": "https://si0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundTiled": false,
		      "lang": "en",
		      "statusesCount": 4,
		      "translator": false,
		      "listedCount": 0,
		      "protected": false,
		      "geoEnabled": false,
		      "contributorsEnabled": false,
		      "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBannerURL": null,
		      "profileBannerRetinaURL": null,
		      "profileBannerIPadURL": null,
		      "profileBannerIPadRetinaURL": null,
		      "profileBannerMobileURL": null,
		      "profileBannerMobileRetinaURL": null,
		      "verified": false,
		      "followRequestSent": false,
		      "urlentity": {
		        "start": 0,
		        "end": 0,
		        "url": "",
		        "expandedURL": "",
		        "displayURL": ""
		      },
		      "rateLimitStatus": null,
		      "accessLevel": 0
		    },
		    "contributors": [],
		    "urlentities": [],
		    "truncated": false,
		    "favorited": false,
		    "retweet": false,
		    "retweetedByMe": false,
		    "possiblySensitive": false,
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  {
		    "createdAt": 1373728798000,
		    "id": 356070504458760192,
		    "text": "RT @MuleSoft: Learn how to get the most data quickly - Best Practices for #Salesforce Data Loading [infographic] http://t.co/JU6Dmk7gHB",
		    "source": "web",
		    "inReplyToStatusId": -1,
		    "inReplyToUserId": -1,
		    "inReplyToScreenName": null,
		    "geoLocation": null,
		    "place": null,
		    "retweetCount": 2,
		    "retweetedStatus": {
		      "createdAt": 1373580382000,
		      "id": 355448001516421120,
		      "text": "Learn how to get the most data quickly - Best Practices for #Salesforce Data Loading [infographic] http://t.co/JU6Dmk7gHB",
		      "source": "<a href=\"http://www.hootsuite.com\" rel=\"nofollow\">HootSuite</a>",
		      "inReplyToStatusId": -1,
		      "inReplyToUserId": -1,
		      "inReplyToScreenName": null,
		      "geoLocation": null,
		      "place": null,
		      "retweetCount": 2,
		      "retweetedStatus": null,
		      "userMentionEntities": [],
		      "hashtagEntities": [
		        {
		          "start": 60,
		          "end": 71,
		          "text": "Salesforce"
		        }
		      ],
		      "mediaEntities": [],
		      "currentUserRetweetId": -1,
		      "user": {
		        "id": 15358364,
		        "name": "MuleSoft",
		        "screenName": "MuleSoft",
		        "location": "US, Argentina, UK, AUS, APAC",
		        "description": "Connecting the New Enterprise",
		        "descriptionURLEntities": [],
		        "profileImageUrlHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_normal.jpeg",
		        "url": "http://t.co/qF2EtelVo2",
		        "followersCount": 1903,
		        "status": null,
		        "profileBackgroundColor": "000000",
		        "profileTextColor": "333333",
		        "profileLinkColor": "0088CC",
		        "profileSidebarFillColor": "DDEEF6",
		        "profileSidebarBorderColor": "FFFFFF",
		        "profileUseBackgroundImage": true,
		        "showAllInlineMedia": false,
		        "friendsCount": 710,
		        "createdAt": 1215551806000,
		        "favouritesCount": 1,
		        "utcOffset": -28800,
		        "timeZone": "Pacific Time (US & Canada)",
		        "profileBackgroundImageUrl": "http://a0.twimg.com/profile_background_images/820268245/8d4e8a365da8fe214abeb03957a3cb46.jpeg",
		        "profileBackgroundImageUrlHttps": "https://si0.twimg.com/profile_background_images/820268245/8d4e8a365da8fe214abeb03957a3cb46.jpeg",
		        "profileBackgroundTiled": true,
		        "lang": "en",
		        "statusesCount": 1254,
		        "translator": false,
		        "listedCount": 113,
		        "protected": false,
		        "geoEnabled": false,
		        "contributorsEnabled": false,
		        "profileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_normal.jpeg",
		        "biggerProfileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_bigger.jpeg",
		        "miniProfileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_mini.jpeg",
		        "originalProfileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e.jpeg",
		        "profileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_normal.jpeg",
		        "biggerProfileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_bigger.jpeg",
		        "miniProfileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_mini.jpeg",
		        "originalProfileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e.jpeg",
		        "profileBackgroundImageURL": "http://a0.twimg.com/profile_background_images/820268245/8d4e8a365da8fe214abeb03957a3cb46.jpeg",
		        "profileBannerURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/web",
		        "profileBannerRetinaURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/web_retina",
		        "profileBannerIPadURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/ipad",
		        "profileBannerIPadRetinaURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/ipad_retina",
		        "profileBannerMobileURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/mobile",
		        "profileBannerMobileRetinaURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/ipad_retina",
		        "verified": false,
		        "followRequestSent": false,
		        "urlentity": {
		          "start": 0,
		          "end": 22,
		          "url": "http://t.co/qF2EtelVo2",
		          "expandedURL": "http://www.MuleSoft.com",
		          "displayURL": "MuleSoft.com"
		        },
		        "rateLimitStatus": null,
		        "accessLevel": 0
		      },
		      "contributors": [],
		      "urlentities": [
		        {
		          "start": 99,
		          "end": 121,
		          "url": "http://t.co/JU6Dmk7gHB",
		          "expandedURL": "http://ow.ly/mSNmu",
		          "displayURL": "ow.ly/mSNmu"
		        }
		      ],
		      "truncated": false,
		      "favorited": false,
		      "retweet": false,
		      "retweetedByMe": false,
		      "possiblySensitive": false,
		      "rateLimitStatus": null,
		      "accessLevel": 0
		    },
		    "userMentionEntities": [
		      {
		        "start": 3,
		        "end": 12,
		        "name": "MuleSoft",
		        "screenName": "MuleSoft",
		        "id": 15358364
		      }
		    ],
		    "hashtagEntities": [
		      {
		        "start": 74,
		        "end": 85,
		        "text": "Salesforce"
		      }
		    ],
		    "mediaEntities": [],
		    "currentUserRetweetId": 356070504458760192,
		    "user": {
		      "id": 1591226834,
		      "name": "MuleConnectorTest",
		      "screenName": "MuleConnector",
		      "location": "",
		      "description": "",
		      "descriptionURLEntities": [],
		      "profileImageUrlHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "url": null,
		      "followersCount": 1,
		      "status": null,
		      "profileBackgroundColor": "C0DEED",
		      "profileTextColor": "333333",
		      "profileLinkColor": "0084B4",
		      "profileSidebarFillColor": "DDEEF6",
		      "profileSidebarBorderColor": "C0DEED",
		      "profileUseBackgroundImage": true,
		      "showAllInlineMedia": false,
		      "friendsCount": 2,
		      "createdAt": 1373728616000,
		      "favouritesCount": 0,
		      "utcOffset": -1,
		      "timeZone": null,
		      "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundImageUrlHttps": "https://si0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundTiled": false,
		      "lang": "en",
		      "statusesCount": 4,
		      "translator": false,
		      "listedCount": 0,
		      "protected": false,
		      "geoEnabled": false,
		      "contributorsEnabled": false,
		      "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBannerURL": null,
		      "profileBannerRetinaURL": null,
		      "profileBannerIPadURL": null,
		      "profileBannerIPadRetinaURL": null,
		      "profileBannerMobileURL": null,
		      "profileBannerMobileRetinaURL": null,
		      "verified": false,
		      "followRequestSent": false,
		      "urlentity": {
		        "start": 0,
		        "end": 0,
		        "url": "",
		        "expandedURL": "",
		        "displayURL": ""
		      },
		      "rateLimitStatus": null,
		      "accessLevel": 0
		    },
		    "contributors": [],
		    "urlentities": [
		      {
		        "start": 113,
		        "end": 135,
		        "url": "http://t.co/JU6Dmk7gHB",
		        "expandedURL": "http://ow.ly/mSNmu",
		        "displayURL": "ow.ly/mSNmu"
		      }
		    ],
		    "truncated": false,
		    "favorited": false,
		    "retweet": true,
		    "retweetedByMe": true,
		    "possiblySensitive": false,
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  },
		  {
		    "createdAt": 1373728786000,
		    "id": 356070453044981760,
		    "text": "RT @MuleSoft: Ready for the #integration tsunami? Get your free copy of the Forrester Hybrid Integration report http://t.co/D3ka4TpUuZ #hyb…",
		    "source": "web",
		    "inReplyToStatusId": -1,
		    "inReplyToUserId": -1,
		    "inReplyToScreenName": null,
		    "geoLocation": null,
		    "place": null,
		    "retweetCount": 7,
		    "retweetedStatus": {
		      "createdAt": 1373642750000,
		      "id": 355709594082877440,
		      "text": "Ready for the #integration tsunami? Get your free copy of the Forrester Hybrid Integration report http://t.co/D3ka4TpUuZ #hybridcloud",
		      "source": "<a href=\"http://www.hootsuite.com\" rel=\"nofollow\">HootSuite</a>",
		      "inReplyToStatusId": -1,
		      "inReplyToUserId": -1,
		      "inReplyToScreenName": null,
		      "geoLocation": null,
		      "place": null,
		      "retweetCount": 7,
		      "retweetedStatus": null,
		      "userMentionEntities": [],
		      "hashtagEntities": [
		        {
		          "start": 14,
		          "end": 26,
		          "text": "integration"
		        },
		        {
		          "start": 121,
		          "end": 133,
		          "text": "hybridcloud"
		        }
		      ],
		      "mediaEntities": [],
		      "currentUserRetweetId": -1,
		      "user": {
		        "id": 15358364,
		        "name": "MuleSoft",
		        "screenName": "MuleSoft",
		        "location": "US, Argentina, UK, AUS, APAC",
		        "description": "Connecting the New Enterprise",
		        "descriptionURLEntities": [],
		        "profileImageUrlHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_normal.jpeg",
		        "url": "http://t.co/qF2EtelVo2",
		        "followersCount": 1903,
		        "status": null,
		        "profileBackgroundColor": "000000",
		        "profileTextColor": "333333",
		        "profileLinkColor": "0088CC",
		        "profileSidebarFillColor": "DDEEF6",
		        "profileSidebarBorderColor": "FFFFFF",
		        "profileUseBackgroundImage": true,
		        "showAllInlineMedia": false,
		        "friendsCount": 710,
		        "createdAt": 1215551806000,
		        "favouritesCount": 1,
		        "utcOffset": -28800,
		        "timeZone": "Pacific Time (US & Canada)",
		        "profileBackgroundImageUrl": "http://a0.twimg.com/profile_background_images/820268245/8d4e8a365da8fe214abeb03957a3cb46.jpeg",
		        "profileBackgroundImageUrlHttps": "https://si0.twimg.com/profile_background_images/820268245/8d4e8a365da8fe214abeb03957a3cb46.jpeg",
		        "profileBackgroundTiled": true,
		        "lang": "en",
		        "statusesCount": 1254,
		        "translator": false,
		        "listedCount": 113,
		        "protected": false,
		        "geoEnabled": false,
		        "contributorsEnabled": false,
		        "profileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_normal.jpeg",
		        "biggerProfileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_bigger.jpeg",
		        "miniProfileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_mini.jpeg",
		        "originalProfileImageURL": "http://a0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e.jpeg",
		        "profileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_normal.jpeg",
		        "biggerProfileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_bigger.jpeg",
		        "miniProfileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e_mini.jpeg",
		        "originalProfileImageURLHttps": "https://si0.twimg.com/profile_images/3434513773/42e3645f12ed15b13a5782ba8b61de3e.jpeg",
		        "profileBackgroundImageURL": "http://a0.twimg.com/profile_background_images/820268245/8d4e8a365da8fe214abeb03957a3cb46.jpeg",
		        "profileBannerURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/web",
		        "profileBannerRetinaURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/web_retina",
		        "profileBannerIPadURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/ipad",
		        "profileBannerIPadRetinaURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/ipad_retina",
		        "profileBannerMobileURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/mobile",
		        "profileBannerMobileRetinaURL": "https://pbs.twimg.com/profile_banners/15358364/1364324907/ipad_retina",
		        "verified": false,
		        "followRequestSent": false,
		        "urlentity": {
		          "start": 0,
		          "end": 22,
		          "url": "http://t.co/qF2EtelVo2",
		          "expandedURL": "http://www.MuleSoft.com",
		          "displayURL": "MuleSoft.com"
		        },
		        "rateLimitStatus": null,
		        "accessLevel": 0
		      },
		      "contributors": [],
		      "urlentities": [
		        {
		          "start": 98,
		          "end": 120,
		          "url": "http://t.co/D3ka4TpUuZ",
		          "expandedURL": "http://ow.ly/mSX27",
		          "displayURL": "ow.ly/mSX27"
		        }
		      ],
		      "truncated": false,
		      "favorited": false,
		      "retweet": false,
		      "retweetedByMe": false,
		      "possiblySensitive": false,
		      "rateLimitStatus": null,
		      "accessLevel": 0
		    },
		    "userMentionEntities": [
		      {
		        "start": 3,
		        "end": 12,
		        "name": "MuleSoft",
		        "screenName": "MuleSoft",
		        "id": 15358364
		      }
		    ],
		    "hashtagEntities": [
		      {
		        "start": 28,
		        "end": 40,
		        "text": "integration"
		      },
		      {
		        "start": 135,
		        "end": 139,
		        "text": "hyb"
		      }
		    ],
		    "mediaEntities": [],
		    "currentUserRetweetId": 356070453044981760,
		    "user": {
		      "id": 1591226834,
		      "name": "MuleConnectorTest",
		      "screenName": "MuleConnector",
		      "location": "",
		      "description": "",
		      "descriptionURLEntities": [],
		      "profileImageUrlHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "url": null,
		      "followersCount": 1,
		      "status": null,
		      "profileBackgroundColor": "C0DEED",
		      "profileTextColor": "333333",
		      "profileLinkColor": "0084B4",
		      "profileSidebarFillColor": "DDEEF6",
		      "profileSidebarBorderColor": "C0DEED",
		      "profileUseBackgroundImage": true,
		      "showAllInlineMedia": false,
		      "friendsCount": 2,
		      "createdAt": 1373728616000,
		      "favouritesCount": 0,
		      "utcOffset": -1,
		      "timeZone": null,
		      "profileBackgroundImageUrl": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundImageUrlHttps": "https://si0.twimg.com/images/themes/theme1/bg.png",
		      "profileBackgroundTiled": false,
		      "lang": "en",
		      "statusesCount": 4,
		      "translator": false,
		      "listedCount": 0,
		      "protected": false,
		      "geoEnabled": false,
		      "contributorsEnabled": false,
		      "profileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURL": "http://a0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_normal.png",
		      "biggerProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_bigger.png",
		      "miniProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1_mini.png",
		      "originalProfileImageURLHttps": "https://si0.twimg.com/sticky/default_profile_images/default_profile_1.png",
		      "profileBackgroundImageURL": "http://a0.twimg.com/images/themes/theme1/bg.png",
		      "profileBannerURL": null,
		      "profileBannerRetinaURL": null,
		      "profileBannerIPadURL": null,
		      "profileBannerIPadRetinaURL": null,
		      "profileBannerMobileURL": null,
		      "profileBannerMobileRetinaURL": null,
		      "verified": false,
		      "followRequestSent": false,
		      "urlentity": {
		        "start": 0,
		        "end": 0,
		        "url": "",
		        "expandedURL": "",
		        "displayURL": ""
		      },
		      "rateLimitStatus": null,
		      "accessLevel": 0
		    },
		    "contributors": [],
		    "urlentities": [
		      {
		        "start": 112,
		        "end": 134,
		        "url": "http://t.co/D3ka4TpUuZ",
		        "expandedURL": "http://ow.ly/mSX27",
		        "displayURL": "ow.ly/mSX27"
		      }
		    ],
		    "truncated": false,
		    "favorited": false,
		    "retweet": true,
		    "retweetedByMe": true,
		    "possiblySensitive": false,
		    "rateLimitStatus": null,
		    "accessLevel": 0
		  }
		]

The response is the same as the one we would get from from **Get user timeline** and **Get user timeline by user screen name**.

### Other Resources

For more information on:

- Twitter.com connector, please visit [http://www.mulesoft.org/connectors/twitter](http://www.mulesoft.org/connectors/twitter)

- Mule AnyPoint™ connectors, please visit [http://www.mulesoft.org/connectors](http://www.mulesoft.org/connectors)

- Mule platform and how to build Mule apps, please visit [http://www.mulesoft.org/documentation/display/current/Home](http://www.mulesoft.org/documentation/display/current/Home)


