package ru.se.ifmo.prog.lab7.commands;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collections;

public class Response implements Serializable {
	private LinkedList<String> message;

	public Response() {
		message = new LinkedList<String>();
	}

	public Response(String[] messageArr) {
		message = new LinkedList<String>();
		for (int i = 0; i < messageArr.length; ++i) {
			if (messageArr[i] != "") {
				message.addLast(messageArr[i]);
			}
		}
	}

	public String[] getMessage() {
		String[] messageArr = new String[message.size()];
		for (int i = 0; i < messageArr.length; ++i) {
			messageArr[i] = message.get(i);
		}
		return messageArr;
	}

	public void addLine(String line) {
		message.addLast(line);
	}

	public void addLines(String[] lines) {
		for (String s : lines) {
			if (!s.equals(null)) {
				message.addLast(s);
			}
		}
	}

	public void addLines(LinkedList<String> lines) {
		for (String s : lines) {
			if (!s.equals(null)) {
				message.addLast(s);
			}
		}
	}
}
