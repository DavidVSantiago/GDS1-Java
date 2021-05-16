import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Bolinha {	
	public BufferedImage sprite;
	public BufferedImage img;
	public AffineTransform af;
	public double posX;
	public double posY;
	public double raio;
	public double velBase;
	public double velX;
	public double velY;
	public double centroX;
	public double centroY;
	
	public Bolinha() {
		try {
			sprite = ImageIO.read(getClass().getResource("imgs/sprite_person_bola.png"));
			img = Recursos.getInstance().cortarImagem(400, 100, 430, 130, sprite);
		}catch (Exception e) {
			System.out.println("Erro ao carregar a imagem!");
		}
		af = new AffineTransform();
		raio = 15;
		posX = (Principal.LARGURA_TELA/2 - raio); // TODO
		posY = (Principal.ALTURA_TELA/2)-raio; // TODO
		velBase = 5;
		velX = velBase/2;
		velY = velBase/2;
		centroX = posX+raio;
		centroY = posY+raio;
	}
	public void update(double deltaTime) {
		mover(deltaTime);
		centroX = posX+raio;
		centroY = posY+raio;
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
