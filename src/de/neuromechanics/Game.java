package de.neuromechanics;

import java.awt.Canvas;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.HashSet;

public class Game implements Runnable {
	public static final int FPS = 60;
	public static final long maxLoopTime = 1000 / FPS;
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 640;

	public Screen screen;
	Player player;
	Level level;
	KeyManager keyManager;
	private Camera gameCamera;
	BufferStrategy bs;
	Graphics g;
	AnimEntity fire;

	public static void main(String[] arg) {
		Game game = new Game();
		new Thread(game).start();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		long timestamp;
		long oldTimestamp;

		screen = new Screen("Game", SCREEN_WIDTH, SCREEN_HEIGHT);
		keyManager = new KeyManager();
		screen.getFrame().addKeyListener(keyManager);

		TileSet[] tileSet = new TileSet[3];
		// Ground layer tileset with blocking tiles
		HashSet hs = new HashSet(Arrays.asList(0, 1, 2, 12, 14, 24, 25, 26));
		tileSet[0] = new TileSet("/tiles/rpg.png", 12 /*sizeX*/, 12/*sizeY*/, 3 /*border*/, hs);
		// Second layer tileset with blocking tiles
		hs = new HashSet(Arrays.asList(160, 161));
		tileSet[1] = new TileSet("/tiles/tileb.png", 16, 16, 0, hs);
		// Transparent Z / foreground layer tileset, no blocking tiles
		tileSet[2] = new TileSet("/tiles/tileb.png", 16, 16, 0, hs);
		
		String[] paths = new String[3];
		paths[0] = "/level/level1.txt";
		paths[1] = "/level/level1a.txt";
		paths[2] = "/level/level1b.txt";
		level = new Level(this, paths, tileSet);

		SpriteSheet playerSprite = new SpriteSheet("/sprites/player.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
		player = new Player(this, level, 320, 320, playerSprite);
		SpriteSheet fireSprite = new SpriteSheet("/sprites/fire_big.png", 3, 1, 64, 128);
		fire = new AnimEntity(this, "Fire", fireSprite, 450, 380, 64, 192);
		
		gameCamera = new Camera(level.getSizeX(), level.getSizeY());
		while(true) {
			oldTimestamp = System.currentTimeMillis();
			update();
			timestamp = System.currentTimeMillis();
			if(timestamp-oldTimestamp > maxLoopTime) {
				System.out.println("Wir sind zu spät!");
				continue;
			}
			render();
			timestamp = System.currentTimeMillis();
			System.out.println(maxLoopTime + " : " + (timestamp-oldTimestamp));
			if(timestamp-oldTimestamp <= maxLoopTime) {
				try {
					Thread.sleep(maxLoopTime - (timestamp-oldTimestamp) );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	void update() {
		keyManager.update();
		player.setMove(getInput());
		player.update();
		fire.update();
	}
	void render() {
		  Canvas c = screen.getCanvas();
		  bs = c.getBufferStrategy();
		  if(bs == null){
		    screen.getCanvas().createBufferStrategy(3);
		    return;
		  }
		  g = bs.getDrawGraphics();
		  //Clear Screen
		  g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		  level.render(g);
		  player.render(g);
		  fire.render(g);
		  level.renderZ(g);
		  bs.show();
		  g.dispose();
		}
	
	private Point getInput(){
		int xMove = 0;
		int yMove = 0;
		if(keyManager.up)
			yMove = -1;
		if(keyManager.down)
			yMove = 1;
		if(keyManager.left)
			xMove = -1;
		if(keyManager.right)
			xMove = 1;
		return new Point(xMove, yMove);
	}
	public Camera getGameCamera(){
		return gameCamera;
	}
}