package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;
import java.util.stream.*;

public class SumOfAge extends Command {
	public SumOfAge() {
		super("sum_of_age", "вывести сумму значений поля age для всех элементов коллекции", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
			return new Response(new String[0]);
		}
		//String[] response = {Integer.toString(collectiondata.sumAge())};
		String[] response = {Integer.toString(collectiondata.getDragons().stream().mapToInt(Dragon::getAge).sum())};
		return new Response(response);
	}
}
