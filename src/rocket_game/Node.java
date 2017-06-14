package rocket_game;

import java.util.ArrayList;
import java.util.List;

abstract class Node extends Cell {

	private int size = 2;
	private List<Integer> rows = new ArrayList<>();
	private List<Integer> columns = new ArrayList<>();

	public Node(int value, List<Integer> rows, List<Integer> columns) {
		this.value = value;
		this.rows = rows;
		this.columns = columns;
	}

	public Node(Node node) {
		this(node.getValue(), node.getRows(), node.getColumns());
	}

	public int getSize() {
		return size;
	}

	public List<Integer> getRows() {
		return rows;
	}

	public List<Integer> getColumns() {
		return columns;
	}

	public void set(List<Integer> rows, List<Integer> columns) {
		this.rows = rows;
		this.columns = columns;
	}

	abstract void moveUp(Board board);

	abstract void moveDown(Board board);

	abstract void moveLeft(Board board);

	abstract void moveRight(Board board);
}
