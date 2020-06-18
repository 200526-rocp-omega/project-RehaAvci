package com.revature.templates;

import java.util.Objects;

public class DurationTemplate {
	private int numOfMonths;

	public DurationTemplate() {
		super();
	}

	public DurationTemplate(int numOfMonths) {
		super();
		this.numOfMonths = numOfMonths;
	}

	public int getNumOfMonths() {
		return numOfMonths;
	}

	public void setNumOfMonths(int numOfMonths) {
		this.numOfMonths = numOfMonths;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numOfMonths);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DurationTemplate)) {
			return false;
		}
		DurationTemplate other = (DurationTemplate) obj;
		return numOfMonths == other.numOfMonths;
	}

	@Override
	public String toString() {
		return "PassTimeTemplate [numOfMonths=" + numOfMonths + "]";
	}
	
	
}
