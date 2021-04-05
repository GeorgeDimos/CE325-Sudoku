package sudoku;

import sudoku.controller.SudokuController;
import sudoku.model.SudokuGrid;
import sudoku.solver.SudokuSolver;
import sudoku.view.SudokuView;

/**
 * @author george
 */
public class Sudoku {
	private Sudoku() {

		SudokuView view = new SudokuView();
		SudokuSolver solver = new SudokuSolver();
		SudokuGrid grid = new SudokuGrid(solver);
		new SudokuController(view, grid);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new Sudoku();
	}

}
