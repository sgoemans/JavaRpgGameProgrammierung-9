package de.neuromechanics;

import java.awt.Point;

public abstract class Creature extends AnimEntity {
	protected int health;
	public int speed;
	protected int xMove, yMove;
	private Level level;
	
	public Creature(Game game, String name, Level level, SpriteSheet spriteSheet, int x, int y, int width, int height, int health, int speed) {
		super(game, name, spriteSheet, x, y, width, height);
		this.level = level;
		this.health = health;
		this.speed = speed;
		xMove = 0;
		yMove = 0;
	}
	private int op = 1;
	private int slow = 0;
	private int oldX;
	private int oldY;
	public void move(){
		oldX = entityX;
		oldY = entityY;
		entityX += xMove * speed;
		entityY += yMove * speed;
		int[][] touched = level.getTilesTouched(this);
		for(int i = 0; i < touched.length; i++) {
			if(Utils.containsBlock(touched)) {
				entityX = oldX;
				entityY = oldY;
			}
		}
		if(xMove == 0 && yMove == 0) {
			slow = 7;
			xPos = 1;
			pause = true;
			return;
		}
		pause = false;
		setCurrentImage(xMove, yMove);
	}
	int prevDirection;
	private void setCurrentImage(int xMove, int yMove) {
		if(yMove == -1) {
			prevDirection = 3;
		} else if(yMove == 1) {
			prevDirection = 0;
		} else if(xMove == -1) {
			prevDirection = 1;
		} else if(xMove == 1) {
			prevDirection = 2;
		}
		yPos = prevDirection;
	}
	public void setMove(Point p) {
		xMove = p.x;
		yMove = p.y;
	}
}