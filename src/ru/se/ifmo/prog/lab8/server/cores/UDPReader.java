package ru.se.ifmo.prog.lab8.server.cores;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;
import java.nio.channels.*;
import java.awt.event.*;
import java.util.Iterator;
import ru.se.ifmo.prog.lab8.commands.*;
import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.exceptions.*;
import ru.se.ifmo.prog.lab8.server.threads.*;
import java.sql.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class UDPReader {
	private DatagramSocket datagramSocket;
	private boolean active;
	private ByteBuffer buffer;
	private byte arr[];
	private DatagramPacket datagramPacket;
	private CollectionData collection;
	private CommandManager commandmanager;
	private UDPSender sender;
	private HashMap<String, LinkedList<Command>> histories;
	private HashMap<String, Integer> curCommPar;
	private HashMap<String, CommandShallow> lastComm;
	private LinkedList<Command> localHistory;
	private DatabaseConnector connector;
	private boolean authorized;
	private String login;
	private String password;
	private ReentrantLock requestLock;
	private ExecutorService executor;
	private int prevThreads;
	private int connected;

	public UDPReader(DatagramSocket datagramSocket, CollectionData collection, CommandManager commandmanager, UDPSender sender, DatabaseConnector connector) {
		this.active = true;
		this.connected = 0;
		this.connector = connector;
		this.datagramSocket = datagramSocket;
		this.buffer = ByteBuffer.allocate(10000);
		this.arr = new byte[10000];
		this.collection = collection;
		this.commandmanager = commandmanager;
		this.sender = sender;
		this.histories = new HashMap<String, LinkedList<Command>>();
		this.curCommPar = new HashMap<String, Integer>();
		this.lastComm = new HashMap<String, CommandShallow>();
		this.localHistory = new LinkedList<Command>();
		this.requestLock = new ReentrantLock();
		this.executor = Executors.newFixedThreadPool(500);
		try {
			datagramSocket.setSoTimeout(100);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void start(Logger logger) {
		int parametersptr = -1;
		CommandShallow shallow = new CommandShallow();
		String[] parameters = new String[0];
		while (this.active) {
			try {
				if (System.in.available() > 0) {
					byte[] inputBuffer = new byte[System.in.available()];
					System.in.read(inputBuffer);
					String input = (new String(inputBuffer)).replaceAll("\n", "");
					if (parametersptr == -1) {
						String[] com = input.split(" ");
						if (com.length > 0) {
							try {
								Command command = commandmanager.getCommand(com[0]);
								if (command != null && !authorized && !command.getName().equals("exit") && !command.getName().equals("sign_in login password") && !command.getName().equals("register login password") && !command.getName().equals("help") && !command.getName().equals("save")) {
									System.out.println("Команда " + command.getName() + " недоступна неавторизованным пользователям");
									continue;	
								}
								if (command == null) {
									System.out.println("Неизвестная команда!");
									continue;
								}
								shallow = new CommandShallow(command, com, login, password);
								if (command.getParameterAdvices() != null && command.getParameterAdvices().length > 0) {
									parametersptr = 0;
									parameters = new String[command.getParameterAdvices().length+1];
									System.out.print(command.getParameterAdvices()[parametersptr]);
								}
								else {
									this.localExecute(shallow, logger);
								}
							}
							catch (Exception e) {
								System.out.println(e.getMessage());
							}
						}
					}
					else {
						parameters[parametersptr] = input;
						parametersptr++;
						if (parametersptr == parameters.length - 1) {
							parametersptr = -1;
							parameters[parameters.length-1] = login;
							shallow.setParameters(parameters);
							this.localExecute(shallow, logger);		
						}
						else {
							System.out.print(shallow.getCommand().getParameterAdvices()[parametersptr]);
						}
					}
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			readCommand(logger);
		}
	}
	
	private void localExecute(CommandShallow shallow, Logger logger) {
		Integer stacksize = 0;
		localHistory.addLast(shallow.getCommand());
		if (localHistory.size() > 5) {
			localHistory.removeFirst();
		}
		if (shallow.getCommand().getName().equals("history")) {
			for (Command com : localHistory) {
				System.out.println(com.getName());
			}
		}
		System.out.println("Executing");
		Response response = shallow.execute(stacksize,  commandmanager, collection, connector);
		System.out.println("Executed");
		for (String s: response.getMessage()) {
			if (s.equals("exit")) {
				this.stop();
				break;
			}
			else if (s.equals("save")) {
				try {
					connector.save(collection.getDragons());
				}
				catch (SQLException e) {
					System.out.println(e.getMessage());
					logger.severe("Ошибка сохранения!");
				}
				break;
			}
			else if (s.equals("Вы успешно зашли в систему")) {
                                                this.authorized = true;
                                                this.login = shallow.getArguments()[1];
                                                this.password = shallow.getArguments()[2];
                        }
			System.out.println(s);
		}
	}

	private void readCommand(Logger logger) {
		try {
			/*datagramPacket = new DatagramPacket(arr, arr.length);
			datagramSocket.receive(datagramPacket);
			ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
			ObjectInput in = new ObjectInputStream(bis);
			CommandShallow shallow = (CommandShallow)in.readObject(); */
			if (executor.isTerminated() || !active) {
				return;
			}
			if (prevThreads != Thread.getAllStackTraces().keySet().size()) {
	//			logger.info("Запущено " + Thread.getAllStackTraces().keySet().size() + " потоков");
			}
			prevThreads = Thread.getAllStackTraces().keySet().size();
			datagramPacket = executor.submit(new ReadThread(datagramSocket)).get();
			if (datagramPacket != null && new String(datagramPacket.getData(), 0, datagramPacket.getLength()).isEmpty()) {
				datagramPacket = null;
			}
			if (datagramPacket != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
        	                ObjectInput in = new ObjectInputStream(bis);
                	        StringShallow command = (StringShallow)in.readObject();
				if (command.getCommand().length == 1 && command.getCommand()[0].equals("connect")) {
					Response response = new Response(new String[]{"success!"});
					sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
					return;
				}
				//logger.info((curCommPar.get(command.getLogin()) == null ? 0 : curCommPar.get(command.getLogin())) + " vs " + (lastComm.get(command.getLogin()) == null ? 0 : lastComm.get(command.getLogin()).getCommand().getParameterAdvices() == null ? 0 : lastComm.get(command.getLogin()).getCommand().getParameterAdvices().length));
				if (!lastComm.containsKey(command.getLogin()) || lastComm.get(command.getLogin()).getCommand().getParameterAdvices() == null || curCommPar.get(command.getLogin()) >= lastComm.get(command.getLogin()).getCommand().getParameterAdvices().length) { //Если введена команда
					if (command.getCommand().length == 0 || commandmanager.getCommand(command.getCommand()[0]) == null) {
						Response response = new Response(new String[]{"Неизвестная команда!"});
						sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
						return;
					}
					Command com = commandmanager.getCommand(command.getCommand()[0]);
					curCommPar.put(command.getLogin(), 0);
					CommandShallow shallow = new CommandShallow(com, command.getCommand(), com.getParametersTypes() == null ? 0 : com.getParametersTypes().length, command.getLogin(), command.getPassword());
					if (shallow.getCommand().getName().equals("sign_in login password") || shallow.getCommand().getName().equals("register login password") || shallow.getCommand().getName().equals("exit") || shallow.getCommand().getName().equals("help") || (shallow.getLogin() != null && shallow.getPassword() != null)) {
						if (shallow.getCommand().getParameterAdvices() == null || shallow.getCommand().getParameterAdvices().length == 0) {
	                	                        /*FutureTask<Response> future = new FutureTask<>(new RequestThread(requestLock, collection, shallow, histories, datagramPacket.getAddress(), commandmanager, connector));
        	                	                Thread requestThread = new Thread(future);
                	                	        requestThread.start();
                        	                	if (future.get().getMessage().length >= 1 && !(future.get().getMessage()[0].equals("null") || future.get().getMessage()[0] == null))
	                        	                {
        	                        	                sender.send(future.get(), datagramPacket.getAddress(), datagramPacket.getPort(), logger);
                	                        	}*/
							lastComm.put(command.getLogin(), shallow);
							executeRequest(shallow, logger);
						}
						else {
							lastComm.put(command.getLogin(), shallow);
							Response response = new Response(new String[] {shallow.getCommand().getParameterAdvices()[0]});
							sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
						}
						return;
                        	        }
					else {
						Response response = new Response(new String[]{"Команда недоступна неавторизованным пользователям!"});
                                                sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
                                                return;
					}
				}
				else {
					int curPar = curCommPar.get(command.getLogin());
					String par = command.getCommand().length > 0 ? command.getCommand()[0] : null;
					try {
						if (!lastComm.get(command.getLogin()).getCommand().getChecker(curPar).check(par)) {
							Response response = new Response(new String[]{"Неправильный аргумент, попробуйте снова"});
							sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
	                                                curCommPar.put(command.getLogin(), 1000000);
							return;
						}
					}
					catch (Exception e) {
						logger.info(e.getMessage());
					}
					lastComm.get(command.getLogin()).setParameter(curPar, par);
					++curPar;
					curCommPar.put(command.getLogin(), curPar);
					if (curPar >= lastComm.get(command.getLogin()).getCommand().getParameterAdvices().length) {
						logger.info("Выполняем запрос...");
						if (curPar < lastComm.get(command.getLogin()).getCommand().getParametersTypes().length) {
							lastComm.get(command.getLogin()).setParameter(curPar, command.getLogin());
							++curPar;
						}
						executeRequest(lastComm.get(command.getLogin()), logger);
						return;
					}
					Response response = new Response(new String[]{lastComm.get(command.getLogin()).getCommand().getParameterAdvices()[curPar]});
                                        sender.send(response, datagramPacket.getAddress(), datagramPacket.getPort(), logger);
				}
					
			}
				/*
				if (shallow.getCommand().getName().equals("sign_in login password") || shallow.getCommand().getName().equals("register login password") || shallow.getCommand().getName().equals("exit") || shallow.getCommand().getName().equals("help") || (shallow.getLogin() != null && shallow.getPassword() != null)) {
					Response response = new Response();
					logger.info("Создаем поток");
					FutureTask<Response> future = new FutureTask<>(new RequestThread(requestLock, collection, shallow, histories, datagramPacket.getAddress(), commandmanager, connector));
					Thread requestThread = new Thread(future);
					requestThread.start();
					if (future.get().getMessage().length >= 1 && !(future.get().getMessage()[0].equals("null") || future.get().getMessage()[0] == null))
					{
						sender.send(future.get(), datagramPacket.getAddress(), datagramPacket.getPort(), logger);
					}
				}
			}
			*/
			//showMessage(datagramPacket);
		}
		catch (InterruptedException e) {
        		e.printStackTrace();
        	}
		catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	public void executeRequest(CommandShallow shallow, Logger logger) {
		try {
			
			FutureTask<Response> future = new FutureTask<>(new RequestThread(requestLock, collection, shallow, histories, datagramPacket.getAddress(), commandmanager, connector));
			Thread requestThread = new Thread(future);
			requestThread.start();
			if (future.get().getMessage().length == 0 || (future.get().getMessage().length >= 1 && !(future.get().getMessage()[0].equals("null") || future.get().getMessage()[0] == null))) {
				sender.send(future.get(), datagramPacket.getAddress(), datagramPacket.getPort(), logger);
			}
		}
		catch (Exception e) {
			logger.info("Поток прерван");
		}
		
	}

	public void stop() {
		try {
			this.active = false;
			executor.shutdown();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

