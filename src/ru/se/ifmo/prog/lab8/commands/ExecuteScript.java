package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;
import java.util.Collections;
import java.util.LinkedList;

public class ExecuteScript extends Command {
	public ExecuteScript() {
		super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.", 2);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 1000) {
			return new Response(new String[0]);
		}
		LinkedList<CommandShallow> commandsList = ScriptReader.readCommands(args[1], commandmanager, login, password);
		if (commandsList.size() == 0) {
			return new Response(new String[] {"Файл не найден или пуст"});
		}
		Response response = new Response(new String[0]);
		for (CommandShallow command : commandsList) {
			String[] res = command.execute(++stacksize, commandmanager, collectiondata, connector).getMessage();
			if (res.length > 0) {
				response.addLines(res);
			}
		}
		for (int i = 0; i < response.getMessage().length; ++i) {
			System.out.println(response.getMessage()[i]);
		}
		return response;
	}
}

