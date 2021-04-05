package sudoku.controller.commands;

import sudoku.model.SudokuGrid;
import sudoku.model.tiles.SudokuTile;
import sudoku.view.ViewTile;


public abstract class Command {

	protected final SudokuGrid grid;
	protected final ViewTile viewTile;
	protected final SudokuTile oldTile;
	protected SudokuTile newTile;

	protected Command(SudokuGrid grid, ViewTile viewTile) {
		this.grid = grid;
		this.viewTile = viewTile;
		oldTile = viewTile.getSudokuTile();
	}

	public void execute() {
		if (!canChange(oldTile, newTile)) {
			throw new CantChangeException("Conflict.");
		}
		viewTile.setTile(newTile);
		grid.replaceTile(oldTile, newTile);
	}

	public void undo() {
		viewTile.setTile(oldTile);
		grid.replaceTile(newTile, oldTile);
	}

	protected abstract boolean canChange(SudokuTile oldTile, SudokuTile newTile);

}
