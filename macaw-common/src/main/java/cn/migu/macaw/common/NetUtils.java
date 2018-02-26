package cn.migu.macaw.common;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import com.google.common.net.InetAddresses;

/**
 * 
 * copy from apache item
 * 
 * @author soy
 */
public class NetUtils
{
    public final static String LOCAL_FLAG = "local";

    // ------------------------------------------------------------------------
    //  Lookup of to free ports
    // ------------------------------------------------------------------------
    
    /**
     * Find a non-occupied port.
     *
     * @return A non-occupied port.
     */
    public static int getAvailablePort()
    {
        for (int i = 0; i < 50; i++)
        {
            try
            {
                ServerSocket serverSocket = new ServerSocket(0);
                
                int port = serverSocket.getLocalPort();
                serverSocket.close();
                if (port != 0)
                {
                    return port;
                }
            }
            catch (IOException ignored)
            {
            }
        }
        
        throw new RuntimeException("Could not find a free permitted port on the machine.");
    }
    
    // ------------------------------------------------------------------------
    //  Encoding of IP addresses for URLs
    // ------------------------------------------------------------------------
    
    /**
     * Encodes an IP address properly as a URL string. This method makes sure that IPv6 addresses
     * have the proper formatting to be included in URLs.
     * <p>
     * This method internally uses Guava's functionality to properly encode IPv6 addresses.
     * 
     * @param address The IP address to encode.
     * @return The proper URL string encoded IP address.
     */
    public static String ipAddressToUrlString(InetAddress address)
    {
        if (address == null)
        {
            throw new NullPointerException("address is null");
        }
        else if (address instanceof Inet4Address)
        {
            return address.getHostAddress();
        }
        else if (address instanceof Inet6Address)
        {
            return '[' + InetAddresses.toAddrString(address) + ']';
        }
        else
        {
            throw new IllegalArgumentException("Unrecognized type of InetAddress: " + address);
        }
    }
    
    /**
     * Encodes an IP address and port to be included in URL. in particular, this method makes
     * sure that IPv6 addresses have the proper formatting to be included in URLs.
     * 
     * @param address The address to be included in the URL.
     * @param port The port for the URL address.
     * @return The proper URL string encoded IP address and port.
     */
    public static String ipAddressAndPortToUrlString(InetAddress address, int port)
    {
        return ipAddressToUrlString(address) + ':' + port;
    }

    
    /**
     * Normalizes and encodes a hostname and port to be included in URL. 
     * In particular, this method makes sure that IPv6 address literals have the proper
     * formatting to be included in URLs.
     *
     * @param host The address to be included in the URL.
     * @param port The port for the URL address.
     * @return The proper URL string encoded IP address and port.
     * @throws UnknownHostException Thrown, if the hostname cannot be translated into a URL.
     */
    public static String hostAndPortToUrlString(String host, int port)
        throws UnknownHostException
    {
        return ipAddressAndPortToUrlString(InetAddress.getByName(host), port);
    }
    
}
