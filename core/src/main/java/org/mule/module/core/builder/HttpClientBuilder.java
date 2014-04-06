package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.config.dsl.Builder;
import org.mule.module.core.processor.HttpClient;

import java.util.Map;

/**
 * Created by machaval on 3/21/14.
 */
public class HttpClientBuilder implements Builder<HttpClient>
{

    //Parameters
    private String baseUrl;
    private String uri;
    private String method;
    private String body;
    private Object headers;

    public HttpClientBuilder(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public HttpClientBuilder body(String body)
    {
        this.body = body;
        return this;
    }

    public HttpClientBuilder headers(Map<String, String> headers)
    {
        this.headers = headers;
        return this;
    }

    public HttpClientBuilder headers(String headersExpression)
    {
        this.headers = headersExpression;
        return this;
    }

    public HttpClientBuilder method(String method)
    {
        this.method = method;
        return this;
    }


    public HttpClientBuilder path(String path)
    {
        this.uri = path;
        return this;
    }


    @Override
    public HttpClient create(MuleContext muleContext)
    {
        final HttpClient httpClient = new HttpClient();
        httpClient.setMethod(method);
        httpClient.setBaseUrl(baseUrl);
        httpClient.setHeaders(headers);
        httpClient.setUri(uri);
        httpClient.setBody(body);
        return httpClient;
    }
}
