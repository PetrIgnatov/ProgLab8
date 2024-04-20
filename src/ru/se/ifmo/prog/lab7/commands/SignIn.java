package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;

public class SignIn extends Command {
	public SignIn() {
		super("sign_in login password", "войти в систему", 3, new String[0]);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
			return new Response(new String[0]);
		}
		String[] response = new String[] {connector.signIn(args[1], args[2])};
		return new Response(response);
	}
}
