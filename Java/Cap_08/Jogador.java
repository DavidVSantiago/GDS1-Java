import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;

public class Jogador {
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

	public Jogador() {
		try {
			sprite = ImageIO.read(getClass().getResource("imgs/sprite.png"));
			cima = Recursos.getInstance().cortarImagem(100, 0, 200, 100, sprite);
			baixo = Recursos.getInstance().cortarImagem(0, 100, 100, 200, sprite);
			esquerda = Recursos.getInstance().cortarImagem(200, 100, 300, 200, sprite);
			direita = Recursos.getInstance().cortarImagem(300, 0, 400, 100, sprite);
			parada = Recursos.getInstance().cortarImagem(300, 100, 400, 200, sprite);
			esquerda_baixo = Recursos.getInstance().cortarImagem(100, 100, 200, 200, sprite);
			esquerda_cima = Recursos.getInstance().cortarImagem(0, 0, 100, 100, sprite);
			direita_baixo = Recursos.getInstance().cortarImagem(400, 0, 500, 100, sprite);
			direita_cima = Recursos.getInstance().cortarImagem(200, 0, 300, 100, sprite);
		} catch (Exception e) {
			System.out.println("Erro ao carregar a imagem!");
		}
		imgAtual = parada;
		af = new AffineTransform();
		velBase = 3;
		raio = 50;
		posX = (Principal.LARGURA_TELA * (7.0/8.0) - raio);
		posY = (Principal.ALTURA_TELA / 2) - raio;
		velX = 0;
		velY = 0;
		centroX = posX + raio;
		centroY = posY + raio;
	}

	public void handlerEvents(boolean k_cima, boolean k_baixo,
			boolean k_esquerda, boolean k_direita) {
		velX = 0;
		velY = 0;
		imgAtual = parada;
		if (k_cima == true) {
			velY = -velBase;
			imgAtual = cima;
			if (k_direita == true) {
				velX = velBase;
				imgAtual = direita_cima;
			}
			if (k_esquerda == true) {
				velX = -velBase;
				imgAtual = esquerda_cima;
			}
		} else if (k_baixo == true) {
			velY = velBase;
			imgAtual = baixo;
			if (k_direita == true) {
				velX = velBase;
				imgAtual = direita_baixo;
			}
			if (k_esquerda == true) {
				velX = -velBase;
				imgAtual = esquerda_baixo;
			}
		} else if (k_esquerda == true) {
			velX = -velBase;
			imgAtual = esquerda;
		} else if (k_direita == true) {
			velX = velBase;
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
