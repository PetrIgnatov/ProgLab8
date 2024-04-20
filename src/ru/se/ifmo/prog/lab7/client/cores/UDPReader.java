package ru.se.ifmo.prog.lab7.client.cores;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import java.nio.channels.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;

public class UDPReader {
	private DatagramChannel datagramChannel;
	private ByteBuffer buffer;
	private byte[] arr;

	public UDPReader()
	{
		this.datagramChannel = null;
		arr = new byte[10000];
		buffer = ByteBuffer.wrap(arr);
	}

	public UDPReader(DatagramChannel datagramChannel) {
		this.datagramChannel = datagramChannel;
		arr = new byte[10000];
		buffer = ByteBuffer.wrap(arr);
	}

	public Response getResponse() {
		int iter = 0;
		try {
			arr = new byte[10000];
			buffer = ByteBuffer.wrap(arr);
			buffer.clear();
			boolean ok = false;
			while (!ok) {
				try {
					++iter;
					ok = true;
					datagramChannel.receive(buffer);
					ByteArrayInputStream bis = new ByteArrayInputStream(arr);
					ObjectInput in = new ObjectInputStream(bis);
					Response response = (Response)in.readObject();	
					return response;
				}
				catch (StreamCorruptedException e) {
					ok = false;
					if (iter >= 10000) {
						System.out.println("Сервер недоступен, попробуйте позже");
						return new Response(new String[0]);
					}
				}
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return new Response(new String[0]);	
	}
}
