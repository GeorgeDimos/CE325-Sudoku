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
    private static Deque<int[][]> backup;
    private static boolean verify;
    private static JTextField[][] fields;

    public static void main(String[] args) {

	
	
	runGUI();
    }

    private static void runGUI(){
	JFrame frame = new JFrame("Sudoku");
	frame.setLayout(new BorderLayout());
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	frame.setVisible(true);

	buildMenu(frame);

	buildNumbersPanel(frame);

	buildOptionsPanel(frame);

	frame.pack();
    }
    
    private static void buildMenu(JFrame frame){

	JMenuBar menu = new JMenuBar();
	menu.setVisible(true);
	frame.setJMenuBar(menu);

	JMenu menuGame = new JMenu("New Game");
	menu.add(menuGame);

	JMenuItem menuEasy = new JMenuItem("Easy");
	menuEasy.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		createGame(new File("sudoku_easy.txt"));
	    }
	    
	});
	JMenuItem menuInter = new JMenuItem("Itermediate");
	menuInter.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		createGame(new File("sudoku_medium.txt"));
	    }
	    
	});
	JMenuItem menuExp = new JMenuItem("Expert");
	menuExp.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		createGame(new File("sudoku_hard.txt"));
	    }
	    
	});
	menuGame.add(menuEasy);menuGame.add(menuInter);menuGame.add(menuExp);

    }
    
    private static void createGame(File file){
	backup = new ArrayDeque<>();
	verify = false;
	verifyButton.setSelected(false);
	Scanner in = null;
	try {
	    in = new Scanner(file);
	} catch (IOException ex) {
	    System.err.println("Input file error");
	}

	int[][] arr = new int[9][9];
	int k=0, l=0;
	while(in.hasNextLine()){
	    for(String s : in.nextLine().split("\\s")){
		arr[k][l++] = Integer.valueOf(s);
	    }
	    l=0;
	    k++;
	}

	in.close();
	board = new SudokuGrid(arr, fields);
	for(int i=0; i<9; i++){
	    options[i].setEnabled(true);
	    for(int j=0; j<9; j++){
		fields[i][j].addFocusListener(focus);
		fields[i][j].setFocusable(true);
	    }
	}
	options[11].setEnabled(true);
	verifyButton.setEnabled(true);
	resetBackgroundColors();
    }


    private static void buildNumbersPanel(JFrame frame){
	fields = new JTextField[9][9];
	JPanel mainPanel = new JPanel(new GridLayout(3,3));
	frame.add(mainPanel, CENTER);

	JPanel[] grid = new JPanel[9];
	for(int i=0; i<9; i++){
	    grid[i] = new JPanel(new GridLayout(3,3));
	    for(int j=0; j<9; j++){
		grid[i].add(fields[i][j] = new JTextField());
		fields[i][j].setEditable(false);
	    }
	    grid[i].setBorder(BorderFactory.createEmptyBorder(5,2,5,2)); 
	    mainPanel.add(grid[i]);
	}
    }

    private static void buildOptionsPanel(JFrame frame){

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
	verifyButton.setRolloverEnabled(false);
	verifyButton.addItemListener((ItemEvent e) -> {
	    verify = verifyButton.getSelectedObjects() != null;
	    resetBackgroundColors();
	});
	

	options[11] = new JButton();
	options[11].setIcon(new ImageIcon(new ImageIcon("src/Sudoku/Images/rubik.png").getImage().getScaledInstance(10, 15, Image.SCALE_DEFAULT)));
	lowerPanel.add(options[11]);
	options[11].addActionListener((ActionEvent e) -> {
	    for(int i=0; i<9; i++){
		for(int j=0; j<9; j++){
		    board.getTile(i, j).setValue(board.getTile(i, j).getSolution());
		    board.getTile(i, j).setTextField(String.valueOf(board.getTile(i, j).getSolution()));
		    board.getTile(i, j).getTextField().setFocusable(false);
		}
	    }
	    for(int i=0; i<12; i++){	    
		options[i].setEnabled(false);
	    }
	    verifyButton.setEnabled(false);
	});

	frame.add(lowerPanel, PAGE_END);
    }

    private static void resetBackgroundColors(){
	for(int i=0;i<9; i++){
	    for(int j=0; j<9; j++){
		if(board.getTile(i,j).getTextField()==selected){continue;}
		if(board.getTile(i,j).canEdit()){
		    if(verify && board.getTile(i,j).isWrong()){
			board.getTile(i,j).getTextField().setBackground(Color.blue);
		    }
		    else{
			board.getTile(i,j).getTextField().setBackground(Color.white);
		    }
		}
		else{
		    board.getTile(i,j).getTextField().setBackground(Color.gray);
		}
	    }
	}
    }

    private static void updateBackgroundColors(){
	resetBackgroundColors();
	if(selected==null || board.getTile(selected).getValue()==0){
	    return;
	}
	board.getSameValue(board.getTile(selected).getValue()).forEach((t) -> {
	    t.getTextField().setBackground(new Color(255,255,0));
	});
    }

    private static final FocusListener focus = new FocusListener() {
	@Override
	public void focusGained(FocusEvent fe) {
	    selected = (JTextField)fe.getComponent();
	    selected.setBackground(new Color(255,255,0));
	    if(board.getTile(selected).canEdit()&& board.getTile(selected).getValue()!=0){
		options[9].setEnabled(true);
	    }
	    else{
		options[9].setEnabled(false);
	    }
	    updateBackgroundColors();
	}

	@Override
	public void focusLost(FocusEvent fe) {
	
	}
    };

    private static final ActionListener numClicked = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(selected==null){
		return;
	    }
	    if(!board.getTile(selected).canEdit()){
		return;
	    }
	    if(board.canChange(board.getTile(selected), Integer.valueOf(ae.getActionCommand()))){
		if(backup.isEmpty()){
		    options[10].setEnabled(true);
		}
		backup.push(board.getSnapshot());
		board.change(board.getTile(selected), Integer.valueOf(ae.getActionCommand()));
		options[9].setEnabled(true);
		updateBackgroundColors();
	    }
	    else{
		updateBackgroundColors();
		ArrayList<SudokuTile> conf = board.getConflicts(board.getTile(selected), Integer.valueOf(ae.getActionCommand()));
		conf.stream().filter((t) -> (t.getTextField()!=selected)).forEachOrdered((t) -> {
		    t.getTextField().setBackground(Color.red);
		});
	    }
	}
    };

    private static final ActionListener erase = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(selected==null){
		return;
	    }
	    if(board.getTile(selected).canEdit() && board.getTile(selected).getValue()!=0){
		backup.push(board.getSnapshot());
		board.change(board.getTile(selected),0);
		options[9].setEnabled(false);
	    }
	}
    };

    private static final ActionListener revert = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent ae) {
	    selected = null;
	    board.restore(backup.pop());
	    if(backup.isEmpty()){
		options[10].setEnabled(false);
	    }
	    updateBackgroundColors();
	}
    };
    
}
