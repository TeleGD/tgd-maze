package pages;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppFont;
import app.AppGame;
import app.AppInput;
import app.AppLoader;
import app.AppPage;
import app.AppPlayer;

public class Players extends AppPage {

	static private Font playersFont;

	// static private int playersLineHeight;

	static {
		Players.playersFont = AppLoader.loadFont ("/fonts/vt323.ttf", AppFont.BOLD, 24);

		// Players.playersLineHeight = 30;
	}

	private boolean backFlag;
	private boolean forwardFlag;
	private int leftFlags;
	private int rightFlags;
	private int previousID;
	private int nextID;

	private boolean playersVisibility;

	protected int playersBoxWidth;
	protected int playersBoxHeight;
	protected int playersBoxX;
	protected int playersBoxY;

	private int playersColumnWidth;

	public Players (int ID) {
		super (ID);
	}

	@Override
	public void init (GameContainer container, StateBasedGame game) {
		super.initSize (container, game, 600, 400);
		super.init (container, game);

		this.backFlag = false;
		this.forwardFlag = false;
		this.leftFlags = 0;
		this.rightFlags = 0;
		this.previousID = -1;
		this.nextID = -1;

		this.playersBoxX = this.contentX;
		this.playersBoxY = this.subtitleBoxY + this.subtitleBoxHeight + AppPage.gap;
		this.playersBoxWidth = this.contentWidth;
		this.playersBoxHeight = this.hintBoxY - this.playersBoxY - AppPage.gap;

		this.playersColumnWidth = (this.playersBoxWidth + AppPage.gap) / 4 - AppPage.gap;

		this.playersVisibility = true;

		this.hintVisibility = false;
		this.hintBlink = true;

		this.setTitle ("Menu des joueurs");
		this.setSubtitle ("Sans sous-titre");
		this.setHint ("PRESS [A]");
	}

	@Override
	public void poll (GameContainer container, StateBasedGame game, Input user) {
		super.poll (container, game, user);
		AppInput appInput = (AppInput) container.getInput ();
		this.backFlag = false;
		this.forwardFlag = false;
		this.leftFlags = 0;
		this.rightFlags = 0;
		AppGame appGame = (AppGame) game;
		loop: for (int i = 0, l = appInput.getControllerCount (); i < l; i++) {
			boolean BUTTON_A = appInput.isButtonPressed (AppInput.BUTTON_A, i);
			for (int j = appGame.appPlayers.size () - 1; j > 0; j--) {
				AppPlayer appPlayer = appGame.appPlayers.get (j);
				if (appPlayer.getControllerID () == i) {
					int record = appPlayer.getButtonPressedRecord ();
					if (BUTTON_A == ((record & AppInput.BUTTON_A) == 0)) {
						appPlayer.setButtonPressedRecord (record ^ AppInput.BUTTON_A);
					}
					continue loop;
				}
			}
			if (BUTTON_A && i != appGame.appPlayers.get (0).getControllerID () && appGame.appPlayers.size () < 4) {
				int colorID = appGame.availableColorIDs.remove (0);
				String name = "Joueur " + AppPlayer.COLOR_NAMES [colorID]; // TODO: set user name
				appGame.appPlayers.add (new AppPlayer (colorID, i, name, AppInput.BUTTON_A));
				if (appGame.appPlayers.size () >= 2 && !this.hintVisibility) {
					this.hintVisibility = true;
				}
			}
		}
		for (int i = appGame.appPlayers.size () - 1; i > 0; i--) {
			AppPlayer appPlayer = appGame.appPlayers.get (i);
			boolean BUTTON_B = appInput.isButtonPressed (AppInput.BUTTON_B, appPlayer.getControllerID ());
			int record = appPlayer.getButtonPressedRecord ();
			if (BUTTON_B == ((record & AppInput.BUTTON_B) == 0)) {
				appPlayer.setButtonPressedRecord (record ^ AppInput.BUTTON_B);
				if (BUTTON_B) {
					appGame.availableColorIDs.add (0, appGame.appPlayers.remove (i).getColorID ());
					if (appGame.appPlayers.size () < 2 && this.hintVisibility) {
						this.hintVisibility = false;
					}
				}
			}
		}
		for (int i = 0, j = 1, l = appGame.appPlayers.size (); i < l; i++, j <<= 1) {
			AppPlayer appPlayer = appGame.appPlayers.get (i);
			int ID = appPlayer.getControllerID ();
			boolean BUTTON_L = appInput.isButtonPressed (AppInput.BUTTON_L, ID);
			boolean BUTTON_R = appInput.isButtonPressed (AppInput.BUTTON_R, ID);
			int record = appPlayer.getButtonPressedRecord ();
			if (BUTTON_L == ((record & AppInput.BUTTON_L) == 0)) {
				record ^= AppInput.BUTTON_L;
				if (BUTTON_L) {
					this.leftFlags |= j;
				}
			}
			if (BUTTON_R == ((record & AppInput.BUTTON_R) == 0)) {
				record ^= AppInput.BUTTON_R;
				if (BUTTON_R) {
					this.rightFlags |= j;
				}
			}
			appPlayer.setButtonPressedRecord (record);
		}
		{
			AppPlayer gameMaster = appGame.appPlayers.get (0);
			int gameMasterID = gameMaster.getControllerID ();
			boolean BUTTON_A = appInput.isKeyDown (AppInput.KEY_ENTER) || appInput.isButtonPressed (AppInput.BUTTON_A, gameMasterID);
			boolean BUTTON_B = appInput.isKeyDown (AppInput.KEY_ESCAPE) || appInput.isButtonPressed (AppInput.BUTTON_B, gameMasterID);
			int gameMasterRecord = gameMaster.getButtonPressedRecord ();
			if (BUTTON_A == ((gameMasterRecord & AppInput.BUTTON_A) == 0)) {
				gameMasterRecord ^= AppInput.BUTTON_A;
				this.forwardFlag = BUTTON_A;
			}
			if (BUTTON_B == ((gameMasterRecord & AppInput.BUTTON_B) == 0)) {
				gameMasterRecord ^= AppInput.BUTTON_B;
				this.backFlag = BUTTON_B;
			}
			gameMaster.setButtonPressedRecord (gameMasterRecord);
		}
	}

