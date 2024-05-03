package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;
import java.util.stream.*;

public class Show extends Command {
	public Show() {
		super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
			return new Response(new String[0]);
		}
		//String[] response = collectiondata.getDragons().stream().map(dr -> dr.toString()).toArray(String[]::new);
		return new Response(collectiondata.dragonsStringArr());
	}
}
