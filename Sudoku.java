/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private static JTextField selected;
    private static JButton[] options;
    private static JCheckBox verifyButton;
    private static SudokuGrid board;
    private static Deque<SudokuTile> backup;
    private static boolean verify;
    private static JTextField[][] fields;
    private static Map<JTextField, SudokuTile> mapText2Tile;
    private static Map<SudokuTile, JTextField> mapTile2Text;

    public static void main(String[] args) {
	new Sudoku();
    }

    private Sudoku(){
	
	JFrame frame = new JFrame("Sudoku");
	frame.setLayout(new BorderLayout());
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	frame.setVisible(true);
	fields = new JTextField[9][9];
	mapText2Tile = new LinkedHashMap<>();
	mapTile2Text = new LinkedHashMap<>();
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
	    createGame(new File("sudoku_easy.txt"));
	});
	JMenuItem menuInter = new JMenuItem("Itermediate");
	menuInter.addActionListener((ActionEvent e) -> {
	    createGame(new File("sudoku_medium.txt"));
	});
	JMenuItem menuExp = new JMenuItem("Expert");
	menuExp.addActionListener((ActionEvent e) -> {
	    createGame(new File("sudoku_hard.txt"));
	});
	menuGame.add(menuEasy);menuGame.add(menuInter);menuGame.add(menuExp);

    }
    
    private void resetGame(){
	backup = new ArrayDeque<>();
    }
    
    private void resetUI(){
	verify = false;
	verifyButton.setSelected(false);

	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		fields[i][j].setText(mapText2Tile.get(fields[i][j]).getValue()==0 ? "" : String.valueOf(mapText2Tile.get(fields[i][j]).getValue()));
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
		mapText2Tile.put(fields[i/3*3+j/3][(i%3)*3+j%3], board.getTile(i, j));
		mapTile2Text.put(board.getTile(i, j), fields[i/3*3+j/3][(i%3)*3+j%3]);
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
	    for(int j=0; j<9; j++){
		grid[i].add(fields[i][j] = new JTextField());
		fields[i][j].setEditable(false);
	    }
	    grid[i].setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); 
	    mainPanel.add(grid[i]);
	}
    }

    private void buildOptionsPanel(JFrame frame){

	JPanel lowerPanel =  new JPanel(new FlowLayout());
	options = new JButton[12];
	for(int i=0; i<9; i++){
	    options[i] = new JButton(String.valueOf(i+1));
	    lowerPanel.add(options[i]);
	    options[i].setMnemonic(97+i);
	    options[i].addActionListener(numClicked);
	}

	options[9] = new JButton();
	options[9].setIcon(new ImageIcon(new ImageIcon("src/Sudoku/Images/eraser.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	lowerPanel.add(options[9]);
	options[9].addActionListener(erase);
	options[9].setEnabled(false);

	options[10] = new JButton();
	options[10].setIcon(new ImageIcon(new ImageIcon("src/Sudoku//Images/undo.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	lowerPanel.add(options[10]);
	options[10].addActionListener(revert);
	options[10].setEnabled(false);

	verifyButton = new JCheckBox("Verify against solution");
	lowerPanel.add(verifyButton);
	verifyButton.setEnabled(false);
	verifyButton.setRolloverEnabled(false);
	verifyButton.addItemListener((ItemEvent e) -> {
	    verify = verifyButton.getSelectedObjects() != null;
	    updateBackgroundColors();
	});
	

	options[11] = new JButton();
	options[11].setIcon(new ImageIcon(new ImageIcon("src/Sudoku/Images/rubik.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	lowerPanel.add(options[11]);
	options[11].setEnabled(false);
	options[11].addActionListener((ActionEvent e) -> {
	    board.solve();
	    for(int i=0;i<9; i++){
		for(int j=0; j<9; j++){
		    fields[i][j].setText(String.valueOf(mapText2Tile.get(fields[i][j]).getValue()));
		}
	    }
	    selected = null;
	    resetBackgroundColors();
	    disableUI();
	});

	frame.add(lowerPanel, PAGE_END);
    }

    private void resetBackgroundColors(){
	for(int i=0;i<9; i++){
	    for(int j=0; j<9; j++){
		if(fields[i][j]==selected){continue;}
		if(mapText2Tile.get(fields[i][j]).canEdit()){
		    if(verify && mapText2Tile.get(fields[i][j]).isWrong()){
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
	if(selected==null || mapText2Tile.get(selected).getValue()==0){
	    return;
	}
	board.getSameValue(mapText2Tile.get(selected).getValue()).forEach((SudokuTile t) -> {
	    mapTile2Text.get(t).setBackground(new Color(255,255,0));
	});
	if(verify && mapText2Tile.get(selected).isWrong()){selected.setBackground(Color.blue);}
	}

    private final FocusListener focus = new FocusListener() {
	@Override
	public void focusGained(FocusEvent fe) {
	    selected = (JTextField)fe.getComponent();
	    if(mapText2Tile.get(selected).canEdit()&& mapText2Tile.get(selected).getValue()!=0){
		options[9].setEnabled(true);
	    }
	    else{
		options[9].setEnabled(false);
	    }
	    updateBackgroundColors();
	    selected.setBackground(new Color(255,255,0));
	}

	@Override
	public void focusLost(FocusEvent fe) {
	
	}
    };

    private final ActionListener numClicked = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(selected==null){
		return;
	    }
	    if(!mapText2Tile.get(selected).canEdit()){
		selected.setBackground(Color.red);
		return;
	    }
	    if(board.canChange(mapText2Tile.get(selected), Integer.valueOf(ae.getActionCommand()))){
		if(backup.isEmpty()){
		    options[10].setEnabled(true);
		}
		backup.push(mapText2Tile.get(selected).backup());
		board.change(mapText2Tile.get(selected), Integer.valueOf(ae.getActionCommand()));
		selected.setText(ae.getActionCommand());
		options[9].setEnabled(true);
		updateBackgroundColors();
	    }
	    else{
		updateBackgroundColors();
		ArrayList<SudokuTile> conf = board.getConflicts(mapText2Tile.get(selected), Integer.valueOf(ae.getActionCommand()));
		conf.stream().filter((t) -> (mapTile2Text.get(t)!=selected)).forEachOrdered((t) -> {
		    mapTile2Text.get(t).setBackground(Color.red);
		});
	    }
	}
    };

    private final ActionListener erase = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(selected==null){
		return;
	    }
	    if(mapText2Tile.get(selected).canEdit() && mapText2Tile.get(selected).getValue()!=0){
		backup.push(mapText2Tile.get(selected).backup());
		board.change(mapText2Tile.get(selected),0);
		selected.setText("");
		options[9].setEnabled(false);
	    }
	}
    };

    private final ActionListener revert = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    selected = null;
	    SudokuTile t = backup.pop();
	    board.restore(t);
	    mapTile2Text.get(board.getTile(t.getRow(),t.getColumn())).setText(t.getValue()!=0 ? String.valueOf(t.getValue()): "");
	    if(backup.isEmpty()){
		options[10].setEnabled(false);
	    }
	    updateBackgroundColors();
	}
    };
    
}
