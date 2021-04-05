package sudoku.view.backgroundcolor;

import sudoku.view.ViewTile;

import java.awt.*;

public class NormalBackgroundState implements BackgroundColorState {

	private final ViewTile tile;

	public NormalBackgroundState(ViewTile tile) {
		this.tile = tile;
	}

	@Override
	public Color getBackgroundColor() {
		return tile.getDefaultBackgroundColor();
	}
}
