package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;

public interface Executable {
	public String getName();
	public String getDescription();
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password);
}
