package sudoku.controller.commands;

import sudoku.model.SudokuGrid;
import sudoku.model.tiles.SudokuTile;
import sudoku.model.tiles.SudokuVariableTile;
import sudoku.view.ViewTile;

public class ChangeTile extends Command {

	private final int newValue;

	public ChangeTile(SudokuGrid grid, ViewTile viewTile, int newValue) {
		super(grid, viewTile);
		this.newValue = newValue;
		this.newTile = new SudokuVariableTile(oldTile, newValue);
	}

	@Override
	protected boolean canChange(SudokuTile oldTile, SudokuTile newTile) {
		return grid.hasConflicts(oldTile, newValue) && oldTile.canChange();
	}

}
