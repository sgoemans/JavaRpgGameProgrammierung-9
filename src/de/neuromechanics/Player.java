package de.neuromechanics;

public class Player extends Creature {
	public static final int MARGIN_HORIZ = 14;
	public static final int MARGIN_VERT = 2;
	public static final int DEFAULT_HEALTH = 100;
	public static final int DEFAULT_SPEED = 2;
	private Game game;

	public Player(Game game, Level level, int x, int y, SpriteSheet playerSprite) {
		super(game, "Player", level, playerSprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED);
		this.game = game;
	}

	@Override
	public void update() {
		move();
		super.update();
		game.getGameCamera().centerOnEntity(this);
	}
}