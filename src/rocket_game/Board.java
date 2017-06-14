package rocket_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
	private final int verticalSize = 5;
	private final int horizontalSize = 7;

	private List<Hole> holes = new ArrayList<>();
	private List<VNode> vNodes = new ArrayList<>();
	private List<HNode> hNodes = new ArrayList<>();
	private List<List<Cell>> position = new ArrayList<List<Cell>>();
	private int heuristicRate;

	public Board() {
		for (int i = 0; i < verticalSize; i++)
			position.add(i, new ArrayList<Cell>(Collections.nCopies(horizontalSize, null)));

		holes.add(new Hole(2, 0));
		holes.add(new Hole(2, 3));
		holes.add(new Hole(2, 6));

		hNodes.add(0, new HNode(1, new ArrayList<>(Arrays.asList(0, 0)), new ArrayList<>(Arrays.asList(0, 1))));
		hNodes.add(1, new HNode(2, new ArrayList<>(Arrays.asList(0, 0)), new ArrayList<>(Arrays.asList(2, 3))));
		hNodes.add(2, new HNode(3, new ArrayList<>(Arrays.asList(0, 0)), new ArrayList<>(Arrays.asList(4, 5))));
		hNodes.add(3, new HNode(4, new ArrayList<>(Arrays.asList(1, 1)), new ArrayList<>(Arrays.asList(0, 1))));
		hNodes.add(4, new HNode(5, new ArrayList<>(Arrays.asList(1, 1)), new ArrayList<>(Arrays.asList(3, 4))));
		hNodes.add(5, new HNode(6, new ArrayList<>(Arrays.asList(3, 3)), new ArrayList<>(Arrays.asList(2, 3))));
		hNodes.add(6, new HNode(7, new ArrayList<>(Arrays.asList(3, 3)), new ArrayList<>(Arrays.asList(5, 6))));
		hNodes.add(7, new HNode(8, new ArrayList<>(Arrays.asList(4, 4)), new ArrayList<>(Arrays.asList(1, 2))));
		hNodes.add(8, new HNode(9, new ArrayList<>(Arrays.asList(4, 4)), new ArrayList<>(Arrays.asList(3, 4))));
		hNodes.add(9, new HNode(10, new ArrayList<>(Arrays.asList(4, 4)), new ArrayList<>(Arrays.asList(5, 6))));

		vNodes.add(0, new VNode(11, new ArrayList<>(Arrays.asList(3, 4)), new ArrayList<>(Arrays.asList(0, 0))));
		vNodes.add(1, new VNode(12, new ArrayList<>(Arrays.asList(2, 3)), new ArrayList<>(Arrays.asList(1, 1))));
		vNodes.add(2, new VNode(13, new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(2, 2))));
		vNodes.add(3, new VNode(14, new ArrayList<>(Arrays.asList(2, 3)), new ArrayList<>(Arrays.asList(4, 4))));
		vNodes.add(4, new VNode(15, new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(5, 5))));
		vNodes.add(5, new VNode(16, new ArrayList<>(Arrays.asList(0, 1)), new ArrayList<>(Arrays.asList(6, 6))));

		convertToPosition();
		heuristicRate = heuristicRate();
	}

	public Board(Board parent) {
		holes = new ArrayList<>();
		hNodes = new ArrayList<>();
		vNodes = new ArrayList<>();

		for (int i = 0; i < parent.getHoles().size(); i++)
			holes.add(i, new Hole(parent.getHoles().get(i)));

		for (int i = 0; i < parent.getHNodes().size(); i++)
			hNodes.add(i, new HNode(parent.getHNodes().get(i)));

		for (int i = 0; i < parent.getVNodes().size(); i++)
			vNodes.add(i, new VNode(parent.getVNodes().get(i)));

		for (int i = 0; i < verticalSize; i++)
			position.add(i, new ArrayList<Cell>(Collections.nCopies(horizontalSize, null)));
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
		Map<Integer, Integer> letters = new HashMap<>();
		for (VNode vNode : vNodes) {
			letters.put(vNode.getValue(), vNode.getColumns().get(0) + 1);
		}
		int rate = letters.get(12) / letters.get(11) + letters.get(13) / letters.get(11)
				+ letters.get(11) / letters.get(14) + letters.get(11) / letters.get(15)
				+ letters.get(11) / letters.get(16) + letters.get(13) / letters.get(12)
				+ letters.get(12) / letters.get(14) + letters.get(12) / letters.get(15)
				+ letters.get(12) / letters.get(16) + letters.get(13) / letters.get(14)
				+ letters.get(13) / letters.get(15) + letters.get(13) / letters.get(16)
				+ letters.get(14) / letters.get(15) + letters.get(14) / letters.get(16)
				+ letters.get(15) / letters.get(16);

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

		for (Node vNode : vNodes) {
			for (int i = 0; i < vNode.getSize(); i++) {
				position.get(vNode.getRows().get(i)).set(vNode.getColumns().get(i), vNode);
			}
		}

		for (Node hNode : hNodes) {
			for (int i = 0; i < hNode.getSize(); i++) {
				position.get(hNode.getRows().get(i)).set(hNode.getColumns().get(i), hNode);
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

	public List<HNode> getHNodes() {
		return hNodes;
	}

	public List<VNode> getVNodes() {
		return vNodes;
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
