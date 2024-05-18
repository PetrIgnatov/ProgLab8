package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;

public class UpdateID extends Command {
	public UpdateID() {
		super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному", 2, new String[]{"Имя дракона: ", "Координата X: ", "Координата Y: ", "Возраст: ", "Цвет (доступные варианты - GREEN, YELLOW, ORANGE, WHITE): ", "Тип дракона (доступные варианты - WATER, UNDERGROUND, AIR): ", "Характер дракона (доступные варианты - EVIL, GOOD, CHAOTIC, CHAOTIC_EVIL, FICKLE): ", "Глубина пещеры: ", "Количество сокровищ в пещере: "}, new String[] {"String", "Int", "Float", "Int", "Color", "Type", "Character", "Double", "Float", "Login"}, new Checker[] {(String x) -> x != null && !x.equals(""), (String x) -> x != null && StringShallow.isInteger(x) && Integer.parseInt(x) > -32, (String x) -> x != null && StringShallow.isFloat(x), (String x) -> x != null && StringShallow.isInteger(x) && Integer.parseInt(x) > 0, (String x) -> x.equals("GREEN") || x.equals("ORANGE") || x.equals("WHITE") || x.equals("YELLOW"), (String x) -> x.equals("WATER") || x.equals("UNDERGROUND") || x.equals("AIR"), (String x) -> x.equals("EVIL") || x.equals("GOOD") || x.equals("CHAOTIC") || x.equals("CHAOTIC_EVIL") || x.equals("FICKLE"), (String x) -> x != null && StringShallow.isFloat(x), (String x) -> StringShallow.isFloat(x) && Float.parseFloat(x) > 0});
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
      return new Response(new String[0]);
    }
		try {
			Integer.parseInt(args[1]);
		}
		catch (Exception e) {
			return new Response(new String[] {"Аргумент не является числом"});
			
		}
		int index = collectiondata.findById(Integer.parseInt(args[1]));
		if (index == -1) {
			return new Response(new String[] {"Дракон с ID " + args[1] + " не найден"});
		}
       		return new Response(collectiondata.update(params, index, Integer.parseInt(args[1]), login));
        }
}


