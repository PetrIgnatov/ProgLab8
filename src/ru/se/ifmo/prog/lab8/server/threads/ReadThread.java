package ru.se.ifmo.prog.lab8.server.threads;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Callable;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.commands.*;

public class ReadThread implements Callable<DatagramPacket> {
	private DatagramSocket datagramSocket;
	private byte[] arr;
	private DatagramPacket datagramPacket;

	public ReadThread(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
		arr = new byte[10000];
		datagramPacket = new DatagramPacket(arr, arr.length);
	}
	@Override
	public DatagramPacket call() {
		try {
			datagramSocket.receive(datagramPacket);	
			return datagramPacket;
		}
		catch (Exception e) {
			return null;
		}
	}
}
