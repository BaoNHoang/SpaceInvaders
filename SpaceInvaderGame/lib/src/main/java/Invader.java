import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public abstract class Invader extends Ship {

	private boolean orig = true;
	private boolean moveRight = true;

	public Invader(int x, int y) {
		super(x, y);
	}
	
	public void move() {
		
	}
	
	public int getPoints() {
		return 0;
		
	}

	public void getPainted(Graphics g) {
		Image image;
		image = getImage("img_invaderhit.gif");
		g.drawImage(image, getX(), getY(), null);
	}
	
	public void setOrig(boolean value) {
		orig = value;
	}
	
	public void setCollided(boolean value) {
		
	}
	
	public boolean getCollided() {
		return false;
	}
	
	public boolean getDirect() {
		return moveRight;
	}
	
	public void setDirect(boolean value) {
		moveRight = value;
	}
	
	public boolean getOrig() {
		return orig;
	}
	
	public Missile fireTorpedo() {
        var torpedo = new Missile(getX() + 15, getY());
        return torpedo;
    }
	
	protected Image getImage(String filename) {
		URL url = getClass().getResource(filename);
		ImageIcon icon = new ImageIcon(url);
		return icon.getImage();
	}

}
