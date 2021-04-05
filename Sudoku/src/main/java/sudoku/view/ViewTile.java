package sudoku.view;

import sudoku.model.tiles.SudokuTile;
import sudoku.view.backgroundcolor.BackgroundColorState;
import sudoku.view.backgroundcolor.NormalBackgroundState;

import javax.swing.*;
import java.awt.*;

public class ViewTile {

	private final JTextField textField;
	BackgroundColorState state;
	private SudokuTile tile;

	public ViewTile(JTextField textField) {
		this.textField = textField;
		state = new NormalBackgroundState(this);
	}

	public int getValue() {
		return tile.getValue();
	}

	public int getSolution() {
		return tile.getSolution();
	}

	public String getText() {
		return tile.getText();
	}

	public Color getDefaultBackgroundColor() {
		return tile.getDefaultBackgroundColor();
	}

	public Color getBackgroundColor() {
		return state.getBackgroundColor();
	}

	public void setBackgroundColorState(BackgroundColorState state) {
		this.state = state;
	}

	public void setTile(SudokuTile tile) {
		this.tile = tile;
		textField.setText(this.getText());
		textField.setBackground(this.getBackgroundColor());
	}

	public SudokuTile getSudokuTile() {
		return tile;
	}

	public JTextField getTextField() {
		return textField;
	}
}
