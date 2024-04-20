package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.classes.*;
import java.util.Comparator;

public class Descending extends Command {
	public Descending() {
		super("print_field_descending_character", "вывести значения поля character всех элементов в порядке убывания", 1);
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }		
    		/*
		String[] response = new String[collectiondata.getDragons().size()];
		for (int i = collectiondata.getDragons().size() - 1; i >= 0; i--) {
			response[i] = collectiondata.getDragons().get(i).getCharacter().toString();
		}
		*/
		String[] response = collectiondata.getDragons().stream().map(Dragon::getCharacter).sorted(new CharComparator()).map(ch -> ch.toString()).toArray(String[]::new);
		return new Response(response);
	}
}

class CharComparator implements Comparator<DragonCharacter> {
	public int compare(DragonCharacter a, DragonCharacter b) {
		return b.compareTo(a);
	}
}
