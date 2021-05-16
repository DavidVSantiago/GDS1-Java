import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Game extends JPanel{
	private Bola bola;
	private Inimigo inimigo;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage imgAtual;
	private long tempoAtual;
	private long tempoAnterior;
	private double deltaTime;
	private double FPS_limit = 60;
	
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
		inimigo = new Inimigo();
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
		tempoAnterior = System.nanoTime();
		double tempoMinimo = (1e9)/FPS_limit; // duração mínima do quadro (em nanosegundos)
		while(true) { // repetição intermitente do gameloop
			tempoAtual = System.nanoTime();
			deltaTime = (tempoAtual - tempoAnterior) * (6e-8);
			handlerEvents();
			update(deltaTime);
			render();
			tempoAnterior = tempoAtual; // no próximo quadro, o tempo final será o inicial desse quadro
			try {
				int tempoEspera = (int) ((tempoMinimo - deltaTime)*(1e-6));
				Thread.sleep( tempoEspera );
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	public void update(double deltaTime) {
		bola.posX = bola.posX + (bola.velX*deltaTime);
		bola.posY = bola.posY + (bola.velY*deltaTime);
		bola.centroX = bola.posX + bola.raio;
		bola.centroY = bola.posY + bola.raio;
		testeColisoes(deltaTime);
	}
	public void render() {
		repaint();
	}
	
	// OUTROS METODOS -------------------------
	public void testeColisoes(double deltaTime) {
		// colisão comum ---------------------------------
		if(bola.posX + (bola.raio*2) >= Principal.LARGURA_TELA || bola.posX <= 0){
			bola.posX = bola.posX - (bola.velX*deltaTime); // desfaz o movimento
		}
		if(bola.posY + (bola.raio*2) >= Principal.ALTURA_TELA || bola.posY <= 0) {
			bola.posY = bola.posY - (bola.velY*deltaTime); // desfaz o movimento
		}
		// colisão circular ------------------------------
		double catetoH = bola.centroX - inimigo.centroX;
		double catetoV = bola.centroY - inimigo.centroY;
		double hipotenusa = Math.sqrt(Math.pow(catetoH, 2)+Math.pow(catetoV, 2));
		if(hipotenusa <= bola.raio + inimigo.raio) { // verifica se houve colisão circular
			bola.posX = bola.posX - (bola.velX*deltaTime); // desfaz o movimento horizontal
			bola.posY = bola.posY - (bola.velY*deltaTime); // desfaz o movimento vertical
		}
	}
		
	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // converte o objeto Graphics para Graphics2D
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintComponent(g2d);
		AffineTransform af1 = new AffineTransform();
		AffineTransform af2 = new AffineTransform();
		af1.translate(bola.posX, bola.posY);
		af2.translate(inimigo.posX, inimigo.posY);
		setBackground(Color.LIGHT_GRAY);
		g2d.setColor(Color.RED);
		g2d.drawImage(imgAtual, af1, null);
		g2d.drawImage(inimigo.img, af2, null);
	}
}