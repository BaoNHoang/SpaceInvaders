import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	private Timer timer;
	private Timer enemyTimer;
	private Timer mysteryTimer;
	private boolean gamePaused = false;
	private Missile missile;
	private Missile[] enemyMissiles = new Missile[3];
	private Base base;
	private boolean left;
	private boolean right;
	private int score = 0;
	private Invader[][] enemyList;
	private int enemySpeed = 500;
	private Mystery mysteryShip; // Add mystery ship variable
	private double mysteryShipCreationProbability = 0.003;
	private JLabel titleLabel;
	private Timer enemyMissileTimer;

	public Panel() {
		setBackground(Color.BLACK);

		base = new Base(220, 375);

		enemyList = new Invader[5][10];
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 10; col++) {
				if (row == 0) {
					enemyList[row][col] = new InvaderTop(75 + col * 35, 80 + row * 25);
				} else if (row < 3) {
					enemyList[row][col] = new InvaderMiddle(75 + col * 35, 80 + row * 25);
				} else {
					enemyList[row][col] = new InvaderBottom(75 + col * 35, 80 + row * 25);
				}
			}
		}

		enemyTimer = new Timer(enemySpeed, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (getRightMostX() >= 500) {
					for (int row = 0; row < 5; row++) {
						for (int col = 0; col < 10; col++) {
							if (enemyList[row][col] != null) {
								enemyList[row][col].setY(enemyList[row][col].getY() + 12);
								enemyList[row][col].setDirect(false);
							}
						}
					}

					enemySpeed *= 0.8;
				}

				if (getLeftMostX() <= 0) {
					for (int row = 0; row < 5; row++) {
						for (int col = 0; col < 10; col++) {
							if (enemyList[row][col] != null) {
								enemyList[row][col].setY(enemyList[row][col].getY() + 12);
								enemyList[row][col].setDirect(true);
							}
						}
					}

					enemySpeed *= 0.8;

				}

				for (int row = 0; row < 5; row++) {
					for (int col = 0; col < 10; col++) {
						if (enemyList[row][col] != null) {
							enemyList[row][col].move();

						}
					}
				}

				enemyTimer.setDelay(enemySpeed);

				repaint();
			}
		});
		enemyTimer.start();

		enemyMissileTimer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!gamePaused) {

					int col = (int) (Math.random() * 10);
					for (int row = 0; row < 5; row++) {

						if (enemyList[row][col] != null) {
							if (row == 5 - 1 || enemyList[row + 1][col] == null) {
								if (Math.random() > 0.2) {
									fireEnemyMissile(row, col);
								}
							}
						}

					}

					for (int i = 0; i < 3; i++) {
						if (enemyMissiles[i] != null) {
							enemyMissiles[i].enemyMove();
							if (enemyMissiles[i].getY() > 450) {
								enemyMissiles[i] = null;
							}
						}
					}

					repaint();
				}
			}
		});

		enemyMissileTimer.start();

		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!gamePaused) {
					if (missile != null) {
						missile.move();
						if (missile.getY() < -50) {
							missile = null;
						}
					}
					if (left)
						base.move(Base.Direction.LEFT);
					if (right)
						base.move(Base.Direction.RIGHT);

					checkHit();
					checkCollisions();
					checkBaseHit();
					checkMysteryHit();

					repaint();
				}
			}
		});
		timer.start();

		mysteryTimer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!gamePaused) {

					if (mysteryShip == null && (Math.random() > (1 - mysteryShipCreationProbability))) {
						createMysteryShip();
						mysteryShip.playSound();
					}

					// Move mystery ship if it exists
					if (mysteryShip != null) {
						mysteryShip.move();
						if (mysteryShip.getX() < -50 || mysteryShip.getX() > 550) {
							mysteryShip = null;
						}
					}

					repaint();
				}
			}
		});

		mysteryTimer.start();

		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (!gamePaused) {
					int key = e.getKeyCode();
					switch (key) {
					case KeyEvent.VK_LEFT:
						left = true;
						break;
					case KeyEvent.VK_RIGHT:
						right = true;
						break;
					case KeyEvent.VK_SPACE:
						if (missile == null) {
							missile = base.fireTorpedo();
						}
						break;
					}
					repaint();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!gamePaused) {
					int key = e.getKeyCode();
					switch (key) {
					case KeyEvent.VK_LEFT:
						left = false;
						break;
					case KeyEvent.VK_RIGHT:
						right = false;
						break;
					}
				}
			}
		});
	}

	private void checkHit() {
		if (missile != null) {
			if (missile.getY() > 0) { // Check for collisions only if missile is within the panel
				Rectangle missileRect = new Rectangle(missile.getX(), missile.getY(), 2, 10);
				for (int row = 0; row < 5; row++) {
					for (int col = 0; col < 10; col++) {
						if (enemyList[row][col] != null) {
							Rectangle enemyRect = new Rectangle(enemyList[row][col].getX(), enemyList[row][col].getY(),
									30, 30);
							if (missileRect.intersects(enemyRect)) {
								// Collision detected, remove enemy and missile
								score += enemyList[row][col].getPoints();
								enemyList[row][col].setCollided(true);
								enemyList[row][col].playHitSound();
								missile.setY(-51); // Reset missile position
								repaint();
							}
						}
					}
				}
			}
		}
	}

	private void checkBaseHit() {
		for (int i = 0; i < 3; i++) {
			if (enemyMissiles[i] != null) {
				if (enemyMissiles[i].getY() < 450) {
					Rectangle missileRect = new Rectangle(enemyMissiles[i].getX(), enemyMissiles[i].getY(), 2, 10);
					Rectangle baseRect = new Rectangle(base.getX(), base.getY(), 30, 30);
					if (missileRect.intersects(baseRect)) {
						base.setCollided(true);
						base.playHitSound();
						repaint();
						gameOver();
					}
				}

			}

		}
	}

	private void checkCollisions() {
		Rectangle baseRect = new Rectangle(base.getX(), base.getY(), 30, 25);
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 10; col++) {
				if (enemyList[row][col] != null) {
					Rectangle enemyRect = new Rectangle(enemyList[row][col].getX(), enemyList[row][col].getY(), 30, 25);
					if (baseRect.intersects(enemyRect)) {
						base.setCollided(true);
						base.playHitSound();
						repaint();
						gameOver();
					}
				}
			}
		}
	}

	private void checkMysteryHit() {
		if (missile != null) {
			if (missile.getY() > 0) { // Check for collisions only if missile is within the panel
				Rectangle missileRect = new Rectangle(missile.getX(), missile.getY(), 2, 10);
				if (mysteryShip != null) {
					Rectangle mystRect = new Rectangle(mysteryShip.getX(), mysteryShip.getY(), 30, 30);
					if (missileRect.intersects(mystRect)) {
						// Collision detected, remove enemy and missile
						score += mysteryShip.getPoints();
						mysteryShip.setCollided(true);
						mysteryShip.playHitSound();
						missile.setY(-51); // Reset missile position
						repaint();

					}
				}
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.GREEN);
		g2.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		g2.drawString("Score: " + score, 400, 20);

		base.draw(g2);

		if (missile != null) {
			missile.draw(g2);
		}

		for (Missile enemyMissile : enemyMissiles) {
			if (enemyMissile != null) {
				enemyMissile.draw(g2);
			}
		}

		if (mysteryShip != null) {
			mysteryShip.draw(g2);
			if (mysteryShip.getCollided()) {
				mysteryShip = null;
			}
		}

		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 10; col++) {
				if (enemyList[row][col] != null) {
					if (enemyList[row][col].getCollided()) {
						enemyList[row][col].getPainted(g2);
						enemyList[row][col] = null;
					} else {
						enemyList[row][col].getPainted(g2);
					}
				}
			}
		}

		boolean allInvadersNull = true;
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 10; col++) {
				if (enemyList[row][col] != null) {
					allInvadersNull = false;
					enemyList[row][col].getPainted(g2);
				}
			}
		}

		boolean allMissilesNull = true;
		for (int i = 0; i < enemyMissiles.length; i++) {
			if (enemyMissiles[i] != null) {
				allInvadersNull = false;
			}
		}

		if (allInvadersNull && mysteryShip == null && allMissilesNull) {
			enemySpeed = 500;
			for (int row = 0; row < 5; row++) {
				for (int col = 0; col < 10; col++) {
					if (row == 0) {
						enemyList[row][col] = new InvaderTop(75 + col * 35, 80 + row * 25);
					} else if (row < 3) {
						enemyList[row][col] = new InvaderMiddle(75 + col * 35, 80 + row * 25);
					} else {
						enemyList[row][col] = new InvaderBottom(75 + col * 35, 80 + row * 25);
					}
				}
			}
		}
	}

	public void startGame() {
		requestFocusInWindow();
	}

	public void pauseGame() {
		timer.stop();
		enemyTimer.stop();
		mysteryTimer.stop();
		enemyMissileTimer.stop();
		gamePaused = true;
	}

	public void resumeGame() {
		timer.start();
		enemyTimer.start();
		mysteryTimer.start();
		enemyMissileTimer.start();
		gamePaused = false;
	}

	public void quitGame() {
		timer.stop();
		enemyTimer.stop();
		mysteryTimer.stop();
		enemyMissileTimer.stop();
		gamePaused = true;
	}

	private void gameOver() {
		timer.stop();
		enemyTimer.stop();
		mysteryTimer.stop();
		enemyMissileTimer.stop();

		titleLabel = new JLabel("Game Over", JLabel.CENTER);
		titleLabel.setForeground(Color.GREEN);
		titleLabel.setBackground(Color.BLACK);
		titleLabel.setOpaque(true);
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
		titleLabel.setBounds(0, 150, getWidth(), 50);
		add(titleLabel);
	}

	public int getRightMostX() {
		// Calculate the rightmost X value of the invaders
		for (int col = 9; col >= 0; col--) {
			for (int row = 0; row < 5; row++) {
				if (enemyList[row][col] != null) {
					return enemyList[row][col].getX() + 30;
				}
			}
		}
		return -1; // Default return if no valid invader found
	}

	public int getLeftMostX() {
		// Calculate the leftmost X value of the invaders
		for (int col = 0; col < 10; col++) {
			for (int row = 0; row < 5; row++) {
				if (enemyList[row][col] != null) {
					return enemyList[row][col].getX();
				}
			}
		}
		return -1; // Default return if no valid invader found
	}

	private void createMysteryShip() {
		int startX = (Math.random() < 0.5) ? -50 : getWidth() + 50;
		mysteryShip = new Mystery(startX, 50);
	}

	private void fireEnemyMissile(int row, int col) {
		for (int i = 0; i < enemyMissiles.length; i++) {
			if (enemyMissiles[i] == null) {
				enemyMissiles[i] = enemyList[row][col].fireTorpedo();
				break;
			}
		}
	}
}