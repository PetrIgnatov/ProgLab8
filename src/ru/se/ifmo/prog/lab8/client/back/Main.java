package ru.se.ifmo.prog.lab8.client.back;

import ru.se.ifmo.prog.lab8.classes.*;
import ru.se.ifmo.prog.lab8.commands.*;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.client.back.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab8.exceptions.*;
import java.io.*;

public class Main {
	private static UDPConnector connector;
	private static UDPSender sender;
	private static UDPReader reader;

	static {
                UDPConnector connector = new UDPConnector();
                UDPSender sender = new UDPSender();
                UDPReader reader = new UDPReader();
	}
	/*public static void main() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nВыключаем клиент");
			}));
		CommandManager commandmanager = new CommandManager();
		UDPConnector connector = new UDPConnector();
		UDPSender sender = new UDPSender(); 
		UDPReader reader = new UDPReader();
		try {
			Console console = new Console(sender, reader);	
			console.start(connector, false, null);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Фатальная ошибка! Дайте программисту по горбу");
		}
	}*/
	public static boolean connect(String host) {
		connector = new UDPConnector();
                connector.connect(host, 6789);
                sender = new UDPSender(connector.getDatagramChannel(), connector.getAddress());
                reader = new UDPReader(connector.getDatagramChannel());
		try {
			StringShallow shallow = new StringShallow(new String[]{"connect"}, "", "");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse().getMessage();
			if (response.length > 0 && response[0].equals("success!")) {
				System.out.println(response[0]);
				return true;
			}
			return false;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
