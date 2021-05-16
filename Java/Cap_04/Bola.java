import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bola {	
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
	
	public Bola() {
		try {
			cima = ImageIO.read(getClass().getResource("imgs/cima.gif"));
			baixo = ImageIO.read(getClass().getResource("imgs/baixo.gif"));
			esquerda = ImageIO.read(getClass().getResource("imgs/esquerda.gif"));
			direita = ImageIO.read(getClass().getResource("imgs/direita.gif"));
			parada = ImageIO.read(getClass().getResource("imgs/parada.gif"));
			esquerda_baixo = ImageIO.read(getClass().getResource("imgs/esquerda_baixo.gif"));
			esquerda_cima = ImageIO.read(getClass().getResource("imgs/esquerda_cima.gif"));
			direita_baixo = ImageIO.read(getClass().getResource("imgs/direita_baixo.gif"));
			direita_cima = ImageIO.read(getClass().getResource("imgs/direita_cima.gif"));
		}catch (Exception e) {
			System.out.println("Erro ao carregar a imagem!");
		}
		posX = 100;
		posY = 100;
		raio = 50;
		velX = 0;
		velY = 0;
	}
}
