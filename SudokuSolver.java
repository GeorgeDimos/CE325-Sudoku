/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author George
 */
public class SudokuSolver{
    private final int[][] values;
    
    public SudokuSolver(int[][] array){
		values = new int[9][9];
		for (int i=0; i<9; i++){
			System.arraycopy(array[i], 0, values[i], 0, 9);
		}
    }
    
    public int[][] getValues(){
		if(solve()){
			return values;
		}
		return null;
    }
    
    private boolean solve(){
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (values[i][j] == 0) {
					for (int number = 1; number <10; number++) {
						if (allowed(i, j, number)) {
							values[i][j] = number;
							if(solve()){
								return true;
							} 
							else {
								values[i][j] = 0;
							}
						}
					}
					return false;
				}
			}
		}
		return true;
    }
    
    private boolean inRow(int row, int number){
		for(int i=0; i<9; i++){
			if(values[row][i]==number){
				return true;
			}
		}
		return false;
    }
    
    private boolean inColumn(int column, int number){
		for(int i=0; i<9; i++){
			if(values[i][column]==number){
				return true;
			}
		}
		return false;
    }
    
    private boolean inGrid(int row, int column, int number){
		int r = row - row % 3;
		int c = column - column % 3;

		for (int i = r; i < r + 3; i++)
			for (int j = c; j < c + 3; j++)
				if (values[i][j] == number)
					return true;

		return false;
    }
    
    private boolean allowed(int row, int column, int number){
		return !(inGrid(row, column, number) || inColumn(column, number) || inRow(row, number));
    }
    
}
