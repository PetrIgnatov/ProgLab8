package ru.se.ifmo.prog.lab8.classes;

import java.io.Serializable;

public class Coordinates implements Comparable<Coordinates>, Serializable {
	private Integer x; //Значение поля должно быть больше -32, Поле не может быть null
	private Float y; //Поле не может быть null
			
	public Coordinates(int x, float y) {
		if (x <= -32) {
			throw new IllegalArgumentException("Error! X value should be greater than -32");
		}
		this.x = Integer.valueOf(x);
		this.y = Float.valueOf(y);
	}

	public Coordinates(Integer x, Float y) {
		if (x == null) {
			throw new IllegalArgumentException("Error! X value can't be null");
		}
		if (x.intValue() <= -32) {
			throw new IllegalArgumentException("Error! X value should be greater than -32");
		}
		this.x = Integer.valueOf(x);
		if (y == null) {
			throw new IllegalArgumentException("Error! Y value can't be null");
		}
		this.y = Float.valueOf(y);
	}

	public int getX() {
		return this.x.intValue();
	}
	
	public float getY() {
		return this.y.floatValue();
	}
	public Integer getXPtr()
	{
		return this.x;
	}
	public Float getYPtr()
	{
		return this.y;
	}
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other == null || other.getClass() != this.getClass()) {
			return false;
		}
		Coordinates coordinates = (Coordinates)other;
		return (this.x.intValue() == coordinates.getX() && this.y.floatValue() == coordinates.getY());
	}
	@Override
	public int hashCode()
	{
		return (int)(y.floatValue()*1000001)+x.intValue();
	}
	@Override
	public int compareTo(Coordinates other) {
		if (this.x.intValue() < other.getX()) {
			return -1;
		}
		if (this.x.intValue() > other.getX()) {
			return 1;
		}
		if (this.y.floatValue() < other.getY()) {
			return -1;
		}
		if (this.y.floatValue() > other.getY()) {
			return 1;
		}
		return 0;
	}
	@Override
	public String toString() {
		return x.toString() + ";" + y.toString();
	}
}
