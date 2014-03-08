
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
    public void testTwitterChangeStatus()
    {
        Mule mule = new Mule();
        mule.declare(twitterConfig("","")
                             .consumerKey("")
                             .consumerSecret("")
                             .accessKey("")
                             .accessSecret("").as("twitter"));
        mule.declare(flow("TestTwitter")
                             .on(endpoint("http://localhost:8081"))
                             .then(updateStatus("#[payload]")));
    }

}
