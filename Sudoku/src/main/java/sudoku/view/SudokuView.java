package sudoku.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_END;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SudokuView {

	public final JCheckBox verifyButton;
	public final JButton[] numberButtons;
	public final JButton solveButton;
	public final JButton undoButton;
	public final JButton eraseButton;
	private final JMenuItem easyOption;
	private final JMenuItem mediumOption;
	private final JMenuItem hardOption;
	public JTextField[][] fields;


	public SudokuView() {

		JFrame frame = new JFrame("Sudoku");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);


		JMenuBar menu = new JMenuBar();
		menu.setVisible(true);
		frame.setJMenuBar(menu);

		JMenu menuGame = new JMenu("File");
		menu.add(menuGame);

		JMenu newGame = new JMenu("New Game");

		easyOption = new JMenuItem("Easy");

		mediumOption = new JMenuItem("Medium");

		hardOption = new JMenuItem("Hard");

		newGame.add(easyOption);
		newGame.add(mediumOption);
		newGame.add(hardOption);

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener((ActionEvent e) ->
						System.exit(0)
		);

		menuGame.add(newGame);
		menuGame.add(exit);

		JPanel mainPanel = new JPanel(new GridLayout(3, 3));
		frame.add(mainPanel, CENTER);

		JPanel[] grid = new JPanel[9];
		for (int i = 0; i < 9; i++) {
			grid[i] = new JPanel(new GridLayout(3, 3));
			grid[i].setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			mainPanel.add(grid[i]);
		}

		fields = new JTextField[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				fields[i][j] = new JTextField("");
				fields[i][j].setHorizontalAlignment(JTextField.CENTER);
				Font font1 = new Font("SansSerif", Font.BOLD, 30);
				fields[i][j].setFont(font1);
				grid[(j / 3) + (i / 3) * 3].add(fields[i][j]);
				fields[i][j].setEditable(false);
				fields[i][j].setFocusable(false);
			}
		}

		JPanel lowerPanel = new JPanel(new FlowLayout());

		numberButtons = new JButton[9];

		for (int i = 0; i < 9; i++) {
			numberButtons[i] = new JButton(String.valueOf(i + 1));
			numberButtons[i].setMnemonic(97 + i);
			numberButtons[i].setEnabled(false);
			lowerPanel.add(numberButtons[i]);
		}

		eraseButton = new JButton();
		eraseButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage(Thread.currentThread().getContextClassLoader().getResource("Images/eraser.png"))
						.getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
		eraseButton.setEnabled(false);
		lowerPanel.add(eraseButton);

		undoButton = new JButton();
		undoButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage(Thread.currentThread().getContextClassLoader().getResource("Images/undo.png"))
						.getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
		undoButton.setEnabled(false);
		lowerPanel.add(undoButton);

		verifyButton = new JCheckBox("Verify against solution");
		verifyButton.setEnabled(false);
		verifyButton.setRolloverEnabled(false);
		lowerPanel.add(verifyButton);

		solveButton = new JButton();
		solveButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage(Thread.currentThread().getContextClassLoader().getResource("Images/rubik.png"))
						.getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
		solveButton.setEnabled(false);

		lowerPanel.add(solveButton);

		frame.add(lowerPanel, PAGE_END);

		frame.pack();
	}

	public void addEasyOptionListener(ActionListener listener) {
		easyOption.addActionListener(listener);
	}

	public void addMediumOptionListener(ActionListener listener) {
		mediumOption.addActionListener(listener);
	}

	public void addHardOptionListener(ActionListener listener) {
		hardOption.addActionListener(listener);
	}

	public void addEraseListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
	}

	public void addUndoListener(ActionListener listener) {
		undoButton.addActionListener(listener);
	}

	public void addVerificationListener(ItemListener listener) {
		verifyButton.addItemListener(listener);
	}

	public void addSolutionListener(ActionListener listener) {
		solveButton.addActionListener(listener);
	}

	public void addNumberListener(ActionListener listener) {
		for (int i = 0; i < 9; i++) {
			numberButtons[i].addActionListener(listener);
		}
	}

	public void addFieldsFocusListener(FocusListener listener) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				fields[i][j].addFocusListener(listener);
			}
		}
	}
}
