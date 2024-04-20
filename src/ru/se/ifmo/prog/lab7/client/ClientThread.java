package ru.se.ifmo.prog.lab7.client;

import java.nio.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Callable;
import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.commands.*;
import ru.se.ifmo.prog.lab7.client.cores.*;
import java.util.*;
import ru.se.ifmo.prog.lab7.exceptions.*;

public class ClientThread implements Runnable {
        private boolean auto;
	private int j;
	private String ip;
	private int port;

        public ClientThread(boolean auto, int j, String ip, int port) {
                this.auto = auto;
		this.j = j;
		this.ip = ip;
		this.port = port;
        }
        @Override
        public void run() {
        	CommandManager commandmanager = new CommandManager();
		UDPConnector connector = new UDPConnector();
		UDPSender sender = new UDPSender(); 
		UDPReader reader = new UDPReader();
		try {
			Console console = new Console(commandmanager, sender, reader);
			console.setServer(ip, port);
			String[] comnames = {"help", "info", "show", "add", "update", "remove_by_id", "clear", "save", "execute_script", "exit", "remove_at", "sort", "history", "sum_of_age", "print_field_ascending_character", "print_field_descending_character", "sign_in", "register"};
			Command[] coms = {new Help(), new Info(), new Show(), new Add(), new UpdateID(), new RemoveID(), new Clear(), new Save(), new ExecuteScript(), new Exit(), new RemoveIndex(), new Sort(), new History(), new SumOfAge(), new Ascending(), new Descending(), new SignIn(), new Register()};
			for (int i = 0; i < coms.length; ++i)
			{
				try {
					commandmanager.createCommand(comnames[i], coms[i]);
				}
				catch (CommandIOException e) {
					System.out.println(e.getMessage());
				}
			}	
			 console.start(connector, auto, "script" + j + ".gl");
			 System.out.println("Thread finished");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Фатальная ошибка! Дайте программисту по горбу");
		}
        }
}

