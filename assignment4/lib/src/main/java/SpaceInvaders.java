import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SpaceInvaders extends JFrame {
	private Panel gamePanel;
	private boolean gameStarted = false;
	private JLabel titleLabel;
	private JMenuItem pause;
	private JMenuItem resume;

	public SpaceInvaders() {
		setTitle("Space Invaders");
		setSize(500, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		getContentPane().setBackground(Color.BLACK);

		titleLabel = new JLabel("SPACE INVADERS", JLabel.CENTER);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
		titleLabel.setBounds(0, 150, getWidth(), 50);
		add(titleLabel);

		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);

		JMenu game = new JMenu("Game");
		JMenu help = new JMenu("Help");

		menu.add(game);
		menu.add(help);

		JMenuItem about = help.add("About...");

		JMenuItem newGame = game.add("New Game");
		game.addSeparator();
		pause = game.add("Pause");
		resume = game.add("Resume");
		game.addSeparator();
		JMenuItem quit = game.add("Quit");

		pause.setEnabled(false);
		resume.setEnabled(false);

		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SpaceInvaders.this, "Start a new game?");
				if (result == JOptionPane.YES_OPTION) {
					pause.setEnabled(true);
					resume.setEnabled(false);
					startNewGame();
				}
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SpaceInvaders.this, "Dare to Quit?");
				if (result == JOptionPane.YES_OPTION) {
					if (gamePanel != null) {
						gamePanel.quitGame();
					}
					dispose();
				}
			}
		});

		about.addActionListener(e -> JOptionPane.showMessageDialog(SpaceInvaders.this,
				new JLabel("<html><hr><b>SpaceInvaders</b><br>by Bao Hoang and Joshua Gould<hr></html>")));
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int result = JOptionPane.showConfirmDialog(SpaceInvaders.this, "Dare to Quit?");
				if (result == JOptionPane.YES_OPTION) {
					if (gamePanel != null) {
						gamePanel.quitGame();
					}
					dispose();
				}
			}
		});

		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameStarted) {
					pause.setEnabled(false);
					resume.setEnabled(true);
					pauseGame();
				}
			}
		});

		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameStarted) {
					pause.setEnabled(true);
					resume.setEnabled(false);
					resumeGame();
				}
			}
		});

	}

	private void startNewGame() {
		if (gamePanel != null) {
			remove(gamePanel);
		}
		gamePanel = new Panel();
		add(gamePanel);
		gamePanel.requestFocusInWindow();
		gameStarted = true;
		validate();
		gamePanel.startGame();
	}

	private void pauseGame() {
		if (gamePanel != null) {
			gamePanel.pauseGame();
		}
	}

	private void resumeGame() {
		if (gamePanel != null) {
			gamePanel.resumeGame();
		}
	}

	public static void main(String[] args) {
		SpaceInvaders f = new SpaceInvaders();
		f.setVisible(true);
	}
}