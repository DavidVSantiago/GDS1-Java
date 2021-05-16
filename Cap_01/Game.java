import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Game extends JPanel{
	
	public Game() {
		setFocusable(true);
		setLayout(null);
	}
	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.RED);
		g.fillOval(100, 100, 50, 50);
	}
}