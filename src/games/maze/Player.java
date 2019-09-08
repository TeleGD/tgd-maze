package games.maze;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import app.AppInput;
import app.AppPlayer;

public abstract class Player {

	protected World world;
	protected Board board;
	protected Image spriteUp;
	protected static Image spriteDown;
	protected Image spriteLeft;
	protected Image spriteRight;
	protected int posX;
	protected int posY;
	protected float x;
	protected float y;
	protected float newX;
	protected float newY;
	protected float width;
	protected float height;
	protected int size;
	protected int rows;
	protected int columns;
	protected float speed;
	protected int direction; // 1 right 2 up 3 left 4 down
	protected boolean moveLeft = false;
	protected boolean moveRight = false;
	protected boolean moveDown = false;
	protected boolean moveUp = false;
	protected boolean canMoveHorizontal;
	protected boolean canMoveVertical;
	protected int controllerID;
	protected int id;
	static {
		try {
			spriteDown = new Image(World.DIRECTORY_IMAGES + "avatar.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Player(World w, AppPlayer appPlayer, int id) {
		controllerID = appPlayer.getControllerID();
		this.id = id;
		world = w;
		this.board = w.board;
		size = board.getSize();
		rows = board.getRows();
		columns = board.getColumns();
		direction = 1;
		speed = 0.3f * ((float) size / board.getImgInitSize()[0]);
		width = spriteDown.getWidth() * ((float) size / board.getImgInitSize()[0]);
		height = spriteDown.getHeight() * ((float) size / board.getImgInitSize()[1]);
		// Attribution des positions de départ en fonction du n° de joueur
		switch (id) {
		case 0:
			posX = (int)(columns / 2) + 1;
			posY = (int)(rows / 2) + 1;
			break;
		case 1:
			posX = 1;
			posY = 1;
			break;
		case 2:
			posX = 1;
			posY = columns - 2;
			break;
		case 3:
			posX = rows - 2;
			posY = 1;
			break;
		}
		updateXY(); // initialisation de x et y selon les posX et posY de départ
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		this.move(container, delta);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		context.drawImage(spriteDown, x, y, x + width, y + height, 0, 0, spriteDown.getWidth() - 1, spriteDown.getHeight() - 1);
	}

	private void updateXY() {
		x = posX * size;
		y = posY * size;
	}

	private void updatePosXY() {
		posX = (int)(x / size);
		posY = (int)(y / size);
	}

	public void move(GameContainer container, int dlt) {
		AppInput input = (AppInput) container.getInput();
		moveLeft = input.isControllerLeft(controllerID);
		moveRight = input.isControllerRight(controllerID);
		moveDown = input.isControllerDown(controllerID);
		moveUp = input.isControllerUp(controllerID);
		float speedX = 0;
		float speedY = 0;
		if (moveUp) {
			speedY = -speed;
		}
		if (moveDown) {
			speedY = speed;
		}
		if (moveLeft) {
			speedX = -speed;
		}
		if (moveRight) {
			speedX = speed;
		}
		if (speedX != 0 && speedY != 0) {
			speedX /= Math.sqrt(2);
			speedY /= Math.sqrt(2);
		}
		newX = x + dlt * speedX;
		newY = y + dlt * speedY;
		doCollisions();
		if (canMoveHorizontal) x = newX;
		if (canMoveVertical) y = newY;
		updatePosXY();
	}

	private void doCollisions() {
		// Wall collisions
		canMoveHorizontal = canMoveVertical = true;
		int xCase = (int)(newX / size);
		int yCase = (int)(newY / size);
		int countSolidCases = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (!board.getCase(yCase + j, xCase + i).isPassable()) {
					countSolidCases++;
				}
			}
		}
		if (countSolidCases > 1) {
			if ((!board.getCase(yCase, xCase).isPassable() && !board.getCase(yCase, xCase + 1).isPassable() && (willBeInsideCase(xCase, yCase) || willBeInsideCase(xCase + 1, yCase))) || (!board.getCase(yCase + 1, xCase).isPassable() && !board.getCase(yCase + 1, xCase + 1).isPassable() && (willBeInsideCase(xCase, yCase + 1) || willBeInsideCase(xCase + 1, yCase + 1)))) {
				canMoveVertical = false;
			}
			if ((!board.getCase(yCase, xCase).isPassable() && !board.getCase(yCase + 1, xCase).isPassable() && (willBeInsideCase(xCase, yCase) || willBeInsideCase(xCase, yCase + 1))) || (!board.getCase(yCase, xCase + 1).isPassable() && !board.getCase(yCase + 1, xCase + 1).isPassable() && (willBeInsideCase(xCase + 1, yCase) || willBeInsideCase(xCase + 1, yCase + 1)))) {
				canMoveHorizontal = false;
			}
			if (!board.getCase(yCase, xCase + 1).isPassable() && !board.getCase(yCase + 1, xCase).isPassable()) {
				if (willBeInsideCase(xCase + 1, yCase)) canMoveHorizontal = false;
				if (willBeInsideCase(xCase, yCase + 1)) canMoveVertical = false;
			}
			if (!board.getCase(yCase, xCase).isPassable() && !board.getCase(yCase + 1, xCase + 1).isPassable()) {
				if (willBeInsideCase(xCase, yCase)) {
					if (newX - xCase * size < newY - yCase * size) {
						canMoveVertical = false;
					} else {
						canMoveHorizontal = false;
					}
				}
				if (willBeInsideCase(xCase + 1, yCase + 1)) {
					if (newX + width - (xCase + 1) * size < (newY + height) - (yCase + 1) * size) {
						canMoveHorizontal = false;
					} else {
						canMoveVertical = false;
					}
				}
			}
		} else if (countSolidCases == 1) {
			if (!board.getCase(yCase, xCase).isPassable() && willBeInsideCase(xCase, yCase)) {
				if (newX - xCase * size < newY - yCase * size) {
					y = (yCase + 1) * size;
					canMoveVertical = false;
				} else {
					x = (xCase + 1) * size;
					canMoveHorizontal = false;
				}
			} else if (!board.getCase(yCase, xCase + 1).isPassable() && willBeInsideCase(xCase + 1, yCase)) {
				if (newX + width - (xCase + 1) * size < (yCase + 1) * size - newY) {
					x = (xCase + 1) * size - width;
					canMoveHorizontal = false;
				} else {
					y = (yCase + 1) * size;
					canMoveVertical = false;
				}
			} else if (!board.getCase(yCase + 1, xCase).isPassable() && willBeInsideCase(xCase, yCase + 1)) {
				if ((xCase + 1) * size - newX < (newY + height) - (yCase + 1) * size) {
					x = (xCase + 1) * size;
					canMoveHorizontal = false;
				} else {
					y = (yCase + 1) * size - height;
					canMoveVertical = false;
				}
			} else if (!board.getCase(yCase + 1, xCase + 1).isPassable() && willBeInsideCase(xCase + 1, yCase + 1)) {
				if (newX + width - (xCase + 1) * size < (newY + height) - (yCase + 1) * size) {
					x = (xCase + 1) * size - width;
					canMoveHorizontal = false;
				} else {
					y = (yCase + 1) * size - height;
					canMoveVertical = false;
				}
			}
		}
		// Player collisions
		for (Player p: world.players) {
			if (!p.equals(this)) {
				if (willBeInside(p.getX(), p.getY(), p.getX() + p.getWidth(), p.getY() + p.getHeight())) {
					p.collideWithPlayer(this);
				}
			}
		}
	}

	public abstract void collideWithPlayer(Player p);

	public boolean willBeInsideCase(int x0, int y0) {
		return willBeInside(x0 * size, y0 * size, (x0 + 1) * size, (y0 + 1) * size);
	}

	public boolean willBeInside(float x0, float y0, float x1, float y1) {
		if (newX + width <= x0) return false;
		if (newX >= x1) return false;
		if (newY + height <= y0) return false;
		if (newY >= y1) return false;
		return true;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getID() {
		return id;
	}

}
