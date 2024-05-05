package ru.se.ifmo.prog.lab8.client.back.threads;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Callable;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.commands.*;
import ru.se.ifmo.prog.lab8.client.back.cores.*;

public class ReadThread implements Callable<Response> {
	private UDPReader reader;

 	public ReadThread(UDPReader reader) {
		this.reader = reader;
	}
	
	@Override
	public Response call() {
		System.out.println("Reading..");
		return reader.getResponse(true);	
	}
}
