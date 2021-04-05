package sudoku.model.tiles;

import java.awt.*;

public interface SudokuTile {
	int getValue();

	int getGrid();

	int getRow();

	int getColumn();

	int getSolution();

	String getText();

	Color getDefaultBackgroundColor();

	boolean canChange();
}
