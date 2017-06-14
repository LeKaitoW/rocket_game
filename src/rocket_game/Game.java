package rocket_game;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private final List<Board> open = new ArrayList<Board>();
	private final List<Board> close = new ArrayList<Board>();

	public Game() {
		open.clear();
		close.clear();
		Board startBoard = new Board();
		open.add(startBoard);
	}

	public void solution() {
		Board currentBoard = null;
		for (int i = 0; i < 900000; i++) {
			Integer minRate = Integer.MAX_VALUE;
			for (Board opened : open) {
				if (opened.getHeuristicRate() < minRate) {
					minRate = opened.getHeuristicRate();
					currentBoard = opened;
				}
			}

			if (checkEnd(currentBoard))
				break;

			if (i % 1000 == 0) {
				System.out.println(i);
				System.out.print("open+close: ");
				System.out.println(open.size() + close.size());
				currentBoard.printPosition();
				System.out.println();
			}
			for (Hole hole : currentBoard.getHoles()) {
				Board copyUp = new Board(currentBoard);
				if (hole.getRow() < 4
						&& copyUp.getPosition().get(hole.getRow() + 1).get(hole.getColumn()) instanceof Node) {
					((Node) copyUp.getPosition().get(hole.getRow() + 1).get(hole.getColumn())).moveUp(copyUp);
					addToOpen(copyUp, currentBoard.getHeuristicRate() - currentBoard.heuristicRate());
				}

				Board copyDown = new Board(currentBoard);
				if (hole.getRow() > 0
						&& copyDown.getPosition().get(hole.getRow() - 1).get(hole.getColumn()) instanceof Node) {
					((Node) copyDown.getPosition().get(hole.getRow() - 1).get(hole.getColumn())).moveDown(copyDown);
					addToOpen(copyDown, currentBoard.getHeuristicRate() - currentBoard.heuristicRate());
				}

				Board copyLeft = new Board(currentBoard);
				if (hole.getColumn() < 6
						&& copyLeft.getPosition().get(hole.getRow()).get(hole.getColumn() + 1) instanceof Node) {
					((Node) copyLeft.getPosition().get(hole.getRow()).get(hole.getColumn() + 1)).moveLeft(copyLeft);
					addToOpen(copyLeft, currentBoard.getHeuristicRate() - currentBoard.heuristicRate());
				}

				Board copyRight = new Board(currentBoard);
				if (hole.getColumn() > 0
						&& copyRight.getPosition().get(hole.getRow()).get(hole.getColumn() - 1) instanceof Node) {
					((Node) copyRight.getPosition().get(hole.getRow()).get(hole.getColumn() - 1)).moveRight(copyRight);
					addToOpen(copyRight, currentBoard.getHeuristicRate() - currentBoard.heuristicRate());

				}
			}
			close.add(currentBoard);
			open.remove(currentBoard);
		}
		System.out.print("nodes:");
		System.out.println(open.size() + close.size());
		System.out.println("ended:");
		currentBoard.printPosition();
	}

	private void addToOpen(Board currentBoard, int path) {
		boolean same = false;
		for (Board closed : close) {
			if (samePosition(currentBoard, closed)) {
				same = true;
				break;
			}
		}
		for (Board opened : open) {
			if (samePosition(currentBoard, opened)) {
				same = true;
				break;
			}
		}

		if (!same) {
			open.add(currentBoard);
			currentBoard.updateHeuristicRate(path);
		}
	}

	private boolean samePosition(Board current, Board old) {
		if (current.getPosition().size() != old.getPosition().size())
			return false;
		List<Hole> oldHoles = old.getHoles();
		List<Hole> currentHoles = current.getHoles();
		for (int i = 0; i < oldHoles.size(); i++) {
			if (oldHoles.get(i).getRow() != currentHoles.get(i).getRow()
					|| oldHoles.get(i).getColumn() != currentHoles.get(i).getColumn())
				return false;
		}
		List<VNode> oldVNodes = old.getVNodes();
		List<VNode> currentVNodes = current.getVNodes();
		for (int i = 0; i < oldVNodes.size(); i++) {
			for (int j = 0; j < oldVNodes.get(i).getSize(); j++) {
				if (oldVNodes.get(i).getRows().get(j) != currentVNodes.get(i).getRows().get(j)
						|| oldVNodes.get(i).getColumns().get(j) != currentVNodes.get(i).getColumns().get(j))
					return false;
			}
		}
		return true;
	}

	private boolean checkEnd(Board board) {
		List<VNode> vNodes = board.getVNodes();
		if (vNodes.get(2).getColumns().get(0) < vNodes.get(1).getColumns().get(0)
				&& vNodes.get(1).getColumns().get(0) < vNodes.get(0).getColumns().get(0)
				&& vNodes.get(0).getColumns().get(0) < vNodes.get(3).getColumns().get(0)
				&& vNodes.get(3).getColumns().get(0) < vNodes.get(4).getColumns().get(0)
				&& vNodes.get(4).getColumns().get(0) < vNodes.get(5).getColumns().get(0)) {
			System.out.println("find");
			board.printPosition();
			return true;
		}
		return false;
	}
}

/*
 * find 4 4 1 1 14 3 3 12 0 2 2 14 15 16 12 0 11 5 5 15 16 13 0 11 6 6 7 7 13 8
 * 8 9 9 10 10 nodes:160889 ended: 4 4 1 1 14 3 3 12 0 2 2 14 15 16 12 0 11 5 5
 * 15 16 13 0 11 6 6 7 7 13 8 8 9 9 10 10
 */

/*
 * поиск в ширину 0 1 2 4 5 6 find 4 4 1 1 14 3 3 12 0 2 2 14 15 16 12 0 11 5 5
 * 15 16 13 0 11 6 6 7 7 13 8 8 9 9 10 10 nodes:181729 ended: 4 4 1 1 14 3 3 12
 * 0 2 2 14 15 16 12 0 11 5 5 15 16 13 0 11 6 6 7 7 13 8 8 9 9 10 10
 * 
 * поиск в глубину (памяти не хватило) 373000 open+close: 748609 2 2 0 0 15 14
 * 12 13 11 10 10 15 14 12 13 11 7 7 9 9 16 6 6 3 3 4 4 16 1 1 5 5 8 8 0
 * 
 * 
 * [0, 0] [1, 1] [2, 2] [4, 4] [5, 5] [6, 6] 13 11 12 14 15 16 find 4 4 1 1 14 3
 * 3 12 0 2 2 14 15 16 12 0 11 5 5 15 16 13 0 11 6 6 7 7 13 8 8 9 9 10 10
 * nodes:123639 ended: 4 4 1 1 14 3 3 12 0 2 2 14 15 16 12 0 11 5 5 15 16 13 0
 * 11 6 6 7 7 13 8 8 9 9 10 10
 */