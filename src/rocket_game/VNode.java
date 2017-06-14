package rocket_game;

import java.util.List;

public class VNode extends Node {

	public VNode(int value, List<Integer> rows, List<Integer> columns) {
		super(value, rows, columns);
	}

	public VNode(VNode vNode) {
		super(vNode);
	}

	@Override
	void moveUp(Board board) {
		List<List<Cell>> position = board.getPosition();
		if (board.isHole(getRows().get(0) - 1, getColumns().get(0))) {
			board.swap((Hole) position.get(getRows().get(0) - 1).get(getColumns().get(0)), this, 1);
		}

	}

	@Override
	void moveDown(Board board) {
		List<List<Cell>> position = board.getPosition();
		if (board.isHole(getRows().get(1) + 1, getColumns().get(1))) {
			board.swap((Hole) position.get(getRows().get(1) + 1).get(getColumns().get(1)), this, 0);
		}
	}

	@Override
	void moveLeft(Board board) {
		List<List<Cell>> position = board.getPosition();
		if (board.isHole(getRows().get(0), getColumns().get(0) - 1)
				&& board.isHole(getRows().get(1), getColumns().get(1) - 1))
			board.doubleSwap((Hole) position.get(getRows().get(0)).get(getColumns().get(0) - 1),
					(Hole) position.get(getRows().get(1)).get(getColumns().get(1) - 1), this);
	}

	@Override
	void moveRight(Board board) {
		List<List<Cell>> position = board.getPosition();
		if (board.isHole(getRows().get(0), getColumns().get(0) + 1)
				&& board.isHole(getRows().get(1), getColumns().get(1) + 1))
			board.doubleSwap((Hole) position.get(getRows().get(0)).get(getColumns().get(0) + 1),
					(Hole) position.get(getRows().get(1)).get(getColumns().get(1) + 1), this);
	}

}
