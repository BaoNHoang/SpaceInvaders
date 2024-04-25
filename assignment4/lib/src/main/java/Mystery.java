import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.sound.sampled.Clip;

public class Mystery extends Ship {
	private int[] pointOptions = { 50, 100, 150, 300 };
	private boolean moveLeft;
	private Clip sound;
	private boolean collided = false;

	public Mystery(int x, int y) {
		super(x, y);
		sound = getSound("aud_mystery.wav");
		if (getX() == -50) {
			moveLeft = true;
		} else
			moveLeft = false;
	}

	public void move() {
		if (moveLeft) {
			setX(getX() + 5);
		} else {
			setX(getX() - 5);
		}
	}

	@Override
	protected boolean intersects(Missile torpedo) {
		Rectangle mysteryRect = new Rectangle(getX(), getY(), 35, 20);
		Rectangle missileRect = new Rectangle(torpedo.getX(), torpedo.getY(), 2, 10);
		return mysteryRect.intersects(missileRect);
	}

	public void draw(Graphics g) {
		Image image;
		if (this.collided) {
			image = getImage("img_invaderhit.gif");
		} else
			image = getImage("img_mystery.gif");
		g.drawImage(image, getX(), getY(), null);
	}

	public int getPoints() {
		return pointOptions[(int) Math.floor(Math.random() * 4)];

	}

	public void playSound() {
		sound.setFramePosition(0);
		sound.start();
	}

	public void setCollided(boolean b) {
		this.collided = b;

	}

	public boolean getCollided() {
		return this.collided;
	}
}
