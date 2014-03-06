/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter.automation;

import java.util.Iterator;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;

public class TwitterTestUtils {
	
	public static int TIMELINE_DEFAULT_LENGTH = 20;
	public static int SETUP_DELAY = 3000;
	
	public static boolean isStatusIdOnTimeline(ResponseList<Status> statusList, long statusId) {
		
		Status status;
		boolean found = false;
		
		Iterator<Status> iter = statusList.iterator();
        while (iter.hasNext() && found == false){
        	status = iter.next();
        	if (status.getId() == statusId) {
        		found = true;
        	}
        }
        return found;     		
	}
	
	public static boolean isStatusTextOnTimeline(ResponseList<Status> statusList, String statusText) {
		
		Status status;
		boolean found = false;
		
		Iterator<Status> iter = statusList.iterator();
        while (iter.hasNext() && found == false){
        	status = iter.next();
        	if (status.getText().contains(statusText)) {
        		found = true;
        	}
        }
        return found;     		
	}
	
	public static String getStatusTextOnTimeline(ResponseList<Status> statusList, long statusId) {
		
		Status status;
		boolean found = false;
		
		Iterator<Status> iter = statusList.iterator();
        while (iter.hasNext() && found == false){
        	status = iter.next();
        	if (status.getId() == statusId) {
        		return status.getText();
           	}
        } 
        return null;
    }
	
	public static String getStatusTextOnResponseList(ResponseList<Status> statusList, long statusId) {
		
		Status status;
		String statusText = "";
		boolean found = false;
		
		Iterator<Status> iter = statusList.iterator();
        while (iter.hasNext() && found == false){
        	status = iter.next();
        	if (status.getId()==statusId) {
        		statusText = status.getText();
        		found = true;
        	}
        } 
        return statusText;
    }
	
	public static long getIdForStatusTextOnResponseList(ResponseList<Status> statusList, String statusText) {
		
		Status status;
		long statusId = 0;
		boolean found = false;
		
		Iterator<Status> iter = statusList.iterator();
        while (iter.hasNext() && found == false){
        	status = iter.next();
        	if (status.getText().contains(statusText)) {
        		statusId = status.getId();
        		found = true;
        	}
        } 
        return statusId;
    }
	public static boolean isUserOnList(ResponseList<User> userList, long userId) {
		
		User user;
		boolean found = false;
		
		Iterator<User> iter = userList.iterator();
        while (iter.hasNext() && found == false){
        	user = iter.next();
        	if (user.getId() == userId) {
        		found = true;
        	}
        }
        return found;     		
	}

	public static boolean isUserIdOnIdList(IDs ids, long userId) {
		
		 boolean found = false; 
		 long[] idArray = ids.getIDs();
		 
         for(int i=0; i < idArray.length; i++){

             if(idArray[i] == userId){
                     found = true;
                     break;       
             }
         }
         
         if(found){
	             return true;
	     }else{
	             return false;
	     }
		
	}
	
}

