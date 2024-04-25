import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Base extends Ship {
    public static enum Direction {
        LEFT, RIGHT
    }

    protected Direction direction;

    private Image image;

	private boolean collided = false;

    public Base(int x, int y) {
        super(x, y);
        image = getImage("img_base.gif");
    }

    public void draw(Graphics2D g2) {
    	
        if (!this.collided) {
        	g2.drawImage(image, getX(), getY(), null);
        }
        else
        	g2.drawImage(getImage("img_basehit.gif"), getX(), getY(), null);
    }
    
    public void setCollided(boolean value) {
		collided = value;
	}

	public boolean getCollided() {
		return this.collided;
	}

    public void move(Direction direction) {
        var x = getX();
        switch (direction) {
        case LEFT:
            if (x - 15 >= 0) {
                setX(x - 5);
            }
            break;
        case RIGHT:
            if (x + 15 <= 465) {
                setX(x + 5);
            }
            break;
        }
    }

    public Missile fireTorpedo() {
        var torpedo = new Missile(getX() + 19, getY());
        torpedo.playSound();
        return torpedo;
    }

    @Override
    protected boolean intersects(Missile torpedo) {
    	Rectangle baseRect = new Rectangle(getX(), getY(), 30, 25);
		Rectangle missileRect = new Rectangle(torpedo.getX(), torpedo.getY(), 2, 10);
		if (baseRect.intersects(missileRect)) {
			this.collided  = true;
		}
		return baseRect.intersects(missileRect);
    }

}
