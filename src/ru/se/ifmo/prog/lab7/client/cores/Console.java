package ru.se.ifmo.prog.lab7.client.cores;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.classes.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.se.ifmo.prog.lab7.exceptions.*;
import java.security.*;

public class Console implements Serializable {
	private Scanner scanner;
	private boolean active;
	private CommandManager commandmanager;
	private LinkedList<Command> history;
	private LinkedList<String> commandsStack;
	private int stacksize;
	private UDPSender sender;
	private UDPReader reader;
	private boolean authorized; 
	private String login;
	private String password;
	private String serverip;
	private int serverport;

	public Console(CommandManager commandmanager, UDPSender sender, UDPReader reader) throws NoSuchAlgorithmException {
		this.scanner = new Scanner(System.in);
		this.active = true;
		this.history = new LinkedList<Command>();
		this.commandmanager = commandmanager;
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
		/*Pattern pattern = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");	
		Patter ipv6pattern = Patter.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
		*/
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
		CommandShallow shallow = new CommandShallow();
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
		/*String[] com; 
		stacksize = 0;
		if (!scanner.hasNextLine()) {
			return;
		}
		com = scanner.nextLine().split(" ");
		if (com.length > 0) {
			Command command	= null;
			try {
				command = commandmanager.getCommand(com[0]);
				if (command != null && command.getName().equals("save")) {
					System.out.println("Клиент не может сохранять данные");
					return;
				}
				else if (command != null && !authorized && !command.getName().equals("exit") && !command.getName().equals("sign_in login password") && !command.getName().equals("register login password") && !command.getName().equals("help")) {
					System.out.println("Команда " + command.getName() + " недоступна неавторизованным пользователям");
					return;
				}
			}
			catch (CommandIOException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (command == null) {
					//System.out.println("Неизвестная команда!");
					return;
				}
				history.push(command);
				while (history.size() > 5) {
					history.pollLast();
				}
				CommandShallow shallow = new CommandShallow(command, com, login, password);
				if (command.getName().equals("add {element}") || command.getName().equals("update id {element}")) {
					String[] advices = command.getParameterAdvices();
					String[] parameters = new String[advices.length];
					for (int i = 0; i < advices.length; ++i) {
						this.print(advices[i]);	
						boolean ok = false;
						while (!ok) {
							parameters[i] = scanner.nextLine();
							ok = true;
							if (parameters[i].split(" ").length > 1 && i != 0) {
								System.out.println("Требуется ввести только одно значение!");
								ok = false;
							}
							if (i != 0) {
								parameters[i] = parameters[i].split(" ")[0];
							}
							if ((i == 0 || i == 1 || i == 2 || i == 3 || i == 7) && (parameters[i].equals(""))) {
								System.out.println("Переменная не может иметь значение null!");
								ok = false;
							}
							if (ok) {
								try {
									switch(i) {
										case 1: {
											int x = Integer.parseInt(parameters[i]);
											if (x <= -32) {
												ok = false;
												System.out.println("X должен быть больше -32");
											} 
										}
										break;
										case 2:
											Float.parseFloat(parameters[i]);
											break;
										case 3: {
											int x = Integer.parseInt(parameters[i]);
											if (x <= 0) {
												ok = false;
												System.out.println("Возраст дракона должен быть больше 0");
											}
										}
										break;
										case 4:
											if (!parameters[i].equals("") && !parameters[i].equals("GREEN") && !parameters[i].equals("YELLOW") && !parameters[i].equals("ORANGE") && !parameters[i].equals("WHITE")) {
												ok = false;
												System.out.println("Введено неверное значение");
											}
										break;
										case 5:
											if (!parameters[i].equals("") && !parameters[i].equals("WATER") && !parameters[i].equals("UNDERGROUND") && !parameters[i].equals("AIR")) {
												ok = false;
												System.out.println("Введено неверное значение");
											}
											break;
										case 6:
											if (!parameters[i].equals("") && !parameters[i].equals("EVIL") && !parameters[i].equals("GOOD") && !parameters[i].equals("CHAOTIC") && !parameters[i].equals("CHAOTIC_EVIL") && !parameters[i].equals("FICKLE")) {
												ok = false;
												System.out.println("Введено неверное значение");
											}
											break;
										case 7:
											Double.parseDouble(parameters[i]);
											break;
										case 8: {
											float x = Float.parseFloat(parameters[i]);
											if (x <= 0) {
												System.out.println("Количество сокровищ должно быть больше 0");
												ok = false;
											}
										}
									}
								}
								catch (Exception e) {
									System.out.println("Введено неверное значение");
									ok = false;
								}
							}
						}
					}
					try {
						shallow.setDragon(parameters, login);
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
	*/
			try {
				String[] com = scanner.nextLine().split(" ");
				StringShallow shallow = new StringShallow(com, login, password);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(shallow);
				byte[] arr = baos.toByteArray();
				sender.send(arr);
				Response response = reader.getResponse();
				for (String s: response.getMessage()) {
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

	
	private void sendCommand(CommandShallow shallow) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(shallow);
		byte[] arr = baos.toByteArray();
		sender.send(arr);
		Response response = reader.getResponse();
		for (String s: response.getMessage()) {
			if (s.equals("exit")) {
				this.stop();
				break;
			}
			System.out.println(s);
		}

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
	
	public void printHistory() {
		for (int i = history.size()-1; i >= 0; i--) {
			println(history.get(i).getName());
		}	
	}
}

