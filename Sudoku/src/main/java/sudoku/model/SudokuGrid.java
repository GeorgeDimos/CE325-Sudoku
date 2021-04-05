package sudoku.model;

import sudoku.model.tiles.EmptyTile;
import sudoku.model.tiles.SolvedTile;
import sudoku.model.tiles.SudokuTile;
import sudoku.solver.SudokuSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sets the Sudoku grid with the  input values parsed row by row.
 *
 * @author George
 */

public class SudokuGrid {
	private final SudokuSolver solver;
	private List<SudokuTile> tiles;

	public SudokuGrid(SudokuSolver solver) {
		this.solver = solver;
	}

	public void populateTiles(int[][] arr) {
		tiles = new ArrayList<>();
		List<SolvedTile> solvedTiles = solver.getSolution(arr);
		SudokuTile tile;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if (arr[i][j] == 0) {
					tile = new EmptyTile(solvedTiles.get(arr[i].length * i + j));
				} else {
					tile = solvedTiles.get(arr[i].length * i + j);
				}
				tiles.add(tile);
			}
		}
	}

	public void replaceTile(SudokuTile oldTile, SudokuTile newTile) {
		tiles.set(tiles.indexOf(oldTile), newTile);
	}

	public List<SudokuTile> getTiles() {
		return tiles;
	}

	public List<SudokuTile> getSolvedTiles() {
		return tiles.stream().map(tile -> new SolvedTile(tile.getSolution(), tile.getGrid(), tile.getRow(), tile.getColumn()))
						.collect(Collectors.toList());
	}

	public List<SudokuTile> getSameValue(SudokuTile tile) {
		return tiles.stream()
						.filter(s -> s.getValue() == tile.getValue())
						.collect(Collectors.toList());
	}

	public List<SudokuTile> getConflicts(SudokuTile t, int val) {
		return tiles.stream()
						.filter((s) -> ((s.getRow() == t.getRow() || s.getColumn() == t.getColumn() || s.getGrid() == t.getGrid()) && s.getValue() == val))
						.collect(Collectors.toList());
	}

	public boolean hasConflicts(SudokuTile t, int newValue) {
		return tiles.stream()
						.noneMatch(s -> ((s.getRow() == t.getRow() || s.getColumn() == t.getColumn() || s.getGrid() == t.getGrid()) && s.getValue() == newValue));
	}

	public boolean isSolved() {
		return tiles.stream().allMatch(tile -> tile.getValue() == tile.getSolution());
	}
}
