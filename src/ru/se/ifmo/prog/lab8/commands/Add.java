package ru.se.ifmo.prog.lab8.commands;

import ru.se.ifmo.prog.lab8.cores.*;
import ru.se.ifmo.prog.lab8.classes.*;

public class Add extends Command {
	public Add() {
		super("add {element}", "добавить новый элемент в коллекцию", 1, new String[]{"Имя дракона: ", "Координата X: ", "Координата Y: ", "Возраст: ", "Цвет (доступные варианты - GREEN, YELLOW, ORANGE, WHITE): ", "Тип дракона (доступные варианты - WATER, UNDERGROUND, AIR): ", "Характер дракона (доступные варианты - EVIL, GOOD, CHAOTIC, CHAOTIC_EVIL, FICKLE): ", "Глубина пещеры: ", "Количество сокровищ в пещере: "}, new String[] {"String", "Int", "Float", "Int", "Color", "Type", "Character", "Double", "Float", "Login"}, new Checker[] {(String x) -> x != null && !x.equals(""), (String x) -> x != null && StringShallow.isInteger(x) && Integer.parseInt(x) > -32, (String x) -> x != null && StringShallow.isFloat(x), (String x) -> x != null && StringShallow.isInteger(x) && Integer.parseInt(x) > 0, (String x) -> x.equals("GREEN") || x.equals("ORANGE") || x.equals("WHITE") || x.equals("YELLOW"), (String x) -> x.equals("WATER") || x.equals("UNDERGROUND") || x.equals("AIR"), (String x) -> x.equals("EVIL") || x.equals("GOOD") || x.equals("CHAOTIC") || x.equals("CHAOTIC_EVIL") || x.equals("FICKLE"), (String x) -> x != null && StringShallow.isFloat(x), (String x) -> StringShallow.isFloat(x) && Float.parseFloat(x) > 0});
	}
	@Override
	public Response execute(String[] args, Integer stacksize, Dragon dragon, CommandManager commandmanager, CollectionData collectiondata, DatabaseConnector connector, String[] params, String login, String password) {
		if (stacksize > 10000) {
      return new Response(new String[]{});
    }
		collectiondata.add(params);
		return new Response(new String[0]);
	}
}

