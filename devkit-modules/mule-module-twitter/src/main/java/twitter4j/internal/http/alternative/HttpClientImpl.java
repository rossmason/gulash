/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package twitter4j.internal.http.alternative;

import twitter4j.TwitterException;
import twitter4j.internal.http.HttpClient;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.http.HttpResponse;


public class HttpClientImpl implements HttpClient
{
    private final HttpClient client;

    public HttpClientImpl(HttpClientConfiguration conf)
    {
        if (HttpClientHiddenConstructionArgument.useMule())
        {
            client = new MuleHttpClient(conf);
        }
        else
        {
            client = new twitter4j.internal.http.HttpClientImpl(conf);
        }
    }

    @Override
    public HttpResponse request(HttpRequest req) throws TwitterException
    {
        return client.request(req);
    }

    @Override
    public void shutdown()
    {
        client.shutdown();
    }

}
