package ru.se.ifmo.prog.lab8.client.back;

import ru.se.ifmo.prog.lab8.classes.*;
import ru.se.ifmo.prog.lab8.commands.*;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.client.back.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab8.exceptions.*;
import java.io.*;
import ru.se.ifmo.prog.lab8.client.back.threads.*;
import java.util.concurrent.*;

public class Main {
	private static UDPConnector connector;
	private static UDPSender sender;
	private static UDPReader reader;
	private static ReadThread readThread;

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
	public static boolean connect(String host, String port) {
		connector = new UDPConnector();
		int iPort = 0;
		try {
			iPort = Integer.parseInt(port);
		}
		catch (Exception e) {
			System.out.println("Ошибка! Порт должен быть числом");
			return false;
		}
                connector.connect(host, iPort);
                sender = new UDPSender(connector.getDatagramChannel(), connector.getAddress());
                reader = new UDPReader(connector.getDatagramChannel());
		try {
			StringShallow shallow = new StringShallow(new String[]{"connect"}, "", "");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage();
			if (response.length > 0 && response[0].equals("success!")) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static boolean register(String login, String password) {
		try {
			StringShallow shallow = new StringShallow(new String[]{"register", login, password}, login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage();
			if (response.length > 0 && response[0].equals("Вы успешно зашли в систему")) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static boolean signIn(String login, String password) {
		try {
			StringShallow shallow = new StringShallow(new String[]{"sign_in", login, password}, "", "");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage();
			if (response.length > 0 && response[0].equals("Вы успешно зашли в систему")) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static String[][] getDragons(String login, String password) {
		try {
			StringShallow shallow = new StringShallow(new String[]{"show"}, login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage(); 
			String[][] ans = new String[response.length][11];
			for (int i = 0; i < response.length; ++i) {
				String[] spl = response[i].split(";");
				if (spl.length != 11) {
					return null;
				}
				for (int j = 0; j < 11; ++j) {
					ans[i][j] = spl[j];
				}
			}
			/*
			if (response.length > 0 && response[0].equals("Вы успешно зашли в систему")) {
				return true;
			}
			return false;*/
			System.out.println(response.length);
			return ans;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static String sendCommand(String com, String login, String password) {
		try {
			System.out.println("Sending command");
			StringShallow shallow = new StringShallow(com.split(" "), login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage();
			String resp = "";
			for (int i = 0; i < response.length; ++i) {
				resp += response[i];
			}
			return resp;
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}

	public static boolean remove(String id, String login, String password) {
		try {
			StringShallow shallow = new StringShallow(new String[]{"remove_by_id", id}, login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static boolean change(String id, String[] params, String login, String password) {
		try {
			{
				StringShallow shallow = new StringShallow(new String[]{"update", id}, login, password);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(shallow);
				byte[] arr = baos.toByteArray();
				sender.send(arr);
				String[] response = reader.getResponse(false).getMessage();
				System.out.println(response[0]);
			}
			for (int i = 0; i < params.length; ++i) {
				StringShallow shallow = new StringShallow(new String[]{params[i]}, login, password);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeObject(shallow);
                                byte[] arr = baos.toByteArray();
                                sender.send(arr);
                                String[] response = reader.getResponse(false).getMessage();
                                System.out.println(response[0]);
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static boolean getMsg() {
		String[] response = reader.getResponse(true).getMessage();
		System.out.println("Got response!");
		for (String r : response) {
			System.out.println(r);
		}
		return true;
	}
}
