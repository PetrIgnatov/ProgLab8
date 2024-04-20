package ru.se.ifmo.prog.lab7.server.cores;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.awt.event.*;
import java.util.logging.*;

public class UDPConnector {
	private DatagramSocket datagramSocket;
	
	public boolean Connect(int port, Logger logger) {
		try {
			String res = null;
			datagramSocket = new DatagramSocket(port);
			String localhost = InetAddress.getLocalHost().getHostAddress();
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	
			while (e.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) e.nextElement();
            if(ni.isLoopback())
                continue;
            if(ni.isPointToPoint())
                continue;
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress address = (InetAddress) addresses.nextElement();
                if(address instanceof Inet4Address) {
                    String ip = address.getHostAddress();
                    if(!ip.equals(localhost))
                        logger.fine(res = ip);
                }
            }
			}
			logger.fine("Сервер запущен " + localhost + " с портом " + port); 
		}
		catch (Exception e) {
			logger.severe(e.getClass().getName());
			logger.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
}
