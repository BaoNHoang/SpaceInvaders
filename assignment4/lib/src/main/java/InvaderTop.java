import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class InvaderTop extends Invader {
	private int points = 30;
	private boolean collided = false;

	public InvaderTop(int x, int y) {
		super(x, y);
	}

	public int getPoints() {
		return points;
	}

	public void setCollided(boolean value) {
		collided = value;
	}

	public boolean getCollided() {
		return this.collided;
	}

	@Override
	public void move() {
		if (super.getDirect()) {
			setX(getX() + 5);
			super.setOrig(!super.getOrig());
		} else {
			setX(getX() - 5);
			super.setOrig(!super.getOrig());
		}
	}

	@Override
	public void getPainted(Graphics g) {
		Image image;
		if (this.collided) {
			super.getPainted(g);
		} else if (super.getOrig()) {
			image = getImage("img_invadertopA.gif");
			g.drawImage(image, getX(), getY(), null);
		} else {
			image = getImage("img_invadertopB.gif");
			g.drawImage(image, getX(), getY(), null);
		}
	}

	@Override
	protected boolean intersects(Missile torpedo) {
		Rectangle invaderRect = new Rectangle(getX(), getY(), 30, 25);
		Rectangle missileRect = new Rectangle(torpedo.getX(), torpedo.getY(), 2, 10);
		if (invaderRect.intersects(missileRect)) {
			this.collided = true;
		}
		return invaderRect.intersects(missileRect);
	}

}