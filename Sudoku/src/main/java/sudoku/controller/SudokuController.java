package sudoku.controller;

import sudoku.controller.commands.CantChangeException;
import sudoku.controller.commands.ChangeTile;
import sudoku.controller.commands.Command;
import sudoku.controller.commands.EraseTile;
import sudoku.model.SudokuGrid;
import sudoku.model.tiles.SudokuTile;
import sudoku.view.SudokuView;
import sudoku.view.ViewTile;
import sudoku.view.backgroundcolor.NormalBackgroundState;
import sudoku.view.backgroundcolor.VerificationBackgroundState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.List;
import java.util.*;

public class SudokuController {

	private final SudokuView view;
	private final SudokuGrid grid;
	private final static Color SELECTED = new Color(255, 255, 0);
	private final static Color ERROR = new Color(255, 0, 0);
	private JTextField selectedField;
	private Deque<Command> history;
	private Map<JTextField, ViewTile> textFieldToViewTileMap;

	public SudokuController(SudokuView view, SudokuGrid grid) {
		this.view = view;
		this.grid = grid;

		view.addEasyOptionListener(new GameOptionListener("Games/easy.txt"));
		view.addMediumOptionListener(new GameOptionListener("Games/medium.txt"));
		view.addHardOptionListener(new GameOptionListener("Games/hard.txt"));
		view.addEraseListener(new EraseActionListener());
		view.addFieldsFocusListener(new FieldsFocusListener());
		view.addSolutionListener(new SolutionListener());
		view.addNumberListener(new NumbersListener());
		view.addVerificationListener(new VerificationListener());
		view.addUndoListener(new UndoListener());
	}

	private void updateBackgroundColors() {
		textFieldToViewTileMap.values().forEach(viewTile -> viewTile.getTextField().setBackground(viewTile.getBackgroundColor()));
	}

	public void populateView(List<SudokuTile> tiles) {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				ViewTile viewTile = new ViewTile(view.fields[i][j]);
				viewTile.setTile(tiles.get(9 * i + j));
				textFieldToViewTileMap.put(view.fields[i][j], viewTile);
				view.fields[i][j].setFocusable(true);
				textFieldToViewTileMap.put(view.fields[i][j], viewTile);
			}
			view.numberButtons[i].setEnabled(true);
		}

		view.verifyButton.setSelected(false);
		view.verifyButton.setEnabled(true);
		view.solveButton.setEnabled(true);
	}

	private int[][] getInput(InputStream input) {

		Scanner in = new Scanner(input);
		int[][] arr = new int[9][9];
		try {
			for (int i = 0; i < 9; i++) {
				int j = 0;
				for (String s : in.nextLine().split("\\s")) {
					arr[i][j++] = Integer.parseInt(s);
				}
			}
		} catch (Exception ex) {
			System.err.println("Invalid File");
		}
		in.close();
		return arr;
	}

	private void disableUI() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				view.fields[i][j].setFocusable(false);
			}
			view.numberButtons[i].setEnabled(false);
		}
		view.verifyButton.setEnabled(false);
		view.solveButton.setEnabled(false);
		view.undoButton.setEnabled(false);
		view.eraseButton.setEnabled(false);
	}

	private class GameOptionListener implements ActionListener {
		private final String file;

		public GameOptionListener(String file) {
			this.file = file;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
			int[][] input = getInput(inputStream);
			history = new LinkedList<>();
			textFieldToViewTileMap = new HashMap<>();
			grid.populateTiles(input);
			populateView(grid.getTiles());
		}
	}

	private class FieldsFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			updateBackgroundColors();
			selectedField = (JTextField) e.getComponent();
			selectedField.setBackground(SELECTED);
			if (!selectedField.getText().isEmpty()) {
				view.eraseButton.setEnabled(true);
				grid.getSameValue(textFieldToViewTileMap.get(selectedField).getSudokuTile()).forEach((t) ->
								view.fields[t.getRow()][t.getColumn()].setBackground(SELECTED));
			} else {
				view.eraseButton.setEnabled(false);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
		}

	}

	private class SolutionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			populateView(grid.getSolvedTiles());
			disableUI();
		}
	}

	private class EraseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				Command erase = new EraseTile(grid, textFieldToViewTileMap.get(selectedField));
				erase.execute();
				history.push(erase);

				updateBackgroundColors();
			} catch (CantChangeException ex) {
				selectedField.setBackground(ERROR);
			}
		}

	}

	private class NumbersListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updateBackgroundColors();
			if (selectedField == null) {
				return;
			}

			int newValue = Integer.parseInt(e.getActionCommand());

			try {
				Command change = new ChangeTile(grid, textFieldToViewTileMap.get(selectedField), newValue);
				change.execute();
				if (history.isEmpty())
					view.undoButton.setEnabled(true);

				history.push(change);

				if (grid.isSolved()) {
					JFrame victoryPopup = new JFrame("You win");
					JTextField victoryMessage = new JTextField("You win!");
					victoryMessage.setEditable(false);
					victoryPopup.add(victoryMessage);
					victoryPopup.setSize(200, 100);
					victoryPopup.setVisible(true);
					disableUI();
				}

			} catch (CantChangeException ex) {
				for (SudokuTile conflict : grid.getConflicts(textFieldToViewTileMap.get(selectedField).getSudokuTile(), newValue)) {
					view.fields[conflict.getRow()][conflict.getColumn()].setBackground(ERROR);
				}
				selectedField.setBackground(SELECTED);
			}
		}
	}

	private class VerificationListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			textFieldToViewTileMap.values().forEach(viewTile -> {
				if (e.getStateChange() == ItemEvent.SELECTED)
					viewTile.setBackgroundColorState(new VerificationBackgroundState(viewTile));
				else
					viewTile.setBackgroundColorState(new NormalBackgroundState(viewTile));
			});
			updateBackgroundColors();
		}

	}

	private class UndoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!history.isEmpty()) {
				Command cmd = history.pop();
				cmd.undo();
				if (history.isEmpty()) view.undoButton.setEnabled(false);
				updateBackgroundColors();
			}
		}
	}
}
