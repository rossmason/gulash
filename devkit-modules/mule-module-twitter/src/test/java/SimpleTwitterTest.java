
import org.mule.api.MuleException;

import org.mule.module.core.Mule;
import static org.mule.module.Twitter.*;
import static org.mule.module.Core.*;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by machaval on 3/7/14.
 */
public class SimpleTwitterTest
{

    @Test
    @Ignore
    public void testTwitterChangeStatus() throws MuleException
    {
        Mule mule = new Mule();
        mule.declare(twitterConfig("", "")
                             .accessKey("")
                             .accessSecret("").as("twitter"));
        mule.declare(flow("TestTwitter")
                             .on(endpoint("http://localhost:8081"))
                             .then(updateStatus("Simple Test").with("twitter")));
        mule.start();
    }

    public static void main(String[] args) throws MuleException
    {
        final Mule mule = new Mule();
        String consumerKey = "";
        String consumerSecret = "";
        String accessKey = "";
        String accessSecret = "";
        mule.declare(twitterConfig(consumerKey, consumerSecret)
                             .accessKey(accessKey)
                             .accessSecret(accessSecret).as("twitter"));
        mule.declare(flow("TestTwitter")
                             .on(endpoint("http://localhost:8081"))
                             .then(updateStatus("#['me la como y que']").with("twitter"))
                             .then(setPayload("OK")));
        mule.start();
    }

}
