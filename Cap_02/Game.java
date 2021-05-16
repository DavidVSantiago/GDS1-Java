import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Game extends JPanel{
	private Bola bola;
	
	public Game() {
		bola = new Bola();
		setFocusable(true);
		setLayout(null);
		
		new Thread(new Runnable() { // instancia da Thread + classe interna anônima
			@Override
			public void run() {
				gameloop(); // inicia o gameloop
			}
		}).start(); // dispara a Thread
	}
	// GAMELOOP -------------------------------
	public void gameloop() {
		while(true) { // repetição intermitente do gameloop
			handlerEvents();
			update();
			render();
			try {
				Thread.sleep(17);
			}catch (Exception e) {}
		}
	}
	public void handlerEvents() {}
	public void update() {
		bola.posX = bola.posX + bola.velX;
	}
	public void render() {
		repaint();
	}
		
	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.RED);
		g.fillOval(bola.posX, bola.posY, bola.raio*2, bola.raio*2);
	}
}