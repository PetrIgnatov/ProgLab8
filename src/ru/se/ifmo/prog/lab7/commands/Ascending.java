package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;
import java.util.stream.*;

public class Ascending extends Command {
	public Ascending() {
		super("print_field_ascending_character", "вывести значения поля character всех элементов в порядке возрастания", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }
		//String[] response = new String[collectiondata.getDragons().size()];
		//for (int i = 0; i < collectiondata.getDragons().size(); ++i) {
		//	response[i] = collectiondata.getDragons().get(i).getCharacter().toString();
		//}	
    		String[] response = collectiondata.getDragons().stream().map(Dragon::getCharacter).sorted().map(ch -> ch.toString()).toArray(String[]::new);
		return new Response(response);
	}
}
