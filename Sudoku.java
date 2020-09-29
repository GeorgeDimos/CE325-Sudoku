package sudoku;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_END;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author george
 */
public class Sudoku{
    /**
     * @param args the command line arguments
     */
    private static JTextField selectedField;
    private static JButton[] options;
    private static JCheckBox verifyButton;
    private static SudokuGrid board;
    private static Deque<UITile> backup;
    private static boolean verify;
    private static JTextField[][] fields;
    private static Map<JTextField, SudokuTile> mapTextfieldToTile;
    private static Map<SudokuTile, JTextField> mapTileToTextfield;

    public static void main(String[] args) {
	new Sudoku();
    }

    private Sudoku(){
	
	JFrame frame = new JFrame("Sudoku");
	frame.setLayout(new BorderLayout());
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	frame.setVisible(true);

	fields = new JTextField[9][9];

	mapTextfieldToTile = new LinkedHashMap<>();
	mapTileToTextfield = new LinkedHashMap<>();

	buildMenu(frame);

	buildNumbersPanel(frame);

	buildOptionsPanel(frame);

	frame.pack();
    }
    
    private void buildMenu(JFrame frame){

	JMenuBar menu = new JMenuBar();
	menu.setVisible(true);
	frame.setJMenuBar(menu);

	JMenu menuGame = new JMenu("New Game");
	menu.add(menuGame);

	JMenuItem menuEasy = new JMenuItem("Easy");
	menuEasy.addActionListener((ActionEvent e) -> {
		createGame(new File("src/sudoku/sudoku_easy.txt"));
	});

	JMenuItem menuInter = new JMenuItem("Itermediate");
	menuInter.addActionListener((ActionEvent e) -> {
		createGame(new File("src/sudoku/sudoku_medium.txt"));
	});

	JMenuItem menuExp = new JMenuItem("Expert");
	menuExp.addActionListener((ActionEvent e) -> {
		createGame(new File("src/sudoku/sudoku_hard.txt"));
	});

	menuGame.add(menuEasy);menuGame.add(menuInter);menuGame.add(menuExp);

    }
    
    private void resetGame(){
	backup = new ArrayDeque<>();
	mapTextfieldToTile.clear();
	mapTileToTextfield.clear();
    }
    
