package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;

public class Sort extends Command {
	public Sort() {
		super("sort", "отсортировать коллекцию в естественном порядке", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }
		collectiondata.sort();
		return new Response(new String[] {"Все успешно отсортировано"});
	}
}
