import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bola {	
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
	public int posX;
	public int posY;
	public int raio;
	public int velX;
	public int velY;
	public int centroX;
	public int centroY;
	
	public Bola() {
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
		}catch (Exception e) {
			System.out.println("Erro ao carregar a imagem!");
		}
		posX = 100;
		posY = 100;
		raio = 50;
		velX = 0;
		velY = 0;
		centroX = posX+raio;
		centroY = posY+raio;
	}
}
