package ru.se.ifmo.prog.lab7.commands;

import ru.se.ifmo.prog.lab7.cores.*;
import ru.se.ifmo.prog.lab7.exceptions.*;
import java.io.Serializable;

public abstract class Command implements Executable, Serializable {
	String name;
	String description;
	CommandManager commandmanager;
	CollectionData collectiondata;
	int argsnumber;
	String[] parametersAdvices;
	String[] parametersTypes;
	Checker[] checkers;
	public int calls;

	public Command(String name, String description, int argsnumber) {
                this.name = name;
                this.description = description;
                this.argsnumber = argsnumber;
                this.parametersAdvices = new String[0];
                this.parametersTypes = new String[0];
                this.checkers = new Checker[0];
                this.calls = 1;
        }


	public Command(String name, String description, int argsnumber, String[] parametersAdvices) {
                this.name = name;
                this.description = description;
                this.argsnumber = argsnumber;
                this.parametersAdvices = parametersAdvices;
                this.parametersTypes = new String[0];
		this.checkers = new Checker[0];
                this.calls = 1;
        }

	public Command(String name, String description, int argsnumber, String[] parametersAdvices, String[] parametersTypes, Checker[] checkers) {
		this.name = name;
		this.description = description;
		this.argsnumber = argsnumber;
		this.parametersAdvices = parametersAdvices;
		this.parametersTypes = parametersTypes;
		this.checkers = checkers;
		this.calls = 1;
	}

	public void check(int argsnumber) throws InputArgumentException{
		if (this.argsnumber != argsnumber) {
			throw new InputArgumentException("Неверное количество аргументов: " + Integer.valueOf(argsnumber-1) + " вместо " + Integer.valueOf(this.argsnumber-1));
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getDescription() {
		return description;
	}

	public String[] getParameterAdvices() {
		return parametersAdvices;
	}

	public String[] getParametersTypes() {
		return parametersTypes;
	}

	public Checker[] getCheckers() {
		return checkers;
	}

	public Checker getChecker(int i) {
		if (i >= checkers.length) {
			return null;
		}
		return checkers[i];
	}
}
