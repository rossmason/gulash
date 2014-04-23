package org.mule.module.webcam;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by machaval on 4/22/14.
 */
public class WebcamModuleTest
{

    @Test
    @Ignore
    public void testWebcam() throws IOException
    {
        final WebcamModule webcamModule = new WebcamModule();
        webcamModule.onStart();
        final InputStream inputStream = webcamModule.takePicture();
        webcamModule.onStop();
    }


}
