/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author George
 */

public class SudokuGrid {
    private final SudokuTile[][] tiles;
    private final Collection<SudokuTile>[] grid;
    private final Collection<SudokuTile>[] nums;
	private int filled;
    
    public SudokuGrid(int arr[][]){
		tiles = new SudokuTile[9][9];
		grid = new ArrayList[9];
		nums = new ArrayList[9];
		filled = 0;

		for(int i=0;i<9;i++){
			grid[i] = new ArrayList();
			nums[i] = new ArrayList();
		}

		for(int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				tiles[i][j] = new SudokuTile((arr[i][j]==0), arr[i][j], (j/3)+(i/3)*3, i, j);
				grid[(j/3)+(i/3)*3].add(tiles[i][j]);
				if(arr[i][j]!=0){
					nums[arr[i][j]-1].add(tiles[i][j]);
					filled++;
				}
			}
		}

		getSolution();
	
    }
    
    public int[][] getValuesArray(){
		int[][] values = new int[9][9];
		for(int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				values[i][j] = tiles[i][j].getValue();
			}
		}
		return values;
    }
    
    public SudokuTile getTile(int row, int column){
		return tiles[row][column];
    }
    
    public ArrayList getConflicts(SudokuTile t, int val){
		ArrayList conf = new ArrayList();
		nums[val-1].stream().filter((s) -> (s.getRow()==t.getRow()||s.getColumn()==t.getColumn()||s.getGrid()==t.getGrid())).forEachOrdered((s) -> {
			conf.add(s);
		});
		return conf;
    }
    
    public Collection<SudokuTile> getSameValue(int val){
		if(val==0){
			return null;
		}
		return nums[val-1];
    }
    
    public boolean canChange(SudokuTile t, int newVal){
		return t.canEdit() && 
			grid[t.getGrid()].stream().noneMatch((s) -> (s.getValue()==newVal)) && 
			(nums[newVal-1].stream().noneMatch((s) -> (s.getRow()==t.getRow() || s.getColumn()==t.getColumn())));
    }
    
    public void change(SudokuTile t, int newVal){

		if(t.getValue()!=0){
			nums[t.getValue()-1].remove(t);
		}
		else{
			filled++;
		}
		
		t.setValue(newVal);
		
		if(newVal!=0){
			nums[newVal-1].add(t);
		}
		else{
			filled--;
		}
    }
	
	public boolean isSolved(){
		return filled==81;
	}
        
    @Override
    public String toString(){
		StringBuilder str = new StringBuilder();
		for(int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				str.append(tiles[i][j].toString()).append(" ");
			}
			str.append("\n");
		}
		return str.toString();
    }
    
    public void solve(){
		for(int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				change(tiles[i][j], tiles[i][j].getSolution());
			}
		}
    }
    
    private void getSolution(){
		SudokuSolver s = new SudokuSolver(getValuesArray());
		if(s.getValues()!=null){
			int[][] array = s.getValues();
			for(int i=0; i<9; i++){
				for (int j=0; j<9; j++){
					tiles[i][j].setSolution(array[i][j]);
				}
			}
		}
    }
    
}