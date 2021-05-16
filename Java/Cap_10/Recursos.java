import java.awt.image.BufferedImage;

public class Recursos {
	private static Recursos singleton = null;
	
	// construtor privado! só pode ser invocado dentro desta classe.
	private Recursos() {
	}
	
	public static Recursos getInstance() {
		if(singleton == null) {
			singleton = new Recursos();
		}
		return singleton;
	}
	// Métodos --------------------------------------------
	public BufferedImage cortarImagem(int x1, int y1, int x2, int y2, BufferedImage img) {
		int largura = x2 - x1;
		int  altura = y2 - y1;
		return img.getSubimage(x1, y1, largura, altura);
	}
}
