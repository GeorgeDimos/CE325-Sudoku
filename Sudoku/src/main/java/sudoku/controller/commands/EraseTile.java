package sudoku.controller.commands;

import sudoku.model.SudokuGrid;
import sudoku.model.tiles.EmptyTile;
import sudoku.model.tiles.SudokuTile;
import sudoku.view.ViewTile;

public class EraseTile extends Command {

	public EraseTile(SudokuGrid grid, ViewTile viewTile) {
		super(grid, viewTile);
		this.newTile = new EmptyTile(oldTile);
	}

	@Override
	protected boolean canChange(SudokuTile oldTile, SudokuTile newTile) {
		return oldTile.canChange();
	}

}
