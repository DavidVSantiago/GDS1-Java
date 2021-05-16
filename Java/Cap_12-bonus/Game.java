import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Game extends JPanel {
	private Jogador jogador;
	private Inimigo inimigo;
	private Bolinha bolinha;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage bg;
	private long tempoAtual;
	private long tempoAnterior;
	private double deltaTime;
	private double FPS_limit = 60;
	private char ESTADO;
	private BufferedImage splashLogo;

	public Game() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(ESTADO == 'E') {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: k_cima = false; break;
					case KeyEvent.VK_DOWN: k_baixo = false; break;
					case KeyEvent.VK_LEFT: k_esquerda = false; break;
					case KeyEvent.VK_RIGHT: k_direita = false; break;
					}	
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(ESTADO == 'E') {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: k_cima = true;	break;
					case KeyEvent.VK_DOWN: k_baixo = true; break;
					case KeyEvent.VK_LEFT: k_esquerda = true; break;
					case KeyEvent.VK_RIGHT: k_direita = true; break;
					case KeyEvent.VK_ESCAPE: ESTADO = 'P'; 
						Recursos.getInstance().tocarSomMenu();
						break;
					}	
				}else if(ESTADO == 'P'){
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						Recursos.getInstance().tocarSomMenu();
						Recursos.getInstance().pauseOpt=0;
						break;
					case KeyEvent.VK_DOWN:
						Recursos.getInstance().tocarSomMenu();
						Recursos.getInstance().pauseOpt=1;
						break;
					case KeyEvent.VK_ENTER:
						Recursos.getInstance().tocarSomMenu();
						if(Recursos.getInstance().pauseOpt==0) {
							ESTADO = 'E';
							k_cima=false;
							k_baixo=false; 
							k_esquerda=false;
							k_direita=false; 
						}
						else
							System.exit(0);
						break;
					case KeyEvent.VK_ESCAPE: ESTADO = 'E';
						Recursos.getInstance().tocarSomMenu();
						k_cima=false;
						k_baixo=false; 
						k_esquerda=false;
						k_direita=false; 
						break;
					}
				}
			}
		});
		
		jogador = new Jogador();
		inimigo = new Inimigo();
		bolinha = new Bolinha();
		ESTADO = 'S';
		agendarTransicao(3000, 'E');
		try {
			bg = ImageIO.read(getClass().getResource("imgs/bg.png"));
			splashLogo = ImageIO.read(getClass().getResource("imgs/logo.png"));
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
		double tempoMinimo = (1e9) / FPS_limit; // duração mínima do quadro (em nanosegundos)
		while (true) { // repetição intermitente do gameloop
			tempoAtual = System.nanoTime();
			deltaTime = (tempoAtual - tempoAnterior) * (6e-8);
			handlerEvents();
			update(deltaTime);
			render();
			tempoAnterior = tempoAtual; // no próximo quadro, o tempo final será o inicial desse quadro
			try {
				int tempoEspera = (int) ((tempoMinimo - deltaTime) * (1e-6));
				Thread.sleep(tempoEspera);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handlerEvents() {
		if(ESTADO == 'E') {			
			jogador.handlerEvents(k_cima, k_baixo, k_esquerda, k_direita);
			inimigo.handlerEvents(bolinha);
		}
	}

	public void update(double deltaTime) {
		if(ESTADO == 'E') {
			jogador.update(deltaTime);
			inimigo.update(deltaTime);
			bolinha.update(deltaTime);
			testeColisoes(deltaTime);
			testeFimJogo();
		}else if(ESTADO == 'G') {
			ESTADO = 'R';
			reiniciar(); // reinicia as variáveis do jogo
			agendarTransicao(2000, 'E'); // agenda uma mudança para o estado EXECUTANDO após 2 segundos.
		}
	}

	public void render() {
		repaint();
	}

	// OUTROS METODOS -------------------------
	public void testeColisoes(double deltaTime) {
		// colisão do jogador com os limites da tela ---------------------------------
		if (jogador.posX + (jogador.raio * 2) >= Principal.LARGURA_TELA || jogador.posX <= 0) {
			jogador.desmoverX(deltaTime);
		}
		if (jogador.posY + (jogador.raio * 2) >= Principal.ALTURA_TELA || jogador.posY <= 0) {
			jogador.desmoverY(deltaTime);
		}

		// colisão do jogador com o limite direito do campo
		if (jogador.posX <= Principal.LIMITE_DIREITO) {
			jogador.desmoverX(deltaTime);
		}

		// colisão da bolinha com o lado direito da tela
		if (bolinha.posX + (bolinha.raio * 2) >= Principal.LARGURA_TELA) {
			bolinha.velX = bolinha.velX * -1;
			bolinha.posX = Principal.LARGURA_TELA/2 - (bolinha.raio) + 90;
			Recursos.getInstance().pontosInimigo++;
		}

		// colisão da bolinha com o lado esquerdo da tela
		if (bolinha.posX <= 0) {
			bolinha.velX = bolinha.velX * -1;
			bolinha.posX = Principal.LARGURA_TELA/2 - (bolinha.raio) - 90;
			Recursos.getInstance().pontosJogador++;
		}

		// colisão da bolinha com o lado inferior da tela
		if (bolinha.posY + (bolinha.raio * 2) >= Principal.ALTURA_TELA) {
			bolinha.velY = bolinha.velY * -1;
			bolinha.posY = Principal.ALTURA_TELA - (bolinha.raio * 2);
			Recursos.getInstance().tocarSomBolinha();
		}

		// colisão da bolinha com o lado superior da tela
		if (bolinha.posY <= 0) {
			bolinha.velY = bolinha.velY * -1;
			bolinha.posY = 0;
			Recursos.getInstance().tocarSomBolinha();
		}
		
		// colisão da bolinha com o jogador
		double ladoHorizontal = jogador.centroX - bolinha.centroX;
		double ladorVertical = jogador.centroY - bolinha.centroY;
		double hipotenusa = Math.sqrt( Math.pow(ladoHorizontal, 2) + Math.pow(ladorVertical, 2));
		if(hipotenusa <= jogador.raio + bolinha.raio) {
			jogador.desmoverX(deltaTime);
			jogador.desmoverY(deltaTime);
			double seno, cosseno;
			cosseno = ladoHorizontal/hipotenusa;
			seno = ladorVertical/hipotenusa;
			bolinha.velX = (- bolinha.velBase) * cosseno;
			bolinha.velY = (- bolinha.velBase) * seno;
			Recursos.getInstance().tocarSomBolinha();
		}
		
		// colisão da bolinha com o inimigo
		ladoHorizontal = inimigo.centroX - bolinha.centroX;
		ladorVertical = inimigo.centroY - bolinha.centroY;
		hipotenusa = Math.sqrt(Math.pow(ladoHorizontal, 2) + Math.pow(ladorVertical, 2));
		if(hipotenusa <= inimigo.raio+bolinha.raio) {
			inimigo.desmoverX(deltaTime);
			inimigo.desmoverY(deltaTime);
			double seno, cosseno;
			cosseno = ladoHorizontal/hipotenusa;
			seno = ladorVertical/hipotenusa;
			bolinha.velX = (- bolinha.velBase) * cosseno;
			bolinha.velY = (- bolinha.velBase) * seno;
			Recursos.getInstance().tocarSomBolinha();
		}
	}
	
	public void agendarTransicao(int tempo, char novoEstado) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(tempo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ESTADO = novoEstado;
			}
		});
		thread.start();
	}
	
	public void testeFimJogo() {
		if (Recursos.getInstance().pontosJogador == Recursos.getInstance().maxPontos){
			Recursos.getInstance().msgFim = "VOCÊ VENCEU!";
			ESTADO='G';
		}else if(Recursos.getInstance().pontosInimigo == Recursos.getInstance().maxPontos){
			Recursos.getInstance().msgFim = "VOCÊ PERDEU!";
			ESTADO='G';
		}
	}
	
	public void reiniciar() {
		// reiniciamos os atributos do inimigo
		inimigo.posX = (Principal.LARGURA_TELA * ( 1.0 / 8.0 ) - inimigo.raio);
		inimigo.posY = (Principal.ALTURA_TELA/2) - inimigo.raio;
		inimigo.velY = inimigo.velBase;
		Recursos.getInstance().pontosInimigo = 0;
		// reiniciamos os atributos do jogador
		jogador.posX = (Principal.LARGURA_TELA * (7.0/8.0) - jogador.raio);
		jogador.posY = (Principal.ALTURA_TELA / 2) - jogador.raio;
		Recursos.getInstance().pontosJogador = 0;
		// reiniciamos os atributos da bolinha
		bolinha.velX = bolinha.velBase/2;
		bolinha.velY = bolinha.velBase/2;
		bolinha.posX = (Principal.LARGURA_TELA/2 - bolinha.raio);
		bolinha.posY = (Principal.ALTURA_TELA/2) - bolinha.raio;
		// reinicia as variáveis das teclas direcionais
		k_cima=false;
		k_baixo=false; 
		k_esquerda=false;
		k_direita=false; 
	}

	// METODO SOBRESCRITO ---------------------
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // converte o objeto Graphics para Graphics2D
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintComponent(g2d);
		
		if(ESTADO == 'S') { // se estiver no Estado SPLASH
			g2d.drawImage(splashLogo, 0, 0, null);
		} else if(ESTADO == 'R') { // se estiver no Estado REINICIANDO
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA);
			g2d.setFont(Recursos.getInstance().fontMenu);
			g2d.setColor(Color.WHITE);
			g2d.drawString(Recursos.getInstance().msgFim, 150, 200);
		} else { // se estiver no Estado EXECUTANDO ou PAUSADO
			// desenha o chão do cenário
			g2d.drawImage(bg, 0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA, null);
			// desenha as marcações dos limites de movimentação
			g2d.setColor(Color.GRAY);
			g2d.fillRect(Principal.LIMITE_DIREITO, 0, 5, Principal.ALTURA_TELA);
			g2d.fillRect(Principal.LIMITE_ESQUERDO, 0, 5, Principal.ALTURA_TELA);
			// desenha os elementos na tela
			g2d.drawImage(jogador.imgAtual, jogador.af, null);
			g2d.drawImage(inimigo.imgAtual, inimigo.af, null);
			g2d.drawImage(bolinha.img, bolinha.af, null);
			if(ESTADO == 'E') { // se estiver no Estado EXECUTANDO
				// desenha a pontuação na tela
				g2d.setFont(Recursos.getInstance().fontPontuacao); 
				g2d.setColor(Color.WHITE);
				g2d.drawString(Recursos.getInstance().pontosInimigo + "pts", 140, 40);
				g2d.drawString(Recursos.getInstance().pontosJogador + "pts", 440, 40);
			} else { // se estiver no Estado PAUSADO
				g2d.setColor(new Color(0, 0, 0, 128));
				g2d.fillRect(0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA);
				// desenha os elementos do menu pause
				g2d.setFont(Recursos.getInstance().fontMenu);
				g2d.setColor(Color.WHITE);
				g2d.drawString("JOGO PAUSADO", 150, 80); 
				g2d.drawString("Continuar", 220, 200);
				g2d.drawString("Sair", 220, 270);
				// desenha o seletor de opções
				if (Recursos.getInstance().pauseOpt == 0) {
					g2d.fillRect(180, 170, 30, 30);
				} else {
					g2d.fillRect(180, 240, 30, 30);
				}
			}
		}
	}
}