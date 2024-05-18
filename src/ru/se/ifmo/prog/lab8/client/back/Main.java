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
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashSet;
import ru.se.ifmo.prog.lab8.client.back.localization.*;
import java.util.ListResourceBundle;
import java.text.*;
/*class ConnectionGUILabels_ru_RU extends ListResourceBundle {
        private Object[][] contents = {{"IP","IP: "}, {"PORT", "ПОРТ: "}, {"LOGIN","ЛОГИН: "},{"PASSWORD","ПАРОЛЬ: "}, {"CONFIRM", "ПОДТВЕРДИТЬ"}, {"REGISTER", "ЗАРЕГИСТРИРОВАТЬСЯ"}, {"SIGN IN", "ВОЙТИ"}, {"SISUCCESS", "ВЫ УСПЕШНО ВОШЛИ!"}, {"SIERROR","ПОПРОБУЙТЕ ДРУГОЙ ЛОГИН ИЛИ ПАРОЛЬ"}, {"REGSUCCESS","ВЫ УСПЕШНО ЗАРЕГИСТРИРОВАЛИСЬ"}, {"REGERROR","ПОПРОБУЙТЕ ДРУГОЙ ЛОГИН ИЛИ ПАРОЛЬ"}, {"CONSUCCESS", "ПОДКЛЮЧЕНИЕ ПРОШЛО УСПЕШНО!"}, {"CONERROR", "НЕ УДАЛОСЬ ПОДКЛЮЧИТЬСЯ! ПОПРОБУЙТЕ ДРУГОЙ IP ИЛИ ПОРТ!"}};
        @Override
        public Object[][] getContents() { return contents; }
}*/
 
public class Main {
	private static UDPConnector connector;
	private static UDPSender sender;
	private static UDPReader reader;
	private static ReadThread readThread;
	private static int sortField;
	private static ArrayList<HashSet<String>> filter;
	private static ResourceBundle r;
	private static Locale loc;
	private static String[] connectionKey = {"IP", "PORT", "LOGIN", "PASSWORD", "CONFIRM", "REGISTER", "SIGN IN", "SISUCCESS", "SIERROR", "REGSUCCESS", "REGERROR", "CONSUCCESS", "CONERROR"};
	private static String[] mainpageKey = {"TRY COMMANDS", "REFRESH", "EXIT", "SORT", "FILTER", "SAVE", "DEL", "ID", "NAME", "X", "Y", "CREATION DATE", "AGE", "COLOR", "TYPE", "CHARACTER", "DEPTH", "NUMBER OF TREASURES", "SEND", "ADD", "CHERR", "DELERR", "ADDERR"};
	private static Locale[] supportedLocales = {
		new Locale("ru", "RU"),
		new Locale("es", "CO"),
		new Locale("hr"),
		new Locale("sk")};

