import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class Inimigo {
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
	
	public Inimigo() {
		try {
			sprite = ImageIO.read(getClass().getResource("imgs/sprite.png"));
			img = Recursos.getInstance().cortarImagem(400, 100, 500, 200, sprite);
		}catch (Exception e) {
			e.printStackTrace();
		}
		af = new AffineTransform();
		raio = 50;
		posX = (Principal.LARGURA_TELA * ( 1.0 / 8.0 ) - raio);
		posY = (Principal.ALTURA_TELA/2)-raio;
		velBase = 3;
		velX = 0;
		velY = 0;
		centroX = posX + raio;
		centroY = posY + raio;
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