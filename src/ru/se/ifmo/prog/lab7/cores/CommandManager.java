package ru.se.ifmo.prog.lab7.cores;

import java.util.Map;
import java.util.HashMap;
import ru.se.ifmo.prog.lab7.commands.*;
import java.io.Serializable;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class CommandManager implements Serializable {
	private HashMap<String, Command> commandList;
	
	public CommandManager() {
		this.commandList = new HashMap<String, Command>();	
	}
	
	public CommandManager(HashMap<String, Command> commandList) {
		this.commandList = commandList;
	}

	public void createCommand(String name, Command command) throws CommandIOException {
		if (name.equals(null) || name.equals("^\s*$")) {
			throw new CommandIOException("Error! Can't create command with name \"" + name + "\"");
		}
		commandList.put(name, command);
	}

	public String getCommands() {
		String commands = "";
		for (Map.Entry<String, Command> val : commandList.entrySet()) {
			commands += val.getValue().getName() + " : " + val.getValue().getDescription() + "\n";
		}
		return commands;
	}

	public Command getCommand(String name) throws CommandIOException{
		if (commandList.containsKey(name)) {
			return commandList.get(name);
		}
		return null;
	}
}

