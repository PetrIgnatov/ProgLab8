package ru.se.ifmo.prog.lab7.classes;

import java.io.Serializable;

public class DragonCave implements Comparable<DragonCave>, Serializable {
	private Double depth; //Поле не может быть null
	private Float numberOfTreasures; //Поле может быть null, Значение поля должно быть больше 0
	
	public DragonCave() {
		throw new IllegalArgumentException("Error! Depth can't be null");
	}

	public DragonCave(double depth, float numberOfTreasures) {
		this.depth = Double.valueOf(depth);
		if (numberOfTreasures <= 0) {
			throw new IllegalArgumentException("Error! Number of Treasures should be more than 0");
		}
		this.numberOfTreasures = Float.valueOf(numberOfTreasures);
	}
	
	public DragonCave(Double depth, Float numberOfTreasures) {
		if (depth == null) {
			throw new IllegalArgumentException("Error! Depth can't be null");
		}
		this.depth = Double.valueOf(depth);
		if (numberOfTreasures != null && numberOfTreasures.floatValue() <= 0) {
			throw new IllegalArgumentException("Error! Number of Treasures should be more than 0");
		}
		if (numberOfTreasures != null)
		{
			this.numberOfTreasures = Float.valueOf(numberOfTreasures);
		}
	}

	public double getDepth() {
		return depth.doubleValue();
	}
	
	public float getNumberOfTreasures() {
		return numberOfTreasures.floatValue();
	}
	
	public Double getDepthPtr() {
		return depth;
	}
	
	public Float getNumberOfTreasuresPtr() {
		return numberOfTreasures;
	}
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other == null || other.getClass() != this.getClass()) {
			return false;
		}
		DragonCave cave = (DragonCave)other;
		return (this.depth.doubleValue() == cave.getDepth() && this.numberOfTreasures.floatValue() == cave.getNumberOfTreasures());
	}
	@Override
	public int hashCode() {
		return (int)(depth.doubleValue()*1000001+(double)numberOfTreasures.floatValue());
	}
	@Override
	public int compareTo(DragonCave other) {
		if (depth.doubleValue() < other.getDepth()) {
			return -1;
		}
		if (depth.doubleValue() > other.getDepth()) {
			return 1;
		}
		if (numberOfTreasures.doubleValue() < other.getNumberOfTreasures()) {
			return -1;
		}
		if (numberOfTreasures.doubleValue() > other.getNumberOfTreasures()) {
			return 1;
		}
		return 0;
	}
	@Override
	public String toString() {
		return depth.toString() + ";" + (numberOfTreasures == null ? "" : numberOfTreasures.toString());
	}
}
