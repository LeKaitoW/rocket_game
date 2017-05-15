package A_heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

	private final List<Board> open = new ArrayList<Board>();
	//private final Map<Board, Integer> open = new HashMap<>();
	private final List<Board> close = new ArrayList<Board>();

	public Game() {
		open.clear();
		close.clear();
		Board startBoard = new Board();
		open.add(startBoard);
		//open.put(startBoard, startBoard.getHeuristicRate());
	}

	public void solution() {
		for (int i = 0; i < 15000; i++) {
			System.out.println(i);
			//System.out.println(open);
			Board currentBoard = open.get(0);
			
			/*Integer minRate = Collections.min(open.values());
			for (Board opened : open.keySet())
				if (open.get(opened) == minRate) {
					currentBoard = opened;
					break;
				}*/
			
			Integer minRate = open.get(0).getHeuristicRate();
			for (Board opened : open){
				if (opened.getHeuristicRate() < minRate){
					minRate = opened.getHeuristicRate();
					currentBoard = opened;
				}
			}
			
			System.out.println(minRate);
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
					addToOpen(copyLeft, currentBoard.getHeuristicRate()- currentBoard.heuristicRate());
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
		System.out.println(open);
		// open.get(0).printPosition();
		close.get(close.size()-1).printPosition();
	}

	private void addToOpen(Board currentBoard, int path) {
		boolean same = false;
		for (int i = 0; i < close.size(); i++) {
			if (samePosition(currentBoard, close.get(i))) {
				same = true;
				break;
			}
		}

		//for (Board opened : open.keySet()) {
		for (Board opened : open){
			if (samePosition(currentBoard, opened)) {
				same = true;
				break;
			}
		}

		if (!same) {
			open.add(currentBoard);
			currentBoard.updateHeuristicRate(path);
			//open.put(currentBoard, currentBoard.heuristicRate() + 1);
		}
	}

	private boolean samePosition(Board current, Board old) {
		if (current.getPosition().size() != old.getPosition().size())
			return false;
		int notSame = 0;
		List<Hole> oldHoles = old.getHoles();
		List<Hole> currentHoles = current.getHoles();
		for (int i = 0; i < oldHoles.size(); i++) {
			if (oldHoles.get(i).getRow() != currentHoles.get(i).getRow()
					|| oldHoles.get(i).getColumn() != currentHoles.get(i).getColumn())
				notSame++;
		}
		List<Node> oldNodes = old.getNodes();
		List<Node> currentNodes = current.getNodes();
		for (int i = 0; i < oldNodes.size(); i++) {
			for (int j = 0; j < oldNodes.get(i).getSize(); j++) {
				if (oldNodes.get(i).getRows().get(j) != currentNodes.get(i).getRows().get(j)
						|| oldNodes.get(i).getColumns().get(j) != currentNodes.get(i).getColumns().get(j))
					notSame++;
			}
		}
		return (notSame == 0) ? true : false;
	}
}
