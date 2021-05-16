import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel{
	private Bola bola;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage imgAtual;
	
	public Game() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}			
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: k_cima=false; break;
				case KeyEvent.VK_DOWN: k_baixo=false; break;
				case KeyEvent.VK_LEFT: k_esquerda=false; break;
				case KeyEvent.VK_RIGHT: k_direita=false; break;
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: k_cima=true; break;
				case KeyEvent.VK_DOWN: k_baixo=true; break;
				case KeyEvent.VK_LEFT: k_esquerda=true; break;
				case KeyEvent.VK_RIGHT: k_direita=true; break;
				}
			}
		});
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
	public void handlerEvents() {
		bola.velX = 0;
		bola.velY = 0;
		imgAtual = bola.parada;
		if(k_cima==true) {
			bola.velY = -3;
			imgAtual = bola.cima;
			if(k_direita ==true) {
				bola.velX = 3;
				imgAtual = bola.direita_cima;
			}
			if(k_esquerda==true) {
				bola.velX = -3;
				imgAtual = bola.esquerda_cima;
			}
		} else if(k_baixo==true) {
			bola.velY = 3;
			imgAtual = bola.baixo;
			if(k_direita ==true) {
				bola.velX = 3;
				imgAtual = bola.direita_baixo;
			}
			if(k_esquerda==true) {
				bola.velX = -3;
				imgAtual = bola.esquerda_baixo;
			}
		} else if(k_esquerda==true) {
			bola.velX = -3;
			imgAtual = bola.esquerda;
		} else if(k_direita==true) {
			bola.velX = 3;
			imgAtual = bola.direita;
		}
	}
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
			bola.posX = bola.posX - bola.velX; // desfaz o movimento
		}
		if(bola.posY + (bola.raio*2) >= Principal.ALTURA_TELA || bola.posY <= 0) {
			bola.posY = bola.posY - bola.velY; // desfaz o movimento
		}
	}
		
	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.RED);
		g.drawImage(imgAtual, bola.posX, bola.posY, null);
	}
}