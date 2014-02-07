package org.mule.module.raspberrypi.rover;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

public class RaspberryPiInfo
{

    public Float getCPUTemperature()
    {
        try
        {
            return SystemInfo.getCpuTemperature();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Float getCPUVoltage()
    {
        try
        {
            return SystemInfo.getCpuVoltage();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getName()
    {
        try
        {
            return NetworkInfo.getHostname();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String[] getIpAddresses()
    {
        try
        {
            return NetworkInfo.getIPAddresses();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getIpAddress()
    {
        try
        {
            String[] ipAddresses = NetworkInfo.getIPAddresses();
            return ipAddresses.length > 0 ? ipAddresses[0] : null;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}