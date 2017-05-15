package A_heuristic;

import java.util.ArrayList;
import java.util.List;

public class Node extends Cell {

	private int size = 2;
	private String orient;
	private List<Integer> rows = new ArrayList<>();
	private List<Integer> columns = new ArrayList<>();

	public Node(int value, List<Integer> rows, List<Integer> columns) {
		this.value = value;
		this.rows = rows;
		this.columns = columns;
		if (this.rows.get(0).equals(this.rows.get(1) - 1))
			this.orient = "v";
		if (this.columns.get(0).equals(this.columns.get(1) - 1))
			this.orient = "h";
	}

	public Node(Node node) {
		this(node.getValue(), node.getRows(), node.getColumns());
	}

	public int getSize() {
		return size;
	}

	public String getOrient() {
		return orient;
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

	public void moveUp(Board board) {
		if (rows.get(0) < 1) {
			return;
		}
		List<List<Cell>> position = board.getPosition();
		if (this.orient.equals("v")) {
			if (board.isHole(this.rows.get(0) - 1, this.columns.get(0))) {
				board.swap((Hole) position.get(this.rows.get(0) - 1).get(this.columns.get(0)), this, 1);
			}
		}

		if (this.orient.equals("h")) {
			if (board.isHole(this.rows.get(0) - 1, this.columns.get(0))
					&& board.isHole(this.rows.get(1) - 1, this.columns.get(1))) {
				board.doubleSwap((Hole) position.get(rows.get(0) - 1).get(columns.get(0)),
						(Hole) position.get(rows.get(1) - 1).get(columns.get(1)), this);
			}
		}
	}

	public void moveDown(Board board) {
		if (rows.get(size - 1) > 3) {
			return;
		}
		List<List<Cell>> position = board.getPosition();
		if (this.orient.equals("v")) {
			if (board.isHole(this.rows.get(1) + 1, this.columns.get(1))) {
				board.swap((Hole) position.get(this.rows.get(1) + 1).get(this.columns.get(1)), this, 0);
			}
		}

		if (this.orient.equals("h")) {
			if (board.isHole(rows.get(0) + 1, columns.get(0)) && board.isHole(rows.get(1) + 1, columns.get(1))) {
				board.doubleSwap((Hole) position.get(rows.get(0) + 1).get(columns.get(0)),
						(Hole) position.get(rows.get(1) + 1).get(columns.get(1)), this);
			}
		}
	}

	public void moveLeft(Board board) {
		if (columns.get(0) < 1)
			return;
		List<List<Cell>> position = board.getPosition();

		if (orient.equals("v")) {
			if (board.isHole(rows.get(0), columns.get(0) - 1) && board.isHole(rows.get(1), columns.get(1) - 1))
				board.doubleSwap((Hole) position.get(rows.get(0)).get(columns.get(0) - 1),
						(Hole) position.get(rows.get(1)).get(columns.get(1) - 1), this);
		}

		if (orient.equals("h")) {
			if (board.isHole(rows.get(0), columns.get(0) - 1))
				board.swap((Hole) position.get(rows.get(0)).get(columns.get(0) - 1), this, 1);
		}
	}

	public void moveRight(Board board) {
		if (columns.get(size - 1) > 5)
			return;
		List<List<Cell>> position = board.getPosition();

		if (orient.equals("v")) {
			if (board.isHole(rows.get(0), columns.get(0) + 1) && board.isHole(rows.get(1), columns.get(1) + 1))
				board.doubleSwap((Hole) position.get(rows.get(0)).get(columns.get(0) + 1),
						(Hole) position.get(rows.get(1)).get(columns.get(1) + 1), this);
		}
		if (orient.equals("h")) {
			if (board.isHole(rows.get(1), columns.get(1) + 1))
				board.swap((Hole) position.get(rows.get(1)).get(columns.get(1) + 1), this, 0);
		}
	}
}
