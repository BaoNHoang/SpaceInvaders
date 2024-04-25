import java.awt.Color;
import java.awt.Graphics2D;

import javax.sound.sampled.Clip;

public class Missile extends Drawable {
	private Clip  sound;
	public Missile(int x, int y) {
		super( x, y );
		sound = getSound( "aud_basefire.wav" );
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fillRect(getX()-8, getY(), 2, 10);
	}
	public void move() {
		setY( getY() - 5 );
	}
	public void enemyMove() {
		setY( getY() + 5 );
	}
	public void playSound() {
		sound.setFramePosition(0);
		sound.start();
	}
}