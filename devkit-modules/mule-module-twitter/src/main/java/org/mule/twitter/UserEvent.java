/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import twitter4j.User;

/**
 * {@link UserEvent} represent an event on an user stream
 *
 * @author flbulgarelli
 */
public class UserEvent {
    private final EventType eventType;
    private final User sourceUser;
    private final User targetUser;
    private final Object payload;

    public UserEvent(EventType eventType, User sourceUser, User targetUser, Object payload) {
        Validate.notNull(eventType);
        this.eventType = eventType;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.payload = payload;
    }

    /**
     * @return The user who generated the event, ie, the user that blocked another
     *         one
     */
    public User getSourceUser() {
        return sourceUser;
    }

    /**
     * @return The user that was target of this event, ie, the user was was blocked
     */
    public User getTargetUser() {
        return targetUser;
    }

    /**
     * @return The type of event
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * @return The paylaod of the event
     */
    public Object getPayload() {
        return payload;
    }

    public static UserEvent fromPayload(EventType eventType, User user, Object payload) {
        return from(eventType, user, user, payload);
    }

    public static UserEvent fromTarget(EventType eventType, User user, User targetUser) {
        return from(eventType, user, targetUser, null);
    }

    public static UserEvent from(EventType eventType, User sourceUser, User targetUser, Object payload) {
        return new UserEvent(eventType, sourceUser, targetUser, payload);
    }

    public enum EventType {
        NEW_STATUS, // 
        BLOCK, //
        UNBLOCK, //
        FOLLOW, //
        PROFILE_UPDATE, //
        RETWEET, //
        LIST_UPDATE, //
        LIST_CREATION, //
        LIST_DELETION, //
        LIST_MEMBER_ADDITION, //
        LIST_MEMBER_DELETION, //
        LIST_SUBSCRIPTION, // 
        LIST_UNSUBSCRIPTION
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
