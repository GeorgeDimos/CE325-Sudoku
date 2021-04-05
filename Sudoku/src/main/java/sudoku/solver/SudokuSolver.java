package sudoku.solver;

import sudoku.model.tiles.EmptyTile;
import sudoku.model.tiles.SolvedTile;
import sudoku.model.tiles.SudokuTile;
import sudoku.model.tiles.SudokuVariableTile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuSolver {

	private final List<SudokuTile> tiles;
	private int[][] input;

	public SudokuSolver() {
		tiles = new ArrayList<>();
	}

	private void parse() {
		SudokuTile tile;
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				if (input[i][j] == 0) {
					tile = new EmptyTile(new SolvedTile(input[i][j], (j / 3) + (i / 3) * 3, i, j));
				} else {
					tile = new SolvedTile(input[i][j], (j / 3) + (i / 3) * 3, i, j);
				}
				tiles.add(tile);
			}
		}
	}

	public List<SolvedTile> getSolution(int[][] input) {
		this.input = input;
		parse();
		findSolution();
		return tiles.stream()
						.map(tile -> new SolvedTile(tile.getValue(), tile.getGrid(), tile.getRow(), tile.getColumn()))
						.collect(Collectors.toList());
	}

	public boolean canChange(SudokuTile t, int newVal) {
		return t.canChange() && tiles.stream()
						.noneMatch((s) -> ((s.getRow() == t.getRow() || s.getColumn() == t.getColumn() || s.getGrid() == t.getGrid()) && s.getValue() == newVal));
	}

	public void change(SudokuTile t, int newVal) {
		tiles.set(tiles.indexOf(t), new SudokuVariableTile(t, newVal));
	}

	private boolean findSolution() {
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				SudokuTile sudokuTile = tiles.get(input[i].length * i + j);
				if (sudokuTile.getValue() == 0) {
					for (int k = 1; k < 10; k++) {
						if (canChange(sudokuTile, k) && sudokuTile.canChange()) {
							change(tiles.get(input[i].length * i + j), k);
							if (findSolution()) {
								return true;
							}
							change(tiles.get(input[i].length * i + j), 0);
						}
					}
					return false;
				}
			}
		}
		return true;
	}
}
