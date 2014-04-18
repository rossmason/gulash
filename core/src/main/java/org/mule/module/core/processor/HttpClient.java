package org.mule.module.core.processor;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Lifecycle;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.PropertyScope;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpConstants;
import org.mule.util.AttributeEvaluator;
import org.mule.util.concurrent.NamedThreadFactory;
import org.mule.util.concurrent.ThreadNameHelper;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Realm;
import com.ning.http.client.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.NameValuePair;
import org.jboss.netty.handler.codec.http.HttpMethod;


public class HttpClient implements MessageProcessor, MuleContextAware, Lifecycle
{

    public static final String TRANSFER_ENCODING_PROPERTY = "Transfer-Encoding";

    private AsyncHttpClient asyncHttpClient;
    private MuleContext context;

    //Parameters
    private String baseUrl;
    private String uri;
    private String method;
    private String body;
    private Object headers;

    public HttpClient()
    {
        uri = "#[message.inboundProperties['http.request']]";
        headers = "#[message.inboundProperties['http.headers']";
        method = "#[message.inboundProperties['http.method']]";
        body = "#[payload]";
    }


    @Override
    public MuleEvent process(final MuleEvent event) throws MuleException
    {
        MuleEvent result;
        final String uriEvaluated = (String) new AttributeEvaluator(uri).resolveValue(event.getMessage());
        Map<String, String> headersEvaluated = null;
        if (headers instanceof Map)
        {
            headersEvaluated = (Map<String, String>) headers;
        }
        else if (headers instanceof String)
        {
            headersEvaluated = (Map<String, String>) new AttributeEvaluator(headers.toString()).resolveValue(event.getMessage());
        }

        final String bodyEvaluated = (String) new AttributeEvaluator(body).resolveValue(event.getMessage());
        final String methodEvaluated = (String) new AttributeEvaluator(method).resolveValue(event.getMessage());

        try
        {

            final String url = baseUrl + uriEvaluated;

            AsyncHttpClient.BoundRequestBuilder requestBuilder = null;

            if (methodEvaluated.equalsIgnoreCase(HttpMethod.POST.getName()))
            {
                requestBuilder = asyncHttpClient.preparePost(url);
                requestBuilder.setBody(bodyEvaluated);
            }
            else if (methodEvaluated.equalsIgnoreCase(HttpMethod.GET.getName()))
            {
                requestBuilder = asyncHttpClient.prepareGet(url);
            }
            else if (methodEvaluated.equalsIgnoreCase(HttpMethod.PUT.getName()))
            {
                requestBuilder = asyncHttpClient.preparePut(url);
                requestBuilder.setBody(bodyEvaluated);
            }
            else if (methodEvaluated.equalsIgnoreCase(HttpMethod.OPTIONS.getName()))
            {
                requestBuilder = asyncHttpClient.prepareOptions(url);
            }
            else if (methodEvaluated.equalsIgnoreCase(HttpMethod.HEAD.getName()))
            {
                requestBuilder = asyncHttpClient.prepareHead(url);
            }
            else if (methodEvaluated.equalsIgnoreCase(HttpMethod.DELETE.getName()))
            {
                requestBuilder = asyncHttpClient.prepareDelete(url);
            }


            //todo add authentication....

            if (headersEvaluated != null)
            {
                for (Map.Entry<String, String> header : headersEvaluated.entrySet())
                {
                    requestBuilder.setHeader(header.getKey(), header.getValue());
                }
            }
            ListenableFuture<Response> execute = requestBuilder.execute();
            Response response = execute.get();
            result = new DefaultMuleEvent(handleResponse(response), event);
        }
        catch (IOException e)
        {
            throw new MessagingException(event, e, this);
        }
        catch (InterruptedException e)
        {
            throw new MessagingException(event, e, this);
        }
        catch (ExecutionException e)
        {
            throw new MessagingException(event, e, this);
        }
        return result;
    }

    private MuleMessage handleResponse(Response response) throws IOException
    {

        final MuleMessage message = new DefaultMuleMessage(response.getResponseBody(), getMuleContext());
        final FluentCaseInsensitiveStringsMap resultHeaders = response.getHeaders();
        final String contentType = response.getContentType();
        message.setProperty(HttpConnector.HTTP_HEADERS, resultHeaders, PropertyScope.INBOUND);
        message.setProperty(HttpConstants.HEADER_CONTENT_TYPE, contentType, PropertyScope.INBOUND);
        message.setProperty(HttpConnector.HTTP_STATUS_PROPERTY, response.getStatusCode(), PropertyScope.INBOUND);
        message.setEncoding(getEncoding(contentType));
        return message;
    }

    public String getEncoding(Object contentType)
    {
        String encoding = "UTF-8";
        if (contentType != null)
        {
            // use HttpClient classes to parse the charset part from the Content-Type
            // header (e.g. "text/html; charset=UTF-16BE")
            Header contentTypeHeader = new Header(HttpConstants.HEADER_CONTENT_TYPE,
                                                  contentType.toString());
            HeaderElement values[] = contentTypeHeader.getElements();
            if (values.length == 1)
            {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null)
                {
                    encoding = param.getValue();
                }
            }
        }
        return encoding;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Object getHeaders()
    {
        return headers;
    }

    public void setHeaders(Object headers)
    {
        this.headers = headers;
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public void initialise() throws InitialisationException
    {
    }

    @Override
    public void start() throws MuleException
    {
        final ExecutorService bossExecutor = Executors.newCachedThreadPool(new NamedThreadFactory(
                String.format("%s%s.boss", ThreadNameHelper.getPrefix(getMuleContext()), "NettyClient"),
                getMuleContext().getExecutionClassLoader()
        ));
        AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder()
                .setFollowRedirects(true)
                .setExecutorService(bossExecutor)
                .setKeepAlive(false)

                .build();


        asyncHttpClient = new AsyncHttpClient(cf);
    }

    @Override
    public void stop() throws MuleException
    {
    }


    @Override
    public void setMuleContext(MuleContext context)
    {
        this.context = context;
    }

    public MuleContext getMuleContext()
    {
        return context;
    }
}
