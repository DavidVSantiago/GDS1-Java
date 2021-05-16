import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class Inimigo {
	public BufferedImage sprite;
	public BufferedImage img;
	public double posX;
	public double posY;
	public double raio;
	public double centroX;
	public double centroY;
	
	public Inimigo() {
		try {
			sprite = ImageIO.read(getClass().getResource("imgs/sprite.png"));
			img = Recursos.getInstance().cortarImagem(400, 100, 500, 200, sprite);
		}catch (Exception e) {
			e.printStackTrace();
		}
		raio = 50;
		posX = (Principal.LARGURA_TELA/2)-raio;
		posY = (Principal.ALTURA_TELA/2)-raio;
		centroX = posX + raio;
		centroY = posY + raio;
	}
}