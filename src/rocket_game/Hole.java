package rocket_game;

public class Hole extends Cell {

	private int row;
	private int column;

	public Hole(int row, int column) {
		this.value = 0;
		this.row = row;
		this.column = column;
	}

	public Hole(Hole hole) {
		this(hole.getRow(), hole.getColumn());
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void set(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
