package sudoku;

/**
 * Represents a tile from a sudoku. Has a value, a row, a column and a grid.
 * Can be set as editable or uneditable and has a field for the correct value.
 * @author George
 */
public class SudokuTile {
    private final boolean canEdit;
    private int value;
    private final int gridNumber;
    private final int row;
    private final int column;
    private int solution;

    public SudokuTile(boolean canEdit, int value, int gridNumber, int row, int column) {
        this.canEdit = canEdit;
        this.value = value;
        this.gridNumber = gridNumber;
        this.row = row;
        this.column = column;
        this.solution = value;
    }

    /**
     * Sets the value if the tile can be edited
     * @param value
     */
    public void setValue(int value) {
        if (canEdit) {
            this.value = value;
        }
    }

    /**
     * Sets the solution value
     * @param solution
     */
    public void setSolution(int solution) {
        this.solution = solution;
    }

    /**
     * Gets the value from solution field
     * @return
     */
    public int getSolution() {
        return solution;
    }

    /**
     * Gets the grid number
     * @return
     */
    public int getGrid() {
        return gridNumber;
    }

    /**
     * Gets the row number
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column number
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets the current value
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Editable or not
     * @return
     */
    public boolean canEdit() {
        return canEdit;
    }

    /**
     * Checks if the current value is correct
     * @return
     */
    public boolean isWrong() {
        return value != solution && value != 0;
    }

    @Override
    public String toString() {
        return gridNumber + "," + row + "," + column + ": " + value + ", ";
    }
}
