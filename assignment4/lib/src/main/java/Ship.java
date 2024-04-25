import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.net.URL;

public abstract class Ship extends Drawable{
    protected boolean hit;
    protected Clip hitSound;
    protected Image hitImage;

    public Ship(int x, int y) {
    	super(x, y);
        hit = false;
        hitSound = getSound("aud_hit.wav");
        hitImage = null; 
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isHitBytorpedo(Missile torpedo) {
        return !hit && intersects(torpedo);
    }

    public void hit() {
        hit = true;
        playHitSound();
    }

    protected abstract boolean intersects(Missile torpedo);

    protected void playHitSound() {
        if (hitSound != null) {
            hitSound.setFramePosition(0);
            hitSound.start();
        }
    }

    protected Clip getSound(String filename) {
        Clip clip = null;
        try {
            URL url = getClass().getResource(filename);
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }
    
    protected Image getImage(String filename) {
		URL url = getClass().getResource(filename);
		ImageIcon icon = new ImageIcon(url);
		return icon.getImage();
	}
}