package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;

public class Info extends Command {
	public Info() {
		super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }
		Response response = new Response(new String[]{collectiondata.toString()});
    return response;
	}
}
