package ru.se.ifmo.prog.lab8.server.threads;

import java.util.concurrent.locks.ReentrantLock;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.commands.*;
import java.net.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class RequestThread implements Callable<Response> {
	private ReentrantLock locker;
	private CollectionData data;
	private CommandShallow shallow;
        private HashMap<String, LinkedList<Command>> histories;
	private InetAddress address;
	private CommandManager commandmanager;
	private DatabaseConnector connector;

	public RequestThread(ReentrantLock locker, CollectionData data, CommandShallow shallow, HashMap<String, LinkedList<Command>> histories, InetAddress address, CommandManager commandmanager, DatabaseConnector connector) {
		this.data = data;
		this.locker = locker;
		this.shallow = shallow;
		this.histories = histories;
		this.address = address;
		this.commandmanager = commandmanager;
		this.connector = connector;
	}
	@Override
	public Response call() {
		locker.lock();
		Response response = new Response();
		try {
			if (shallow.getLogin() == null) {
				if (!histories.containsKey(shallow.getLogin())) {
        	                        histories.put(shallow.getLogin(), new LinkedList<Command>());
	                        }
                        	histories.get(shallow.getLogin()).addLast(shallow.getCommand());
                	        if (histories.get(shallow.getLogin()).size() > 5) {
        	                        histories.get(shallow.getLogin()).removeFirst();
	                        }
			}
                        if (!shallow.getCommand().getName().equals("history")) {
                                Integer stacksize = 0;
                                response = shallow.execute(stacksize, commandmanager, data, connector);
                        }
                        else {
                                String[] history = new String[histories.get(shallow.getLogin()).size()];
                                for (int i = 0; i < history.length; ++i) {
                                        history[i] = histories.get(address).get(i).getName();
                                }
                                response = new Response(history);
                        }
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			locker.unlock();
			return response;
		}
	}
}
