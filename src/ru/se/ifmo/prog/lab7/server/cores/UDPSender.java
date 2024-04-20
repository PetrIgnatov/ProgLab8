package ru.se.ifmo.prog.lab7.server.cores;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.awt.event.*;
import java.util.Iterator;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.server.threads.*;
import java.util.logging.*;
import java.util.concurrent.ForkJoinPool;

public class UDPSender {
	private DatagramSocket datagramSocket;
	private ForkJoinPool forkJoinPool;

	public UDPSender(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
		this.forkJoinPool = new ForkJoinPool();
	}

	public void send(Response response, InetAddress address, int port, Logger logger) {
		if (!forkJoinPool.invoke(new SendThread(response, datagramSocket, address, port))) {
			logger.severe("Error sending response!");
		}
		else {
			logger.fine("Sent response successfully!");
		}
	}
}
