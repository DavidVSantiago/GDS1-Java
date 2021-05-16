import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

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
		bola.posY = bola.posY + bola.velY;
		testeColisoes();
	}
	public void render() {
		repaint();
	}
	
	// OUTROS METODOS -------------------------
	public void testeColisoes() {
		if(bola.posX + (bola.raio*2) >= Principal.LARGURA_TELA || bola.posX <= 0){
			bola.velX = bola.velX*-1;
		}
		if(bola.posY + (bola.raio*2) >= Principal.ALTURA_TELA || bola.posY <= 0) {
			bola.velY = bola.velY*-1;
		}
	}
		
	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.RED);
		g.drawImage(bola.obterImagem(), bola.posX, bola.posY, null);
	}
}