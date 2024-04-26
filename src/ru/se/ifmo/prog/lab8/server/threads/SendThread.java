package ru.se.ifmo.prog.lab8.server.threads;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.RecursiveTask;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.commands.*;

public class SendThread extends RecursiveTask<Boolean> {
        private Response response;
	private InetAddress address;
	private int port;
	private DatagramSocket datagramSocket;

        public SendThread(Response response, DatagramSocket datagramSocket, InetAddress address, int port) {
                this.datagramSocket = datagramSocket;
		this.address = address;
		this.port = port;
		this.response = response;
        }
        @Override
        public Boolean compute() {
                try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(response);
                        byte[] arr = baos.toByteArray();
                        DatagramPacket datagramPacket = new DatagramPacket(arr, arr.length, address, port);
                        datagramSocket.send(datagramPacket);
                        return true;
                }
                catch (Exception e) {
			System.out.println(e.getMessage());
                        return false;
                }
        }
}

