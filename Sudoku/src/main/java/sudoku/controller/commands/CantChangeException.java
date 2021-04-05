package sudoku.controller.commands;

public class CantChangeException extends RuntimeException {
	public CantChangeException(String s) {
		System.out.println(s);
	}
}
