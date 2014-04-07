
import org.mule.api.MuleException;

import org.mule.module.core.Mule;
import static org.mule.module.Twitter.*;
import static org.mule.module.Core.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
                             .then(updateStatus("Simple Test").using("twitter")));
        mule.start();
    }

    public static void main(String[] args) throws MuleException, FileNotFoundException
    {
        final Mule mule = new Mule();

        String consumerKey = "KWAiPYoPy60GYJSKYm5PuQ";
        String consumerSecret = "Ag5VKkwbnoOiHBQ2Rh5J1sQdAh35YbeYVimqqMYp2E";
        String accessKey = "2377668061-bgaWKawaIacFA2JNkXCA0jEyanUr3Vuzn7GRpbi";
        String accessSecret = "prK4R3515VHu9PpD1hZyBoTz152H2tPs7k2jJNtXnvKyE";

        mule.declare(twitterConfig(consumerKey, consumerSecret)
                             .accessKey(accessKey)
                             .accessSecret(accessSecret).as("twitter"));

        mule.declare(flow("TestTwitter")
                             .on(endpoint("http://localhost:8081"))
                             .then(updateStatus("Hello #mule_light")
                                           .mediaName("Test")
                                           .media(new FileInputStream("/Users/machaval/Downloads/logo.png"))
                                           .using("twitter"))
                             .then(setPayload("OK")));
        mule.start();
    }

}
