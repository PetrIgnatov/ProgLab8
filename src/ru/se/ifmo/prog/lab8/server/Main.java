package ru.se.ifmo.prog.lab8.server;

import ru.se.ifmo.prog.lab8.classes.*;
import ru.se.ifmo.prog.lab8.commands.*;
import ru.se.ifmo.prog.lab8.server.cores.*;
import ru.se.ifmo.prog.lab8.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab8.exceptions.*;
import java.util.logging.*;
import java.sql.*;
import java.io.*;

public class Main {
	public static void main(String[] args) throws InputArgumentException {
		Logger logger = Logger.getLogger(Main.class.getName());
		logger.setLevel(Level.ALL);
		logger.info("Запускаем сервер...");
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		UDPConnector.getIP(logger);
		Properties logininfo = new Properties();
		try {
			logininfo.load(new FileInputStream("db1.cfg"));
		}
		catch (Exception e) {
			System.out.println("Ошибка! Не удалось загрузить данные из файла");
			return;
		}
		DatabaseConnector DBConnector;
		try {	
			DBConnector = new DatabaseConnector(logininfo);
		}
		catch (SQLException e) {
			logger.severe(e.getMessage());
			logger.severe("Ошибка! Неверные имя пользователя или пароль");
			return;
		}
		CollectionData collection = new CollectionData();
		try {
			collection.addDragons(DBConnector.getDragons());
		}
		catch (SQLException e) {
			logger.severe("Ошибка! Не получается получить информацию о драконах");
		}	
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Сохраняем коллекцию");
			try {
				DBConnector.save(collection.getDragons());
			}
			catch (SQLException e) {
				logger.severe("Ошибка сохранения!");
			}
			}));
		CommandManager commandmanager = new CommandManager();
		String[] comnames = {"help", "info", "show", "add", "update", "remove_by_id", "clear", "save", "execute_script", "exit", "remove_at", "sort", "history", "sum_of_age", "print_field_ascending_character", "print_field_descending_character", "sign_in", "register"};
		Command[] coms = {new Help(), new Info(), new Show(), new Add(), new UpdateID(), new RemoveID(), new Clear(), new Save(), new ExecuteScript(), new Exit(), new RemoveIndex(), new Sort(), new History(), new SumOfAge(), new Ascending(), new Descending(), new SignIn(), new Register()};
		for (int i = 0; i < coms.length; ++i)
		{
			try {
				commandmanager.createCommand(comnames[i], coms[i]);
			}
			catch (CommandIOException e) {
				logger.severe(e.getMessage());
			}
		}
		logger.fine("CommandManager Initialized!");
		UDPConnector connector = new UDPConnector();
		connector.Connect(6789, logger);
		UDPSender sender = new UDPSender(connector.getDatagramSocket()); 
		UDPReader reader = new UDPReader(connector.getDatagramSocket(), collection, commandmanager, sender, DBConnector);
		reader.start(logger);
	}
}