    private void resetUI(){
	verify = false;
	verifyButton.setSelected(false);

	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		int tileValue = mapTextfieldToTile.get(fields[i][j]).getValue();
		fields[i][j].setText(tileValue==0 ? "" : String.valueOf(tileValue));
		fields[i][j].addFocusListener(focus);
		fields[i][j].setFocusable(true);
	    }
	    options[i].setEnabled(true);
	}
	options[11].setEnabled(true);
	verifyButton.setEnabled(true);
    }
    
    private void disableUI(){
		
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		fields[i][j].setFocusable(false);
	    }
	}

	for(int i=0; i<12; i++){
	    options[i].setEnabled(false);
	}

	verifyButton.setEnabled(false);
    }
    
    private void createGame(File file){
	
	resetGame();

	Scanner in = null;
	try {
	    in = new Scanner(file);
	} catch (IOException ex) {
	    System.err.println("Input file error");
	}

	int[][] arr = new int[9][9];
	for(int i=0; i<9; i++){
	    int j=0;
	    for(String s : in.nextLine().split("\\s")){
		arr[i][j++] = Integer.valueOf(s);
	    }
	}
	in.close();

	board = new SudokuGrid(arr);

	for(int i=0; i<9; i++){
	    for (int j=0; j<9; j++){
		mapTextfieldToTile.put(fields[i/3*3+j/3][(i%3)*3+j%3], board.getTile(i, j));
		mapTileToTextfield.put(board.getTile(i, j), fields[i/3*3+j/3][(i%3)*3+j%3]);
	    }
	}

	resetUI();
	resetBackgroundColors();
    }

    private void buildNumbersPanel(JFrame frame){
	
	JPanel mainPanel = new JPanel(new GridLayout(3,3));
	frame.add(mainPanel, CENTER);

	JPanel[] grid = new JPanel[9];
	for(int i=0; i<9; i++){
	    grid[i] = new JPanel(new GridLayout(3,3));
	    grid[i].setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); 
	    for(int j=0; j<9; j++){
		grid[i].add(fields[i][j] = new JTextField());
		fields[i][j].setEditable(false);
	    }
	    mainPanel.add(grid[i]);
	}
    }

    private void buildOptionsPanel(JFrame frame){

	JPanel lowerPanel =  new JPanel(new FlowLayout());

	options = new JButton[12];

	for(int i=0; i<9; i++){
	    options[i] = new JButton(String.valueOf(i+1));
	    options[i].setMnemonic(97+i);
	    options[i].addActionListener(numberClicked);
	    lowerPanel.add(options[i]);
	}

	options[9] = new JButton();
	options[9].setIcon(new ImageIcon(new ImageIcon("src/sudoku/Images/eraser.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	options[9].addActionListener(erase);
	options[9].setEnabled(false);
	lowerPanel.add(options[9]);


	options[10] = new JButton();
	options[10].setIcon(new ImageIcon(new ImageIcon("src/sudoku/Images/undo.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	options[10].addActionListener(revert);
	options[10].setEnabled(false);
	lowerPanel.add(options[10]);

	verifyButton = new JCheckBox("Verify against solution");
	verifyButton.setEnabled(false);
	verifyButton.setRolloverEnabled(false);
	verifyButton.addItemListener((ItemEvent e) -> {
	    verify = verifyButton.getSelectedObjects() != null;
	    updateBackgroundColors();
	});
	lowerPanel.add(verifyButton);

	options[11] = new JButton();
	options[11].setIcon(new ImageIcon(new ImageIcon("src/sudoku/Images/rubik.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	options[11].setEnabled(false);
	options[11].addActionListener((ActionEvent e) -> {
	    board.solve();
	    for(int i=0;i<9; i++){
		for(int j=0; j<9; j++){
		    fields[i][j].setText(String.valueOf(mapTextfieldToTile.get(fields[i][j]).getValue()));
		}
	    }
	    selectedField = null;
	    resetBackgroundColors();
	    disableUI();
	});
	lowerPanel.add(options[11]);

	frame.add(lowerPanel, PAGE_END);
    }

    private void resetBackgroundColors(){
		
	for(int i=0;i<9; i++){
	    for(int j=0; j<9; j++){
		if(fields[i][j]==selectedField){continue;}

		SudokuTile currentTile = mapTextfieldToTile.get(fields[i][j]);
		if(currentTile.canEdit()){
		    if(verify && currentTile.isWrong()){
			fields[i][j].setBackground(Color.blue);
		    }
		    else{
			fields[i][j].setBackground(Color.white);
		    }
		}
		else{
		    fields[i][j].setBackground(Color.gray);
		}
	    }
	}
    }

    private void updateBackgroundColors(){
	resetBackgroundColors();

	SudokuTile selectedTile = mapTextfieldToTile.get(selectedField);
	if(selectedField==null || selectedTile.getValue()==0){
	    return;
	}

	board.getSameValue(selectedTile.getValue()).forEach((SudokuTile t) -> {
	    mapTileToTextfield.get(t).setBackground(new Color(255,255,0));
	});

	if(verify && selectedTile.isWrong()){
	    selectedField.setBackground(Color.blue);
	}
    }

    private final FocusListener focus = new FocusListener() {
	@Override
	public void focusGained(FocusEvent fe) {

	    selectedField = (JTextField)fe.getComponent();

	    SudokuTile selectedTile = mapTextfieldToTile.get(selectedField);
	    if(selectedTile.canEdit() && selectedTile.getValue()!=0){
		options[9].setEnabled(true);
	    }
	    else{
		options[9].setEnabled(false);
	    }
	    updateBackgroundColors();
	    selectedField.setBackground(new Color(255,255,0));
	}

	@Override
	public void focusLost(FocusEvent fe) {

	}
    };

    private final ActionListener numberClicked = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(selectedField==null){
		return;
	    }

	    SudokuTile selectedTile = mapTextfieldToTile.get(selectedField);

	    if(selectedTile.canEdit()==false){
		selectedField.setBackground(Color.red);
		return;
	    }

	    int newValue = Integer.valueOf(ae.getActionCommand());
	    if(board.canChange(selectedTile, newValue)){

		if(backup.isEmpty()){
		    options[10].setEnabled(true);
		}

		backup.push(new UITile(selectedTile));

		board.change(selectedTile, newValue);
		selectedField.setText(ae.getActionCommand());

		options[9].setEnabled(true);
		updateBackgroundColors();

		if(board.isSolved()){
		    JFrame victoryPopup = new JFrame("You win");
		    JTextField victoryMessage = new JTextField("You win!");
		    victoryMessage.setEditable(false);
		    victoryPopup.add(victoryMessage);
		    victoryPopup.setSize(200, 100);
		    victoryPopup.setVisible(true);
		    selectedField = null;
		    resetBackgroundColors();
		    disableUI();
		}
	    }
	    else{
		updateBackgroundColors();
		ArrayList<SudokuTile> conflicts = board.getConflicts(selectedTile, newValue);
		conflicts.stream().filter((t) -> (mapTileToTextfield.get(t)!=selectedField)).forEachOrdered((t) -> {
		    mapTileToTextfield.get(t).setBackground(Color.red);
		});
	    }
	}
    };

    private final ActionListener erase = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(selectedField==null){
		return;
	    }

	    SudokuTile selectedTile = mapTextfieldToTile.get(selectedField);
	    if(selectedTile.canEdit() && selectedTile.getValue()!=0){
		backup.push(new UITile(selectedTile));
		board.change(selectedTile, 0);
		selectedField.setText("");
		options[9].setEnabled(false);
	    }
	}
    };

    private final ActionListener revert = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    selectedField = null;

	    UITile oldTile = backup.pop();

	    SudokuTile tileToRevert = board.getTile(oldTile.getRow(), oldTile.getColumn());
	    int newValue = oldTile.getValue();
	    JTextField fieldToRevert = mapTileToTextfield.get(tileToRevert);

	    board.change(tileToRevert, newValue);
	    fieldToRevert.setText(newValue==0 ? "":String.valueOf(newValue));
	    if(newValue==0){
		options[9].setEnabled(false);
	    }
	    if(backup.isEmpty()){
		options[10].setEnabled(false);
	    }
	    updateBackgroundColors();
	}
    };
	
    private class UITile{
	private final int value, row, column;

	protected UITile(SudokuTile t){
	    this.value = t.getValue();
	    this.row = t.getRow();
	    this.column = t.getColumn();
	}

	protected int getRow(){
	    return row;
	}

	protected int getColumn(){
	    return column;
	}

	protected int getValue(){
	    return value;
	}
    }
}