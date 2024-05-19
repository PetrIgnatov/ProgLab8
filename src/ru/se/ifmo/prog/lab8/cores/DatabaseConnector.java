package ru.se.ifmo.prog.lab8.cores;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.Collections.*;
import java.util.LinkedList;
import ru.se.ifmo.prog.lab8.classes.*;
import java.security.*;

public class DatabaseConnector {
	private Connection connection;
	private String pepper;
	private Random random;

	public DatabaseConnector() {
		connection = null;
		pepper = "63N3R470R";
		random = new Random();
	}

	public DatabaseConnector(String name, String password) throws SQLException, SQLTimeoutException {
		connection = DriverManager.getConnection("jdbc:postgresql://217.66.152.184:5432/studs", name, password);
		pepper = "63N3R470R";
		random = new Random();
	}

	public DatabaseConnector(Properties logininfo) throws SQLException, SQLTimeoutException {
		connection = DriverManager.getConnection("jdbc:postgresql://172.28.18.134:5432/proglab7", logininfo);
		pepper = "63N3R470R";
		random = new Random();
	}	

	public LinkedList<String> getDragons() throws SQLException {
		Statement state = connection.createStatement();
		ResultSet rs = state.executeQuery("SELECT * FROM DRAGONS;");
		LinkedList<String> dragons = new LinkedList<String>();
		while (rs.next()) {
			int id = rs.getInt(1);
			String name = rs.getString(2);
			int x = rs.getInt(3);
			float y = rs.getFloat(4);
			String creationDate = rs.getString(5);
			int age = rs.getInt(6);
			String color = rs.getString(7);
			String type = rs.getString(8);
			String character = rs.getString(9);
			double depth = rs.getDouble(10);
			float numberOfTreasures = rs.getFloat(11);
			String login = rs.getString(12);
			dragons.addLast(id + ";" + name + ";" + x + ";" + y + ";" + creationDate + ";" + age + ";" + color + ";" + type + ";" + character + ";" + depth + ";" + numberOfTreasures + ";" + login);
		}
		return dragons;
	}
	
	public void clear() throws SQLException {
		Statement state = connection.createStatement();
                int res = state.executeUpdate("DELETE FROM DRAGONS;");
	}

	public void save(LinkedList<Dragon> dragons) throws SQLException {
		clear();
		PreparedStatement ps = connection.prepareStatement("INSERT INTO DRAGONS(id, name, x, y, creationDate, age, color, type, character, depth, numberOfTreasures, login) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = DRAGONS.name, x = DRAGONS.x, y = DRAGONS.y, age = DRAGONS.age, color = DRAGONS.color, type = DRAGONS.type, character = DRAGONS.character, depth = DRAGONS.depth, numberOfTreasures = DRAGONS.numberOfTreasures;");
		for (Dragon dragon : dragons) {
			ps.setInt(1, dragon.getId());
		        ps.setString(2, (dragon.getName() != null ? dragon.getName() : "NULL"));
			ps.setInt(3, dragon.getCoordinates().getXPtr());
			ps.setFloat(4, dragon.getCoordinates().getYPtr());
			ps.setTimestamp(5, new java.sql.Timestamp(dragon.getDate().getTime()));
			ps.setInt(6, dragon.getAge());
			ps.setObject(7, (dragon.getColorStr() != null ? dragon.getColorStr() : "NULL"), Types.OTHER);
			ps.setObject(8, (dragon.getTypeStr() != null ? dragon.getTypeStr() : "NULL"), Types.OTHER);
			ps.setObject(9, (dragon.getCharacterStr() != null ? dragon.getCharacterStr() : "NULL"), Types.OTHER);
			ps.setDouble(10, dragon.getCave().getDepthPtr());
			ps.setFloat(11, dragon.getCave().getNumberOfTreasuresPtr());
			ps.setString(12, dragon.getOwner());
			int result = ps.executeUpdate();
		}
	}

	public String signIn(String login, String password) {
		try {
			Statement state = connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT * FROM USERS WHERE name = '" + login + "';");
			int cnt = 0;
			boolean ok = false;
			while (rs.next()) {
				++cnt;
				String pword = rs.getString(2);
				MessageDigest md = MessageDigest.getInstance("MD2");
				String codedpassword = rs.getString(3)+password+pepper;
				for (int i = 0; i < rs.getInt(4); ++i) {
					codedpassword = new String(md.digest(codedpassword.getBytes("UTF-8")));
				}
				if (pword.equals(codedpassword)) {
					ok = true;
				}
			}
			if (cnt < 1) {
				return "Пользователь с таким логином не найден! Попробуйте снова";
			}
			else if (!ok) {
				return "Неправильный пароль! Попробуйте снова";
			}
			return "Вы успешно зашли в систему";
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return "Ошибка! Попробуйте связаться с администратором или зайти в систему позже";
		}
	}

	public String register(String login, String password) {
		try {
			Statement state = connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT * FROM USERS WHERE name = '" + login + "';");
			int cnt = 0;
			while (rs.next()) {
				++cnt;
			}
			if (cnt > 0) {
				return "Пользователь с таким логином уже существует! Придумайте новый";
			}
			StringBuilder randomString = new StringBuilder();
			for (int i = 0; i < random.nextInt(5)+5; ++i) {
				randomString.append((char)random.nextInt(255));
			}
			String salt = randomString.toString();
			String codedpassword = salt+password+pepper;
			int repeats = 1+random.nextInt(3);
			MessageDigest md = MessageDigest.getInstance("MD2");
			for (int i = 0; i < repeats; ++i) {
				codedpassword = new String(md.digest(codedpassword.getBytes("UTF-8")));
			}
			PreparedStatement ps = connection.prepareStatement("INSERT INTO USERS(name, password, salt, repeats) VALUES (?, ?, ?, ?);");
			ps.setString(1, login);
			ps.setString(2, codedpassword);
			ps.setString(3, salt);
			ps.setInt(4, repeats);
			int res = ps.executeUpdate();
			return "Вы успешно зашли в систему";
		}
		catch (Exception e) {
			return "Ошибка! Попробуйте связаться с администратором или зайти в систему позже";
		}
	}
}
