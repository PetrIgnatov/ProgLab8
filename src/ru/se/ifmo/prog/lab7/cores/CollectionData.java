package ru.se.ifmo.prog.lab7.cores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import ru.se.ifmo.prog.lab7.classes.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Collections;
import ru.se.ifmo.prog.lab7.exceptions.*;
import java.util.concurrent.locks.ReentrantLock;

public class CollectionData {
	private LinkedList<Dragon> dragons;
	private String filename;
	private java.util.Date initDate;
	private int maxId;
	
	public CollectionData() {
		maxId = 0;
		initDate = new java.util.Date();
		dragons = new LinkedList<Dragon>();
	}

	public CollectionData(LinkedList<String> data) {
		maxId = 0;
		initDate = new java.util.Date();
		dragons = new LinkedList<Dragon>();
		try {
			for (String dragon : data) {
				try {
					String[] splitted = dragon.split(";");
					Color col = null;
					switch(splitted[6]) {
						case "GREEN":
							col = Color.GREEN;
							break;
						case "YELLOW":
							col = Color.YELLOW;
							break;
						case "ORANGE":
							col = Color.ORANGE;
							break;
						case "WHITE":
							col = Color.WHITE;
							break;	
						case "":
							col = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный цвет \"" + splitted[6] + "\"");
					}
					DragonType type = null; 
					switch(splitted[7]) {
						case "WATER":
							type = DragonType.WATER;
							break;
						case "UNDERGROUND":
							type = DragonType.UNDERGROUND;	
							break;
						case "AIR":
							type = DragonType.AIR;
							break;
						case "":
							type = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный тип \"" + splitted[7] + "\"");
					}
					DragonCharacter character = null;
					switch(splitted[8]) {
						case "EVIL":
							character = DragonCharacter.EVIL;
							break;
						case "GOOD":
							character = DragonCharacter.GOOD;	
							break;
						case "CHAOTIC":
							character = DragonCharacter.CHAOTIC;
							break;
						case "FICKLE":
							character = DragonCharacter.FICKLE;	
							break;
						case "CHAOTIC_EVIL":
							character = DragonCharacter.CHAOTIC_EVIL;
							break;
						case "":
							character = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный характер \"" + splitted[8] + "\"");
					}
					String format = "yyyy-MM-dd HH:mm:ss";
					DateTimeFormatter formater = DateTimeFormatter.ofPattern(format);
					LocalDateTime date = LocalDateTime.parse(splitted[4], formater);
					maxId = Math.max(maxId, Integer.parseInt(splitted[0]));
					for (int i = 0; i < dragons.size(); ++i) {
						if (Integer.parseInt(splitted[0]) == dragons.get(i).getId()) {
							throw new ConvertationException("Ошибка! У двух драконов одинаковый ID");
						}
					}
					dragons.add(new Dragon(
								Integer.parseInt(splitted[0]),
								splitted[1].equals("") ? null : splitted[1],
								splitted[2].equals("") ? null : Integer.parseInt(splitted[2]),
								splitted[3].equals("") ? null : Float.parseFloat(splitted[3]),
								date,
								Integer.parseInt(splitted[5]),
								col,type,character,
								splitted[9].equals("") ? null : Double.parseDouble(splitted[9]),
								splitted[10].equals("") ? null : Float.parseFloat(splitted[10]),
								splitted[11].equals("") ? null : splitted[11]));
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void addDragons(LinkedList<String> data) {
		try {
			for (String dragon : data) {
				try {
					String[] splitted = dragon.split(";");
					Color col = null;
					switch(splitted[6]) {
						case "GREEN":
							col = Color.GREEN;
							break;
						case "YELLOW":
							col = Color.YELLOW;
							break;
						case "ORANGE":
							col = Color.ORANGE;
							break;
						case "WHITE":
							col = Color.WHITE;
							break;	
						case "":
							col = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный цвет \"" + splitted[6] + "\"");
					}
					DragonType type = null; 
					switch(splitted[7]) {
						case "WATER":
							type = DragonType.WATER;
							break;
						case "UNDERGROUND":
							type = DragonType.UNDERGROUND;	
							break;
						case "AIR":
							type = DragonType.AIR;
							break;
						case "":
							type = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный тип \"" + splitted[7] + "\"");
					}
					DragonCharacter character = null;
					switch(splitted[8]) {
						case "EVIL":
							character = DragonCharacter.EVIL;
							break;
						case "GOOD":
							character = DragonCharacter.GOOD;	
							break;
						case "CHAOTIC":
							character = DragonCharacter.CHAOTIC;
							break;
						case "FICKLE":
							character = DragonCharacter.FICKLE;	
							break;
						case "CHAOTIC_EVIL":
							character = DragonCharacter.CHAOTIC_EVIL;
							break;
						case "":
							character = null;
							break;
						default:
							throw new ConvertationException("Ошибка! Неизвестный характер \"" + splitted[8] + "\"");
					}
					String format = "yyyy-MM-dd HH:mm:ss";
					DateTimeFormatter formater = DateTimeFormatter.ofPattern(format);
					LocalDateTime date = LocalDateTime.parse(splitted[4], formater);
					maxId = Math.max(maxId, Integer.parseInt(splitted[0]));
					for (int i = 0; i < dragons.size(); ++i) {
						if (Integer.parseInt(splitted[0]) == dragons.get(i).getId()) {
							throw new ConvertationException("Ошибка! У двух драконов одинаковый ID");
						}
					}
					dragons.add(new Dragon(
								Integer.parseInt(splitted[0]),
								splitted[1].equals("") ? null : splitted[1],
								splitted[2].equals("") ? null : Integer.parseInt(splitted[2]),
								splitted[3].equals("") ? null : Float.parseFloat(splitted[3]),
								date,
								Integer.parseInt(splitted[5]),
								col,type,character,
								splitted[9].equals("") ? null : Double.parseDouble(splitted[9]),
								splitted[10].equals("") ? null : Float.parseFloat(splitted[10]),
								splitted[11].equals("") ? null : splitted[11]));
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public LinkedList<Dragon> getDragons() {
		return dragons;
	}

	public void clear() {
		dragons.clear();
		maxId = 0;
	}

	public void sort() {
		Collections.sort(dragons);
	}
	
	public Dragon createDragon(String[] splitted, int id) {
		LocalDateTime date = LocalDateTime.now();
                try {
			Color col = null;
			switch(splitted[4]) {
				case "GREEN":
					col = Color.GREEN;
					break;
				case "YELLOW":
					col = Color.YELLOW;
					break;
				case "ORANGE":
					col = Color.ORANGE;
					break;
				case "WHITE":
					col = Color.WHITE;
					break;	
				case "":
					col = null;
					break;
				default:
					throw new ConvertationException("Error! Unknown color \"" + splitted[4] + "\"");
			}
			DragonType type = null; 
			switch(splitted[5]) {
				case "WATER":
					type = DragonType.WATER;
					break;
				case "UNDERGROUND":
					type = DragonType.UNDERGROUND;	
					break;
				case "AIR":
					type = DragonType.AIR;
					break;
				case "":
					type = null;
					break;
				default:
					throw new ConvertationException("Error! Unknown type \"" + splitted[5] + "\"");
			}
			DragonCharacter character = null;
			switch(splitted[6]) {
				case "EVIL":
					character = DragonCharacter.EVIL;
					break;
				case "GOOD":
					character = DragonCharacter.GOOD;	
					break;
				case "CHAOTIC":
					character = DragonCharacter.CHAOTIC;
					break;
				case "FICKLE":
					character = DragonCharacter.FICKLE;	
					break;
				case "CHAOTIC_EVIL":
					character = DragonCharacter.CHAOTIC_EVIL;
					break;
				case "":
					character = null;
					break;
				default:
					throw new ConvertationException("Error! Unknown character \"" + splitted[6] + "\"");
			}
			if (splitted[0] == "") {
				throw new IOException("Ошибка! Имя не может быть равен null");
			}
			if (splitted[3] == "") {
				throw new IOException("Ошибка! Возраст не может быть равен null");
			}
			return new Dragon(
					id,
					splitted[0].equals("") ? null : splitted[0],
					splitted[1].equals("") ? null : Integer.parseInt(splitted[1]),
					splitted[2].equals("") ? null : Float.parseFloat(splitted[2]),
					date,
					splitted[3].equals("") ? null : Integer.parseInt(splitted[3]),
					col,type,character,
					splitted[7].equals("") ? null : Double.parseDouble(splitted[7]),
					splitted[8].equals("") ? null : Float.parseFloat(splitted[8]),
					splitted[9].equals("") ? null : splitted[9]);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void add(String[] splitted) {
		Dragon newDragon = createDragon(splitted, maxId+1);
		if (newDragon != null) {
			dragons.add(newDragon);
			++maxId;
		}
	}
	
	public void add(Dragon dragon) {
		if (dragon != null) {
			dragon.setId(++maxId);
			dragon.setDate(LocalDateTime.now());
			dragons.add(dragon);
		}
	}

	public int findById(int id) {
		for (int i = 0; i < dragons.size(); ++i) {
			if (dragons.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}

	public String[] update(String[] parameters, int ind, int id, String login) {
		if (dragons.get(findById(id)).getOwner().equals(login)) {
			Dragon newDragon = createDragon(parameters, id);
			if (newDragon != null) {
				dragons.set(ind, newDragon);
				return new String[0];
			}
			return new String[] {"Дракон не найден"};
		}
		return new String[] {"Вы не можете менять не своего дракона"};
	}

	public synchronized String[] update(Dragon dragon, int ind, int id, String login) {
		try {
			if (!dragon.equals(null)) {
				if (login.equals(dragons.get(ind).getOwner())) {
					dragon.setId(id);
					dragon.setDate(LocalDateTime.now());
					System.out.println(dragon.toString());
					dragons.set(ind, dragon);
					return new String[0];
				}
				else {
					return new String[] {"Вы не можете менять не своего дракона"};
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new String[] {"Произошла ошибка. Попробуйте снова"};
		}
		return new String[0];
	}

	public String[] remove(int id, String login) {
		try {
			for (int i = 0; i < dragons.size(); ++i) {
				if (dragons.get(i).getId() == id) {
					if (dragons.get(i).getOwner().equals(login)) {
						dragons.remove(i);
						return new String[0];
					}
					return new String[] {"Вы не можете удалить не своего дракона"};
				}
			}
			return new String[] {"Дракон с ID " + id + " не найден"};
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String[0];
	}

	public synchronized String[] removeIndex(int index, String login) {
		try {
			if (index < 0 || index >= dragons.size()) {
				return new String[] {"Недопустимый индекс"};
			}
			if (dragons.get(index).getOwner().equals(login)) {
				dragons.remove(index);
				return new String[0];
			}
			return new String[] {"Вы не можете удалять не своего дракона"};
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String[0];
	}
	
	public int sumAge() {
		int sum = 0;
		for (Dragon dragon : dragons) {
			sum += dragon.getAge();
		}
		return sum;
	}

	public String dragonsString() {
		String s = "";
		for (int i = Math.max(0,dragons.size()-100); i < dragons.size(); ++i) {
			s += dragons.get(i).toString() + "\n";
		}
		return s;
	}
	@Override
	public String toString() {
		return "LinkedList<Dragon>;" + initDate.toString() + ";" + Integer.toString(dragons.size()) + " elements;";
	}
}

