package ru.se.ifmo.prog.lab8.commands;

import java.io.*;
import java.net.*;
import ru.se.ifmo.prog.lab8.classes.*;
import ru.se.ifmo.prog.lab8.exceptions.*;
import ru.se.ifmo.prog.lab8.cores.*;

public class CommandShallow implements Serializable {
	private Command command;
	private String[] args;
	private Dragon dragon;
	private CommandShallow[] commands;
	private String login;
	private String password;
	private String[] params;

	public CommandShallow() {
		this.command = null;
		this.args = null;
		this.dragon = null;
		this.login = null;
		this.password = null;
		this.params = new String[0];
	}

	public CommandShallow(Command command, String[] args, String login, String password) {
		this.command = command;
		this.args = args;
		this.dragon = null;
		this.login = login;
		this.password = password;
		this.params = new String[0];
	}

	public CommandShallow(Command command, String[] args, int paramsSize, String login, String password) {
                this.command = command;
                this.args = args;
                this.dragon = null;
                this.login = login;
                this.password = password;
                this.params = new String[paramsSize];
        }
	
	public Response execute(Integer stacksize, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector) {
		try {
			this.command.check(args.length);
			return command.execute(args, stacksize, dragon, commandmanager, collectiondata, connector, params, login, password);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new Response(new String[] {e.getMessage()});
		}	
	}	

	public void setParameter(int i, String val) {
		this.params[i] = val;
	}

	public void setParameters(String[] val) {
		this.params = val;
	}

	public String[] getParameters() {
		return params;
	}

	public Command getCommand() {
		return command;
	}

	public String[] getArguments() {
		return args;
	}
	
	public void setCommands(CommandShallow[] commands) {
		this.commands = commands;
	}

	public CommandShallow[] getCommands() {
		return this.commands;
	}

	public void setDragon(String[] splitted, String login) throws ConvertationException {
		Color col = null;
		switch(splitted[4]) {
			case "GREEN":
				col = Color.GREEN;
				break;
			case "YELLOW":
				col = Color.YELLOW;
				break;
			case "ORANGE":
				col = Color.ORANGE;
				break;
			case "WHITE":
				col = Color.WHITE;
				break;
			case "":
				col = null;
				break;
			default:
				throw new ConvertationException("Error! Unknown color \"" + splitted[6] + "\"");
		}
		DragonType type = null;
		switch(splitted[5]) {
			case "WATER":
				type = DragonType.WATER;
				break;
			case "UNDERGROUND":
				type = DragonType.UNDERGROUND;
				break;
			case "AIR":
				type = DragonType.AIR;
				break;
			case "":
				type = null;
				break;
			default:
				throw new ConvertationException("Error! Unknown type \"" + splitted[7] + "\"");
		}
		DragonCharacter character = null;
		switch(splitted[6]) {
			case "EVIL":
				character = DragonCharacter.EVIL;
				break;
			case "GOOD":
				character = DragonCharacter.GOOD;
				break;
			case "CHAOTIC":
				character = DragonCharacter.CHAOTIC;
				break;
			case "FICKLE":
				character = DragonCharacter.FICKLE;
				break;
			case "CHAOTIC_EVIL":
				character = DragonCharacter.CHAOTIC_EVIL;
				break;
			case "":
				character = null;
				break;
			default:
				throw new ConvertationException("Error! Unknown character \"" + splitted[8] + "\"");
		}
		this.dragon = new Dragon(
						splitted[0] == "" ? null : splitted[0],
						splitted[1] == "" ? null : Integer.parseInt(splitted[1]),
						splitted[2] == "" ? null : Float.parseFloat(splitted[2]),
						Integer.parseInt(splitted[3]),
						col,type,character,
						splitted[7] == "" ? null : Double.parseDouble(splitted[7]),
						splitted[8] == "" ? null : Float.parseFloat(splitted[8]),
						login);
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public Dragon getDragon() {
	  return dragon;
	}
}
