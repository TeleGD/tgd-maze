package games.maze;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

// import games.maze.cases.FreeCase;

public class Board {

	private Case board[][];
	private int rows;
	private int columns;
	// private World w;
	private LabyGenerator lab;
	private int size;

	public Board(World w, int minSide) {
		// this.w = w;
		int height = w.getHeight();
		int width = w.getWidth();
		if (width < height) {
			size = width / minSide;
			rows = height / size - 1;
			columns = minSide;
		} else {
			size = height / minSide;
			rows = minSide;
			columns = width / size - 1;
		}
		this.lab = (new LabyGenerator(this, rows, columns));
		board = lab.getLab();
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		for (int i = 0; i < this.lab.getNbLig(); i++) {
			for (int j = 0; j < this.lab.getNbCol(); j++) {
				this.board[i][j].render(container, game, context);
			}
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {}

	// public boolean movePlayer(int posX, int posY, Player p) {
	// 	if (posX < rows && posY < columns && posX >= 0 && posY >= 0 && board[posX][posY] instanceof FreeCase) {
	// 		if (board[posX][posY].getPlayerId() ==- 1) {
	// 			// set new cell true
	// 			board[posX][posY].setPlayerId(w.players.indexOf(p));
	// 			// set old cell false
	// 			board[p.getPosX()][p.getPosY()].setPlayerId(-1);
	// 			return true;
	// 		} else if (board[posX][posY].getPlayerId() == 0) {
	// 			w.endGame(p);
	// 		}
	// 	}
	// 	return false;
	// }

	public Case getCase(int row, int column) {
		return board[row][column];
	}

	public int[] getImgInitSize() {
		return board[0][0].getImgInitSize();
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int getSize() {
		return this.size;
	}

}