	static {
                UDPConnector connector = new UDPConnector();
                UDPSender sender = new UDPSender();
                UDPReader reader = new UDPReader();
		sortField = 0;
		filter = new ArrayList<HashSet<String>>();
		for (int i = 0; i < 11; ++i) {
			filter.add(new HashSet<String>());
		}
		loc = supportedLocales[0];
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
	public static void main(String[] args) {
		System.out.println("Hello");
	}

	public static String[] getConnectionText() {
		r = ResourceBundle.getBundle("ru.se.ifmo.prog.lab8.client.back.localization.ConnectionGUILabels", loc);
		String[] s = new String[connectionKey.length];
		for (int i = 0; i < s.length; ++i) {
			s[i] = r.getString(connectionKey[i]);
		}
		return s;
	}

	public static String[] getMainText() {
		r = ResourceBundle.getBundle("ru.se.ifmo.prog.lab8.client.back.localization.MainPageLabels", loc);
		String[] s = new String[mainpageKey.length];
		for (int i = 0; i < s.length; ++i) {
			s[i] = r.getString(mainpageKey[i]);
		}
		return s;
	}

	
	public static void setLanguage(int l) {
		loc = supportedLocales[l];
	}

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

	public static String[][] getAllDragons(String login, String password) {
		try {
			StringShallow shallow = new StringShallow(new String[]{"show"}, login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage(); 
			NumberFormat nf = NumberFormat.getNumberInstance();
			DateFormat to = DateFormat.getDateInstance(DateFormat.FULL);
			String[][] ans = new String[response.length][12];
			for (int i = 0; i < response.length; ++i) {
				String[] spl = response[i].split(";");
				if (spl.length != 12) {
					return null;
				}
				for (int j = 0; j < 12; ++j) {
					ans[i][j] = spl[j];
				}
			       	ans[i][0] = nf.format(Integer.parseInt(ans[i][0]));
				ans[i][2] = nf.format(Integer.parseInt(ans[i][2]));
				ans[i][3] = nf.format(Float.parseFloat(ans[i][3]));
				ans[i][4] = LocalDateTime.parse(ans[i][4]).atZone(
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

	public static String[][] getDragons(String login, String password) {
		try {
			StringShallow shallow = new StringShallow(new String[]{"show"}, login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage(); 
			String[][] ans = new String[response.length][12];
			for (int i = 0; i < response.length; ++i) {
				String[] spl = response[i].split(";");
				if (spl.length != 12) {
					return null;
				}
				for (int j = 0; j < 12; ++j) {
					ans[i][j] = spl[j];
				}
			}
			/*
			if (response.length > 0 && response[0].equals("Вы успешно зашли в систему")) {
				return true;
			}
			return false;*/
			System.out.println(response.length);
			String[][] res = Arrays.stream(ans).sorted((row1, row2) -> row1[sortField].compareTo(row2[sortField])).filter(row -> {
				System.out.println("Columns count " + row.length);
				for (int i = 0; i < Math.min(row.length, 11); ++i) {
					System.out.println("Column " + i + " " + row[i]);
					if (filter.get(i).size() == 0) {
						continue;
					}
					if (!filter.get(i).contains(row[i])) {
						for (String s: filter.get(i)) {
							System.out.println("Filter " + s);
						}
						System.out.println("Failed to find " + row[i]);
						return false;
					}
					else {
						System.out.println("Checked " + i + " " + row[i]);
					}
				}
				return true;
			}).toArray(String[][]::new);
			/*
			for (int i = 0; i < res.length; ++i) {
				for (int j = 0; j < res[i].length; ++j) {
					System.out.println(res[i][j]);
				}
			}
			*/
			return res;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static void setSort(int field) {
		sortField = field;
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
			System.out.println(id);
			StringShallow shallow = new StringShallow(new String[]{"remove_by_id", id}, login, password);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shallow);
			byte[] arr = baos.toByteArray();
			sender.send(arr);
			String[] response = reader.getResponse(false).getMessage();
			if (response.length > 0) {
				System.out.println(response[0]);
			}
			if (response.length == 1 && response[0].equals("Вы не можете удалить не своего дракона")) {
				return false;
			}
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
				System.out.println(params[i]);
				StringShallow shallow = new StringShallow(new String[]{params[i]}, login, password);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeObject(shallow);
                                byte[] arr = baos.toByteArray();
                                sender.send(arr);
                                String[] response = reader.getResponse(false).getMessage();
                                if (response.length == 1 && (response[0].equals("Неправильный аргумент, попробуйте снова") || response[0].equals("Вы не можете менять не своего дракона"))) {
					System.out.println("RETURNING FALSE");
					return false;
				}
			}
			System.out.println("VSE IDET PO PLANU");
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("VSE LETIT V PIZDU");
			return false;
		}
	}

	public static boolean create(String[] params, String login, String password) {
		try {
			{
				StringShallow shallow = new StringShallow(new String[]{"add"}, login, password);
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

	public static boolean setFilter(int n, String[] newFilter) {
		System.out.println("Filter by " + n);
		filter.set(n, new HashSet<String>());
		for (String s: newFilter) {
			System.out.println("Java filter " + s);
			filter.get(n).add(s);
		}
		return true;
	}
}
