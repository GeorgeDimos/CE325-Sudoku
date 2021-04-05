package sudoku.model.tiles;

import java.awt.*;

public class EmptyTile extends SolvedTile {

	private final Color defaultBackgroundColor = new Color(194, 180, 180);

	public EmptyTile(SudokuTile sudokuTile) {
		super(sudokuTile.getSolution(), sudokuTile.getGrid(), sudokuTile.getRow(), sudokuTile.getColumn());
	}

	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public String getText() {
		return "";
	}

	@Override
	public Color getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	@Override
	public boolean canChange() {
		return true;
	}

}

