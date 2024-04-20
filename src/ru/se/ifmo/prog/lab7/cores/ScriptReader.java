package ru.se.ifmo.prog.lab7.cores;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class ScriptReader {
	public static LinkedList<CommandShallow> readCommands(String filename, CommandManager commandmanager, String login, String password) {
		FileInputStream inputStream;
		InputStreamReader reader;
		LinkedList<CommandShallow> shallows = new LinkedList<CommandShallow>();
		try {
			inputStream = new FileInputStream(filename);
			reader = new InputStreamReader(inputStream);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return shallows;
		}
		if (inputStream != null)
		{
			int temp;
			String line = "";
			String[] parameters = new String[0];
			try {
				int parametersptr = -1;
				CommandShallow shallow = new CommandShallow();
				int inputParams = 0;
				while (true) {
					temp = reader.read();
					if ((char)temp != '\n' && temp != -1) {
						line += (char)temp;
					}
					else {
						if (parametersptr == -1) {
							String[] com = line.split(" ");
							line = "";
							if (com.length > 0) {
								try {
									Command command = commandmanager.getCommand(com[0]);
									if (command != null) {
										if (command.getParameterAdvices() != null && command.getParameterAdvices().length != 0) {
											parametersptr = 0;
											inputParams = command.getParameterAdvices().length;
											parameters = new String[command.getParametersTypes().length];
											if (parameters.length != 0 && command.getParametersTypes().length != command.getParameterAdvices().length) {
												parameters[parameters.length-1] = login;
											}
										}	
										shallow = new CommandShallow(command, com, login, password);
										//Случай add или update
										shallows.addLast(shallow);
									}
								}
								catch (Exception e) {
									System.out.println(e.getMessage());
								}
							}
						}
						else {
							parameters[parametersptr] = line;
							line = "";
							parametersptr++;
							if (parametersptr >= inputParams) {
								parametersptr = -1;
								//try {
								shallow.setParameters(parameters);
									//setlogin
								/*}
								catch (ConvertationException e) {
									System.out.println(e.getMessage());
								}*/
							}
						}
						if (temp == -1) {
							break;
						}
					}
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			return shallows;
		}
		return shallows;
	}
}
