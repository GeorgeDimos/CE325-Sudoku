package sudoku.model.tiles;

import java.awt.*;

public class SolvedTile implements SudokuTile {

	private final int solution;
	private final int gridNumber;
	private final int row;
	private final int column;
	private final Color defaultBackgroundColor;

	public SolvedTile(int solution, int gridNumber, int row, int column) {
		this.solution = solution;
		this.gridNumber = gridNumber;
		this.row = row;
		this.column = column;
		this.defaultBackgroundColor = new Color(235, 235, 235);
	}

	public int getValue() {
		return solution;
	}

	public int getGrid() {
		return gridNumber;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public int getSolution() {
		return solution;
	}

	@Override
	public String getText() {
		return String.valueOf(solution);
	}

	public Color getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	@Override
	public boolean canChange() {
		return false;
	}

}
