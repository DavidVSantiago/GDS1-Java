import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Inimigo {
	public BufferedImage sprite;
	public BufferedImage cima;
	public BufferedImage baixo;
	public BufferedImage esquerda;
	public BufferedImage direita;
	public BufferedImage parada;
	public BufferedImage direita_baixo;
	public BufferedImage direita_cima;
	public BufferedImage esquerda_baixo;
	public BufferedImage esquerda_cima;
	public BufferedImage imgAtual;
	public AffineTransform af;
	public double posX;
	public double posY;
	public double raio;
	public double velBase;
	public double velX;
	public double velY;
	public double centroX;
	public double centroY;
	private long acumuladorTempoVertical;
	private long acumuladorTempoHorizontal;
	private long tempoAtual;
	private long tempoAnterior;
	
	public Inimigo() {
		try {
			sprite = ImageIO.read(getClass().getResource("imgs/sprite_inimigo.png"));
			cima = Recursos.getInstance().cortarImagem(100, 0, 200, 100, sprite);
			baixo = Recursos.getInstance().cortarImagem(0, 100, 100, 200, sprite);
			esquerda = Recursos.getInstance().cortarImagem(200, 100, 300, 200, sprite);
			direita = Recursos.getInstance().cortarImagem(300, 0, 400, 100, sprite);
			parada = Recursos.getInstance().cortarImagem(300, 100, 400, 200, sprite);
			esquerda_baixo = Recursos.getInstance().cortarImagem(100, 100, 200, 200, sprite);
			esquerda_cima = Recursos.getInstance().cortarImagem(0, 0, 100, 100, sprite);
			direita_baixo = Recursos.getInstance().cortarImagem(400, 0, 500, 100, sprite);
			direita_cima = Recursos.getInstance().cortarImagem(200, 0, 300, 100, sprite);
		}catch (Exception e) {
			e.printStackTrace();
		}
		imgAtual = parada;
		af = new AffineTransform();
		raio = 50;
		posX = (Principal.LARGURA_TELA * ( 1.0 / 8.0 ) - raio);
		posY = (Principal.ALTURA_TELA/2)-raio;
		velBase = 3;
		velX = 0;
		velY = 0;
		centroX = posX + raio;
		centroY = posY + raio;
		acumuladorTempoVertical = 0;
		acumuladorTempoHorizontal = 0;
		tempoAnterior = System.currentTimeMillis();
	}
	
	public void handlerEvents(Bolinha bolinha) {
		testeColisoes();
		
		tempoAtual = System.currentTimeMillis();
		acumuladorTempoVertical = acumuladorTempoVertical + (tempoAtual - tempoAnterior);
		acumuladorTempoHorizontal= acumuladorTempoHorizontal + (tempoAtual - tempoAnterior);
		
		if(acumuladorTempoVertical>=Recursos.getInstance().gerarAleatorio(80, 120)) {
			acumuladorTempoVertical = 0;
			movimentoVertical(bolinha);
		}
		
		if(acumuladorTempoHorizontal>=Recursos.getInstance().gerarAleatorio(400, 600)) {
			acumuladorTempoHorizontal = 0;
			movimentoHorizontal(bolinha);
		}
		mudarSprite();
		tempoAnterior = tempoAtual;
	}
	private void testeColisoes() {
		if(colideBaixo()==true) {
			velY =0;
			posY = Principal.ALTURA_TELA - raio*2;
		}
		if(colideCima()==true) {
			velY =0;
			posY=0;
		}
		if(colideEsquerda()==true) { 
			velX =0;
			posX=0;
		}
		else if(colideDireita()==true) { 
			velX =0;
			posX= Principal.LIMITE_ESQUERDO - raio*2;
		}		
	}	
	private void movimentoVertical(Bolinha bolinha) {
		velY = 0;
		double diferencaY = centroY - bolinha.centroY;
		double limite = raio*( Recursos.getInstance().gerarAleatorio(4, 8) / 10.0 );
		if(diferencaY < -limite) {
			velY = velBase;
		}else if(diferencaY > limite) {
			velY = -velBase;
		}
	}
	private void movimentoHorizontal(Bolinha bolinha) {
		velX=0;
		double distanciaBolinhaX = Math.abs(centroX-bolinha.centroX);
		double distanciaBolinhaY = Math.abs(centroY-bolinha.centroY);
		double aceleracao= distanciaBolinhaY/120;
		if(distanciaBolinhaX > 211) {
			velX = velBase*aceleracao;
		}else {
			velX = -velBase*aceleracao;
		}
	}
	private boolean colideCima() {
		return posY <= 0;
	}
	private boolean colideBaixo() {
		return posY + (raio*2) >= Principal.ALTURA_TELA;
	}
	private boolean colideDireita() {
		return posX + (raio*2) >= Principal.LIMITE_ESQUERDO;
	}
	private boolean colideEsquerda() {
		return posX <= 0;
	}
	private void mudarSprite() {
		imgAtual = parada;
		if (velY<0) {
			imgAtual = cima;
			if (velX>0) {
				imgAtual = direita_cima;
			}
			if (velX<0) {
				imgAtual = esquerda_cima;
			}
		} else if (velY>0) {
			imgAtual = baixo;
			if (velX>0) {
				imgAtual = direita_baixo;
			}
			if (velX<0) {
				imgAtual = esquerda_baixo;
			}
		} else if (velX<0) {
			imgAtual = esquerda;
		} else if (velX>0) {
			imgAtual = direita;
		}

	}
	
	public void update(double deltaTime) {
		mover(deltaTime);
		centroX = posX + raio;
		centroY = posY + raio;
	}
	
	public void mover(double deltaTime) {
		posX = posX + (velX * deltaTime);
		posY = posY + (velY * deltaTime);
		af.setToTranslation(posX, posY);
	}
	
	public void desmoverX(double deltaTime) {
		posX = posX - (velX * deltaTime);
		af.setToTranslation(posX, posY);
	}
	
	public void desmoverY(double deltaTime) {
		posY = posY - (velY * deltaTime);
		af.setToTranslation(posX, posY);
	}
}