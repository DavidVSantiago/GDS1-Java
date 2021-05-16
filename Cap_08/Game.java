import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Game extends JPanel{
	private Jogador jogador;
	private Inimigo inimigo;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage bg;
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
		jogador = new Jogador();
		inimigo = new Inimigo();
		try {
			bg = ImageIO.read(getClass().getResource("imgs/bg.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		jogador.handlerEvents(k_cima, k_baixo, k_esquerda, k_direita);
	}
	
	public void update(double deltaTime) {
		jogador.update(deltaTime);
		inimigo.update(deltaTime);
		testeColisoes(deltaTime);
	}
	
	public void render() {
		repaint();
	}
	
	// OUTROS METODOS -------------------------
	public void testeColisoes(double deltaTime) {
		// colisão do jogador com os limites da tela ---------------------------------
		if(jogador.posX + (jogador.raio*2) >= Principal.LARGURA_TELA || jogador.posX <= 0){
			jogador.desmoverX(deltaTime);
		}
		if(jogador.posY + (jogador.raio*2) >= Principal.ALTURA_TELA || jogador.posY <= 0) {
			jogador.desmoverY(deltaTime);
		}
		
		// colisão do jogador com o limite direito do campo ------------------------------
		if(jogador.posX <= Principal.LIMITE_DIREITO) {
			jogador.desmoverX(deltaTime);
		}
	}
		
	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // converte o objeto Graphics para Graphics2D
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintComponent(g2d);
		// desenha o chão do cenário
		g2d.drawImage(bg, 0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA, null);
		// desenha as marcações dos limites de movimentação
		g2d.setColor(Color.GRAY);
		g2d.fillRect(Principal.LIMITE_DIREITO, 0, 5, Principal.ALTURA_TELA);
		g2d.fillRect(Principal.LIMITE_ESQUERDO, 0, 5, Principal.ALTURA_TELA);
		// desenha os elementos na tela
		g2d.drawImage(jogador.imgAtual, jogador.af, null);
		g2d.drawImage(inimigo.img, inimigo.af, null);
	}
}