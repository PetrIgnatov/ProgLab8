package ru.se.ifmo.prog.lab8.client.back.cores;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import java.nio.channels.*;

public class UDPSender {
	private DatagramChannel datagramChannel;
	private ByteBuffer buffer;
	private SocketAddress hostAddress;

	public UDPSender()
	{
		this.datagramChannel = null;
		this.hostAddress = null;
	}

	public UDPSender(DatagramChannel datagramChannel, SocketAddress hostAddress) {
		this.datagramChannel = datagramChannel;
		this.hostAddress = hostAddress;
	}

	public void send(byte[] arr) throws IOException {
		buffer = ByteBuffer.wrap(arr);
		datagramChannel.send(buffer, hostAddress);
	}
}
