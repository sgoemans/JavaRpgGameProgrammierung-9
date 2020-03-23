package de.neuromechanics;
import java.awt.Graphics;

public class AnimEntity extends Entity{
  protected SpriteSheet spriteSheet;
  protected Game game;
  protected boolean pause;
  public AnimEntity(Game game, String name, SpriteSheet sheet, int x, int y, int width, int height) {
    super(name, x, y, width, height);
    this.spriteSheet = sheet;
    this.game = game;
  }
  private int slow = 0;
  protected int xPos = 0;
  protected int yPos = 0;
  private int op = 1;
  
  @Override
  protected void update() {
    if(spriteSheet != null) {
      if(!pause) {
        if(slow++ >= 7) {
          slow = 0;
          if(op == -1 && xPos <= 0) {
            op = 1;
          } else if(op == 1 && xPos >= 2) {
            op = -1;
          }
          xPos = (xPos + op);
        }
      }
      image = spriteSheet.getSpriteElement(xPos, yPos);
    }
  }
  @Override
  protected void render(Graphics g) {
    g.drawImage(image, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), null);
  }
}