package ru.se.ifmo.prog.lab8.cores;

import java.io.*;
import java.net.*;
import ru.se.ifmo.prog.lab8.classes.*;
import ru.se.ifmo.prog.lab8.exceptions.*;

public class StringShallow implements Serializable {
	private String[] command;
	private String login;
	private String password;
	
	public StringShallow() {
		this.command = null;
		this.login = null;
		this.password = null;
	}

	public StringShallow(String[] command, String login, String password) {
		this.command = command;
		this.login = login;
		this.password = password;
	}

	public String[] getCommand() {
		return command;
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public static boolean isInteger(String s) {
		if (s == null) {
			return true;
		}
		try {
			Integer.parseInt(s);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isFloat(String s) {
		if (s == null) {
			return true;
		}
		try {
			Float.parseFloat(s);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
}
