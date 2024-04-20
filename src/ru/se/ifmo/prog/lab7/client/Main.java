package ru.se.ifmo.prog.lab7.client;

import ru.se.ifmo.prog.lab7.classes.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.client.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class Main {
	public static void main(String[] args) throws InputArgumentException {
		boolean auto = false;
		if (!(args.length == 0 || args.length == 3)) {
			throw new InputArgumentException("Error! Got " + Integer.valueOf(args.length) + " arguments when 0 required");
		}
		if (args.length >= 1) {
			auto = true;
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nВыключаем клиент");
			}));
		if (!auto) {
		CommandManager commandmanager = new CommandManager();
		UDPConnector connector = new UDPConnector();
		UDPSender sender = new UDPSender(); 
		UDPReader reader = new UDPReader();
		try {
			Console console = new Console(commandmanager, sender, reader);	
			/*String[] comnames = {"help", "info", "show", "add", "update", "remove_by_id", "clear", "save", "execute_script", "exit", "remove_at", "sort", "history", "sum_of_age", "print_field_ascending_character", "print_field_descending_character", "sign_in", "register"};
			Command[] coms = {new Help(), new Info(), new Show(), new Add(), new UpdateID(), new RemoveID(), new Clear(), new Save(), new ExecuteScript(), new Exit(), new RemoveIndex(), new Sort(), new History(), new SumOfAge(), new Ascending(), new Descending(), new SignIn(), new Register()};
			for (int i = 0; i < coms.length; ++i)
			{
				try {
					commandmanager.createCommand(comnames[i], coms[i]);
				}
				catch (CommandIOException e) {
					System.out.println(e.getMessage());
				}
			}	*/
			console.start(connector, auto, null);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Фатальная ошибка! Дайте программисту по горбу");
		}
		}
		else {
			for (int j = 0; j <= 19; ++j) {
				System.out.println("Creating thread " + j);
				Runnable r = new ClientThread(auto, j%3+1, args[1], Integer.parseInt(args[2]));
				Thread thread = new Thread(r, "new thread");
				thread.start();
			}
		}
	}
}
