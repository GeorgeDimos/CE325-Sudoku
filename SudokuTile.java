/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import javax.swing.JTextField;

/**
 *
 * @author George
 */
public class SudokuTile {
    private final boolean canEdit;
    private int value;
    private final int gridNumber;
    private final int row;
    private final int column;
    private int solution;
    private final JTextField textField;
    
    public SudokuTile(boolean canEdit, int value, int gridNumber, int row, int column, JTextField textField){
	this.canEdit = canEdit;
	this.value = value;
	this.gridNumber = gridNumber;
	this.row = row;
	this.column = column;
	this.textField = textField;
	textField.setText(value!=0 ? String.valueOf(value) : "");
    }
    
    public SudokuTile backup(){
	return new SudokuTile(canEdit, value, gridNumber, row, column, textField);
    }
    
    public void setTextField(String text){
	textField.setText(text);
    }
    
    public void setValue(int value){
	if(canEdit){
	    this.value = value;
	}
    }
    
    public void setSolution(int solution){
	this.solution = solution;
    }
    
    public JTextField getTextField(){
	return textField;
    }
    
    public int getSolution(){
	return solution;
    }
    
    public int getGrid(){
	return gridNumber;
    }
    
    public int getRow(){
	return row;
    }
    
    public int getColumn(){
	return column;
    }
    
    public int getValue(){
	return value;
    }
    
    public boolean canEdit(){
	return canEdit;
    }
    
    public boolean isWrong(){
	return value!=solution && value!=0;
    }
    @Override
    public String toString(){
	return gridNumber+","+row+"," + column+": "+value+", " ;
    }
}
