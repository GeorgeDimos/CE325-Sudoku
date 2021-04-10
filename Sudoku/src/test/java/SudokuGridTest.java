import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sudoku.model.tiles.SolvedTile;
import sudoku.solver.SudokuSolver;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuGridTest {

	SudokuSolver solver = new SudokuSolver();;

	@Test
	public void shouldAnswerWithTrue() {
		assertTrue(true);
	}

	@Test
	public void RightAnswerShouldSucceed() {
		int[][] input = {
						{0, 0, 0, 0, 0, 0, 0, 3, 0},
						{0, 2, 0, 7, 0, 0, 1, 0, 5},
						{0, 0, 4, 0, 8, 5, 9, 0, 0},
						{0, 0, 0, 6, 0, 0, 0, 7, 0},
						{2, 5, 0, 0, 0, 0, 0, 9, 4},
						{0, 6, 0, 0, 0, 9, 0, 0, 0},
						{0, 0, 8, 3, 6, 0, 5, 0, 0},
						{3, 0, 5, 0, 0, 1, 0, 2, 0},
						{0, 4, 0, 0, 0, 0, 0, 0, 0}
		};

		int[][] solution = {
						{5, 8, 1, 9, 2, 6, 4, 3, 7},
						{9, 2, 6, 7, 3, 4, 1, 8, 5},
						{7, 3, 4, 1, 8, 5, 9, 6, 2},
						{4, 1, 9, 6, 5, 3, 2, 7, 8},
						{2, 5, 3, 8, 1, 7, 6, 9, 4},
						{8, 6, 7, 2, 4, 9, 3, 5, 1},
						{1, 7, 8, 3, 6, 2, 5, 4, 9},
						{3, 9, 5, 4, 7, 1, 8, 2, 6},
						{6, 4, 2, 5, 9, 8, 7, 1, 3}
		};

		int[][] temp = new int[9][9];
		List<SolvedTile> solution1 = solver.getSolution(input);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				temp[i][j] = solution1.get(9*i+j).getValue();
			}
		}

		assertArrayEquals(solution, temp);
	}

	@Test
	public void WrongAnswerShouldFail() {
		int[][] input = {
						{0, 0, 0, 0, 0, 0, 0, 3, 0},
						{0, 2, 0, 7, 0, 0, 1, 0, 5},
						{0, 0, 4, 0, 8, 5, 9, 0, 0},
						{0, 0, 0, 6, 0, 0, 0, 7, 0},
						{2, 5, 0, 0, 0, 0, 0, 9, 4},
						{0, 6, 0, 0, 0, 9, 0, 0, 0},
						{0, 0, 8, 3, 6, 0, 5, 0, 0},
						{3, 0, 5, 0, 0, 1, 0, 2, 0},
						{0, 4, 0, 0, 0, 0, 0, 0, 0}
		};

		int[][] solution = {
						{5, 8, 1, 9, 2, 6, 4, 3, 7},
						{9, 2, 6, 7, 3, 4, 1, 8, 5},
						{7, 3, 4, 1, 8, 5, 9, 6, 2},
						{4, 1, 9, 6, 5, 3, 2, 7, 8},
						{2, 5, 3, 8, 1, 7, 6, 9, 4},
						{8, 6, 7, 2, 4, 9, 3, 5, 1},
						{1, 7, 8, 3, 6, 2, 5, 4, 9},
						{3, 9, 5, 4, 7, 1, 8, 2, 6},
						{6, 4, 2, 5, 9, 8, 7, 1, 2}
		};

		int[][] temp = new int[9][9];
		List<SolvedTile> solution1 = solver.getSolution(input);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				temp[i][j] = solution1.get(9*i+j).getValue();
			}
		}

		assertFalse(Arrays.deepEquals(solution, temp));
	}
}
