package rocket_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Board {
	private final int verticalSize = 5;
	private final int horizontalSize = 7;

	/*
	 * Р - 1 или 2 - 13 А - 2 или 3 - 12 К - 3 или 4 - 11 Е - 4 или 5 - 14 Т - 5
	 * или 6 - 15 А - 6 или 7 - 16
	 */

	private List<Hole> holes = new ArrayList<>();
	private List<Node> nodes = new ArrayList<>();
	private List<List<Cell>> position = new ArrayList<List<Cell>>();
	private int heuristicRate;

	public Board() {
		for (int i = 0; i < verticalSize; i++)
			position.add(i, new ArrayList<Cell>(Collections.nCopies(horizontalSize, null)));

		holes.add(new Hole(2, 0));
		holes.add(new Hole(2, 3));
		holes.add(new Hole(2, 6));

		nodes.add(0, new Node(1, new ArrayList<>(Arrays.asList(0, 0)), new ArrayList<>(Arrays.asList(0, 1))));
		nodes.add(1, new Node(2, new ArrayList<>(Arrays.asList(0, 0)), new ArrayList<>(Arrays.asList(2, 3))));
		nodes.add(2, new Node(3, new ArrayList<>(Arrays.asList(0, 0)), new ArrayList<>(Arrays.asList(4, 5))));
		nodes.add(3, new Node(4, new ArrayList<>(Arrays.asList(1, 1)), new ArrayList<>(Arrays.asList(0, 1))));
		nodes.add(4, new Node(5, new ArrayList<>(Arrays.asList(1, 1)), new ArrayList<>(Arrays.asList(3, 4))));
		nodes.add(5, new Node(6, new ArrayList<>(Arrays.asList(3, 3)), new ArrayList<>(Arrays.asList(2, 3))));
		nodes.add(6, new Node(7, new ArrayList<>(Arrays.asList(3, 3)), new ArrayList<>(Arrays.asList(5, 6))));
		nodes.add(7, new Node(8, new ArrayList<>(Arrays.asList(4, 4)), new ArrayList<>(Arrays.asList(1, 2))));
		nodes.add(8, new Node(9, new ArrayList<>(Arrays.asList(4, 4)), new ArrayList<>(Arrays.asList(3, 4))));
		nodes.add(9, new Node(10, new ArrayList<>(Arrays.asList(4, 4)), new ArrayList<>(Arrays.asList(5, 6))));
		nodes.add(10, new Node(11, new ArrayList<>(Arrays.asList(3, 4)), new ArrayList<>(Arrays.asList(0, 0))));
		nodes.add(11, new Node(12, new ArrayList<>(Arrays.asList(2, 3)), new ArrayList<>(Arrays.asList(1, 1))));
		nodes.add(12, new Node(13, new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(2, 2))));
		nodes.add(13, new Node(14, new ArrayList<>(Arrays.asList(2, 3)), new ArrayList<>(Arrays.asList(4, 4))));
		nodes.add(14, new Node(15, new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(5, 5))));
		nodes.add(15, new Node(16, new ArrayList<>(Arrays.asList(0, 1)), new ArrayList<>(Arrays.asList(6, 6))));

		convertToPosition();
		heuristicRate = heuristicRate();
	}

	/*
	 * public Board(List<Node> nodes, List<Hole> holes, List<List<Cell>>
	 * position, int iteration) { this.nodes = nodes; this.holes = holes;
	 * this.position = position; this.heuristicRate = heuristicRate() +
	 * iteration; }
	 */
	public Board(Board parent) {
		holes = new ArrayList<>();
		nodes = new ArrayList<>();

		for (int i = 0; i < verticalSize; i++)
			position.add(i, new ArrayList<Cell>(Collections.nCopies(horizontalSize, null)));

		for (int i = 0; i < parent.getHoles().size(); i++)
			holes.add(i, new Hole(parent.getHoles().get(i)));

		for (int i = 0; i < parent.getNodes().size(); i++)
			nodes.add(i, new Node(parent.getNodes().get(i)));

		convertToPosition();
	}

	public boolean isHole(int row, int column) {
		return (position.get(row).get(column) instanceof Hole) ? true : false;
	}

	public List<List<Cell>> getPosition() {
		return position;
	}

	public void swap(Hole hole, Node node, int index) {
		int row = hole.getRow();
		int column = hole.getColumn();
		hole.set(node.getRows().get(index), node.getColumns().get(index));
		List<Integer> rows = new ArrayList<Integer>(Arrays.asList(row, node.getRows().get(index == 0 ? 1 : 0)));
		List<Integer> columns = new ArrayList<>(Arrays.asList(column, node.getColumns().get(index == 0 ? 1 : 0)));
		Collections.sort(rows);
		Collections.sort(columns);
		node.set(rows, columns);
		convertToPosition();
	}

	public void doubleSwap(Hole firstHole, Hole secondHole, Node node) {
		int firstRow = firstHole.getRow();
		int firstColumn = firstHole.getColumn();

		int secondRow = secondHole.getRow();
		int secondColumn = secondHole.getColumn();

		firstHole.set(node.getRows().get(0), node.getColumns().get(0));
		secondHole.set(node.getRows().get(1), node.getColumns().get(1));
		node.set(new ArrayList<Integer>(Arrays.asList(firstRow, secondRow)),
				new ArrayList<Integer>(Arrays.asList(firstColumn, secondColumn)));
	}

	public int heuristicRate() {
		/*
		 * int sumFromFirst = 100; int sumFromSecond = 0; for (int i = 0; i < 3;
		 * i++) { //sumFromFirst += Math.abs(nodes.get(i +
		 * 11).getColumns().get(0) + i - 3); //sumFromSecond +=
		 * Math.abs(nodes.get(i + 11).getColumns().get(0) + (i + 1) - 3);
		 * sumFromFirst -= Math.abs(13 - nodes.get(i+10).getValue() -
		 * nodes.get(i + 10).getColumns().get(0)); } //return
		 * Math.min(sumFromFirst, sumFromSecond); return sumFromFirst;
		 */
		List<Integer> letters = new ArrayList<>();
		for (int i = 0; i < horizontalSize; i++) {
			for (int j = 0; j < verticalSize; j++) {
				if (position.get(j).get(i) instanceof Node && ((Node) position.get(j).get(i)).getOrient().equals("v")) {
					if (!letters.contains(position.get(j).get(i).getValue()))
						letters.add(position.get(j).get(i).getValue());
				}
			}
		}
		int rate = letters.get(1) / letters.get(0) + letters.get(2) / letters.get(0) + letters.get(0) / letters.get(3)
				+ letters.get(0) / letters.get(4) + letters.get(0) / letters.get(5) + letters.get(2) / letters.get(1)
				+ letters.get(1) / letters.get(3) + letters.get(1) / letters.get(4) + letters.get(1) / letters.get(5)
				+ letters.get(2) / letters.get(3) + letters.get(2) / letters.get(4) + letters.get(2) / letters.get(5)
				+ letters.get(3) / letters.get(4) + letters.get(3) / letters.get(5) + letters.get(4) / letters.get(5);
		/*
		 * if (nodes.get(12).getColumns().get(0) >
		 * nodes.get(11).getColumns().get(0)) rate = rate+10; if
		 * (nodes.get(11).getColumns().get(0) >
		 * nodes.get(10).getColumns().get(0)) rate = rate + 10; if
		 * (nodes.get(10).getColumns().get(0) >
		 * nodes.get(13).getColumns().get(0)) rate = rate + 10; if
		 * (nodes.get(13).getColumns().get(0) >
		 * nodes.get(14).getColumns().get(0)) rate = rate + 10;
		 * if(nodes.get(14).getColumns().get(0) >
		 * nodes.get(15).getColumns().get(0)) rate = rate + 10;
		 */
		// for ()
		/*
		 * for (Hole hole : holes) { //rate = rate * (hole.getColumn() + 1);
		 * switch (hole.getColumn()){ case 4: case 5: case 6: rate = rate *
		 * (hole.getColumn()); break; }
		 * 
		 * }
		 */
		return rate;
	}

	private void convertToPosition() {
		for (int i = 0; i < verticalSize; i++)
			position.set(i, new ArrayList<Cell>(Collections.nCopies(horizontalSize, null)));

		for (Node node : nodes) {
			for (int i = 0; i < node.getSize(); i++) {
				position.get(node.getRows().get(i)).set(node.getColumns().get(i), node);
			}
		}

		for (Hole hole : holes) {
			position.get(hole.getRow()).set(hole.getColumn(), hole);
		}
	}

	public void printPosition() {
		for (int i = 0; i < verticalSize; i++) {
			for (int j = 0; j < horizontalSize; j++)
				System.out.format("%3d", position.get(i).get(j).getValue());
			System.out.println();
		}
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Hole> getHoles() {
		return holes;
	}

	public int getHeuristicRate() {
		return heuristicRate;
	}

	public void updateHeuristicRate(int path) {
		heuristicRate = heuristicRate() + path + 1;
	}
}
