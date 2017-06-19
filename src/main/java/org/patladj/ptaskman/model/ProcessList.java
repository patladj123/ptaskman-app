package org.patladj.ptaskman.model;

import java.util.ArrayList;

/**
 * This represents a list of processes with their data
 * @author PatlaDJ
 *
 */
public class ProcessList extends ArrayList<Process> {

	private static final long serialVersionUID = -5066532326392897124L;
	
	public ProcessList() {
		super();
	}
	
	public String toJsonArray() {
		StringBuilder sb=new StringBuilder();
		sb.append("[");

		int size = this.size();
		int lastIndex= size -1;
		for (int i = 0; i< size; i++) {
			sb.append(this.get(i).toJsonArray());
			if (i<lastIndex) { //Last index
				sb.append(",");
			}
		}
		sb.append("]");

		return sb.toString();
	}
}
