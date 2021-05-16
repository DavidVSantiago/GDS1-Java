import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class Inimigo {
	public BufferedImage img;
	public int posX;
	public int posY;
	public int raio;
	public int centroX;
	public int centroY;
	
	public Inimigo() {
		try {
			img = ImageIO.read(getClass().getResource("imgs/inimigo.gif"));
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