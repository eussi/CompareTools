package com.dctis.diff;

public class Difference {
	public static final int NONE = -1;

	private int delStart = NONE;

	private int delEnd = NONE;

	private int addStart = NONE;

	private int addEnd = NONE;

	public Difference(int delStart, int delEnd, int addStart, int addEnd) {
		this.delStart = delStart;
		this.delEnd = delEnd;
		this.addStart = addStart;
		this.addEnd = addEnd;
	}

	public int getDeletedStart() {
		return delStart;
	}

	public int getDeletedEnd() {
		return delEnd;
	}

	public int getAddedStart() {
		return addStart;
	}

	public int getAddedEnd() {
		return addEnd;
	}

	public void setDeleted(int line) {
		delStart = Math.min(line, delStart);
		delEnd = Math.max(line, delEnd);
	}

	public void setAdded(int line) {
		addStart = Math.min(line, addStart);
		addEnd = Math.max(line, addEnd);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Difference) {
			Difference other = (Difference) obj;
			return (delStart == other.delStart && delEnd == other.delEnd
					&& addStart == other.addStart && addEnd == other.addEnd);
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("del: [" + delStart + ", " + delEnd + "]");
		buf.append(" ");
		buf.append("add: [" + addStart + ", " + addEnd + "]");
		return buf.toString();
	}
}
