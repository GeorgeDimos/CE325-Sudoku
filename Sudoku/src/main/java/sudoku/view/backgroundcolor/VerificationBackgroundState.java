package sudoku.view.backgroundcolor;

import sudoku.view.ViewTile;

import java.awt.*;

public class VerificationBackgroundState implements BackgroundColorState {

	private final ViewTile tile;

	public VerificationBackgroundState(ViewTile tile) {
		this.tile = tile;
	}

	@Override
	public Color getBackgroundColor() {
		if (tile.getValue() != 0 && tile.getValue() != tile.getSolution())
			return Color.blue;
		return tile.getDefaultBackgroundColor();
	}
}
