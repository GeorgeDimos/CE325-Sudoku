package sudoku.model.tiles;

import java.awt.*;

public class SudokuVariableTile extends SolvedTile {

	protected final int value;
	private final Color defaultBackgroundColor = new Color(140, 140, 140);

	public SudokuVariableTile(SudokuTile tile, int newValue) {
		super(tile.getSolution(), tile.getGrid(), tile.getRow(), tile.getColumn());
		this.value = newValue;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String getText() {
		return String.valueOf(value);
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
