package sudoku;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Sets the Sudoku grid with the  input values parsed row by row.
 * @author George
 */

public class SudokuGrid {
    private final SudokuTile[][] tiles;
    private final ArrayList<ArrayList<SudokuTile>> grid;
    private final ArrayList<ArrayList<SudokuTile>> sameValueTiles;
    private int filled;

    public SudokuGrid(int arr[][]) {
        tiles = new SudokuTile[9][9];
        grid = new ArrayList<ArrayList<SudokuTile>>(9);
        sameValueTiles = new ArrayList<ArrayList<SudokuTile>>(9);
        filled = 0;

        for (int i = 0; i < 9; i++) {
            grid.add(i, new ArrayList<>());
            sameValueTiles.add(i,new ArrayList<>());
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tiles[i][j] = new SudokuTile((arr[i][j] == 0), arr[i][j], (j / 3) + (i / 3) * 3, i, j);
                grid.get((j / 3) + (i / 3) * 3).add(tiles[i][j]);
                if (arr[i][j] != 0) {
                    sameValueTiles.get(arr[i][j] - 1).add(tiles[i][j]);
                    filled++;
                }
            }
        }

        getSolution();

    }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    public SudokuTile getTile(int row, int column) {
        return tiles[row][column];
    }

    /**
     * Find all the conflicting tiles, ie those with the same value
     * found in the same grid or row or column.
     * @param t
     * @param val
     * @return
     */
    public ArrayList<SudokuTile> getConflicts(SudokuTile t, int val) {
        ArrayList<SudokuTile> conf = new ArrayList<SudokuTile>();
        sameValueTiles.get(val - 1).stream().filter(
                (s) -> (s.getRow() == t.getRow() || s.getColumn() == t.getColumn() || s.getGrid() == t.getGrid()))
                .forEachOrdered((s) -> {
                    conf.add(s);
                });
        return conf;
    }

    /**
     * Finds all the tiles with the same value
     * @param val
     * @return
     */
    public Collection<SudokuTile> getSameValue(int val) {
        if (val == 0) {
            return null;
        }
        return sameValueTiles.get(val - 1);
    }

    /**
     * Checks if the tile can be changed to the new value.
     * @param t
     * @param newVal
     * @return
     */
    public boolean canChange(SudokuTile t, int newVal) {
        return t.canEdit() && grid.get(t.getGrid()).stream().noneMatch((s) -> (s.getValue() == newVal))
                && (sameValueTiles.get(newVal - 1).stream()
                        .noneMatch((s) -> (s.getRow() == t.getRow() || s.getColumn() == t.getColumn())));
    }

    /**
     * Should be called only if canChange returned true.
     * @param t
     * @param newVal
     */
    public void change(SudokuTile t, int newVal) {

        if (t.getValue() != 0) {
            sameValueTiles.get(t.getValue() - 1).remove(t);
        } else {
            filled++;
        }

        t.setValue(newVal);

        if (newVal != 0) {
            sameValueTiles.get(newVal - 1).add(t);
        } else {
            filled--;
        }
    }

    /**
     * Checks if all tiles have been filled
     * @return
     */
    public boolean isSolved() {
        return filled == 81;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                str.append(tiles[i][j].toString()).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Changes all tiles' values to the correct one
     */
    public void solve() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                change(tiles[i][j], tiles[i][j].getSolution());
            }
        }
    }

    /**
     * Calculates solution, temporarily saving the correct value to the
     * "value" field of the tiles in order to use the current methods.
     * Once calculated the solutions' values are saved to the proper field
     * and the original values are restored.
     */
    private void getSolution() {

        findSolution();
        swap();
    }

    /**
     * Brute force backtracking algorithm
     * @return
     */
    private boolean findSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tiles[i][j].getValue() == 0) {
                    for (int k = 1; k < 10; k++) {
                        if (canChange(tiles[i][j], k)) {
                            change(tiles[i][j], k);
                            if (findSolution()) {
                                return true;
                            }
                            change(tiles[i][j], 0);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     */
    private void swap() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int temp = tiles[i][j].getValue();
                change(tiles[i][j], tiles[i][j].getSolution());
                tiles[i][j].setSolution(temp);
            }
        }
    }

}