	@Override
	public void update (GameContainer container, StateBasedGame game, int delta) {
		super.update (container, game, delta);
		AppGame appGame = (AppGame) game;
		if (this.backFlag) {
			if (this.previousID != -1) {
				appGame.enterState (this.previousID);
			}
		}
		if (this.forwardFlag) {
			if (this.nextID != -1) {
				appGame.enterState (this.nextID, new FadeOutTransition (), new FadeInTransition ());
			}
		}
		for (int i = 0, j = this.leftFlags; j != 0; i++, j >>>= 1) {
			if ((j & 1) != 0) {
				AppPlayer appPlayer = appGame.appPlayers.get (i);
				appGame.availableColorIDs.add (0, appPlayer.getColorID ());
				int colorID = appGame.availableColorIDs.remove (appGame.availableColorIDs.size () - 1);
				String name = "Joueur " + AppPlayer.COLOR_NAMES [colorID]; // TODO: set user name
				appPlayer.setColorID (colorID);
				appPlayer.setName (name);
			}
		}
		for (int i = 0, j = this.rightFlags; j != 0; i++, j >>>= 1) {
			if ((j & 1) != 0) {
				AppPlayer appPlayer = appGame.appPlayers.get (i);
				appGame.availableColorIDs.add (appPlayer.getColorID ());
				int colorID = appGame.availableColorIDs.remove (0);
				String name = "Joueur " + AppPlayer.COLOR_NAMES [colorID]; // TODO: set user name
				appPlayer.setColorID (colorID);
				appPlayer.setName (name);
			}
		}
	}

	@Override
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		super.render (container, game, context);
		this.renderPlayers (container, game, context);
	}

	private void renderPlayers (GameContainer container, StateBasedGame game, Graphics context) {
		AppGame appGame = (AppGame) game;
		if (this.playersVisibility) {
			int y = this.playersBoxY;
			for (int i = 0; i < 4; i++) {
				int x = this.playersBoxX + (this.playersColumnWidth + AppPage.gap) * i;
				if (i < appGame.appPlayers.size ()) {
					AppPlayer appPlayer = appGame.appPlayers.get (i);
					int color = appPlayer.getColorID ();
					String name = appPlayer.getName ();
					int playerWidth = Players.playersFont.getWidth (name);
					int playerHeight = Players.playersFont.getHeight (name);
					int playerX = x + (this.playersColumnWidth - playerWidth) / 2;
					int playerY = y + (this.playersBoxHeight - playerHeight) / 2;
					context.setFont (Players.playersFont);
					context.setColor (AppPlayer.STROKE_COLORS [color]);
					context.fillRect (x - 4, y - 4, this.playersColumnWidth, this.playersBoxHeight);
					context.setColor (AppPlayer.FILL_COLORS [color]);
					context.fillRect (x + 4, y + 4, this.playersColumnWidth, this.playersBoxHeight);
					context.setColor (AppPlayer.STROKE_COLORS [color]);
					context.drawString (name, playerX + 4, playerY + 4);
				} else {
					int playerWidth = AppPage.titleFont.getWidth ("+");
					int playerHeight = AppPage.titleFont.getHeight ("+");
					int playerX = x + (this.playersColumnWidth - playerWidth) / 2;
					int playerY = y + (this.playersBoxHeight - playerHeight) / 2;
					context.setFont (AppPage.titleFont);
					context.setColor (AppPage.foregroundColor);
					context.drawRect (x, y, this.playersColumnWidth, this.playersBoxHeight);
					context.setColor (AppPage.highlightColor);
					context.drawString ("+", playerX - 2, playerY - 2);
					context.setColor (AppPage.foregroundColor);
					context.drawString ("+", playerX + 2, playerY + 2);
				}
			}
		}
	}

	public void setPreviousID (int ID) {
		this.previousID = ID;
	}

	public int getPreviousID () {
		return this.previousID;
	}

	public void setNextID (int ID) {
		this.nextID = ID;
		this.setSubtitle (AppGame.TITLES [ID]);
	}

	public int getNextID () {
		return this.nextID;
	}

}
