package ru.se.ifmo.prog.lab8.client.back.cores;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.se.ifmo.prog.lab8.exceptions.*;
import java.security.*;

public class Console implements Serializable {
	private Scanner scanner;
	private boolean active;
	private LinkedList<String> commandsStack;
	private int stacksize;
	private UDPSender sender;
	private UDPReader reader;
	private boolean authorized; 
	private String login;
	private String password;
	private String serverip;
	private int serverport;
/*
	public Console(UDPSender sender, UDPReader reader) throws NoSuchAlgorithmException {
		this.scanner = new Scanner(System.in);
		this.active = true;
		this.commandsStack = new LinkedList<String>();
		this.stacksize = 0;
		this.sender = sender;
		this.reader = reader;
		this.authorized = false;
	}
	
	public void setServer(String ip, int port) {
		this.serverip = ip;
		this.serverport = port;
	}

	public void start(UDPConnector connector, boolean auto, String scriptName) {
		boolean con = false;
		String host = "";
		int port = 0;
		while (!con) {
			con = true;
			this.print("Введите IP хоста: ");
			if (scanner.hasNextLine()) {
				host = scanner.nextLine();
				//Matcher matcher = pattern.matcher(host);
    				//con = matcher.find();
				if (con) {
					try {
						InetAddress.getByName(host);
					}
					catch (UnknownHostException e) {
						System.out.println("Неизвестный IP! Попробуйте ввести снова");
						con = false;
					}
				}
				else {
					con = false;
				}
			}
				try {
					InetAddress.getByName(serverip);
				}
				catch (UnknownHostException e) {
					System.out.println("Неизвестный IP! Попробуйте ввести снова");
					con = false;
				}
		}
		con = false;
		while (!con) {
			con = true;
			this.print("Введите порт хоста: ");
			try {
				if (scanner.hasNextLine()) {
					port = Integer.parseInt(this.scanner.nextLine());
				}
			}
			catch (IllegalArgumentException e) {
				System.out.println("Неизвестный порт! Попробуйте ввести снова");
				con = false;
			}
		}
		connector.connect(host, port);
		sender = new UDPSender(connector.getDatagramChannel(), connector.getAddress());
		reader = new UDPReader(connector.getDatagramChannel());
		int[] parametersptr = {-1};
		//CommandShallow shallow = new CommandShallow();
		String[] parameters = new String[0];
		while (this.active) {
			readCommand();	
		}
	}

	public void print(String line)
	{
		if (line.equals(null))
		{
			return;
		}
		System.out.print(line);
	}

	public void println(String line)
	{
		if (line.equals(null))
		{
			System.out.println();
			return;
		}
		System.out.println(line);
	}

	public void readCommand() {
			try {
				String[] com = scanner.nextLine().split(" ");
				StringShallow shallow = new StringShallow(com, login, password);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(shallow);
				byte[] arr = baos.toByteArray();
				sender.send(arr);
				String[] response = reader.getResponse().getMessage();
				for (String s: response) {
					if (s.equals("exit")) {
						this.stop();
						break;
					}
					else if (s.equals("Вы успешно зашли в систему")) {
						this.authorized = true;
						this.login = com[1];
						this.password = com[2];
					}
					System.out.println(s);
				}
					
			}
			catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		//}
	}

	public String readln() {
		if (commandsStack.size() == 0) {
			stacksize = 0;
			return scanner.nextLine();
		}
		else {
			this.println(commandsStack.peek());
			return commandsStack.removeFirst();
		}
	}
	
	public void stop() {
		active = false;
	}
*/
}

