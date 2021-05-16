import java.awt.image.BufferedImage;
import java.awt.Font;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Recursos {
	private static Recursos singleton = null;
	public int pontosJogador;
	public int pontosInimigo;
	public Font fontPontuacao;
	public int maxPontos;
	public Font fontMenu;
	public String msgFim;
	public int pauseOpt;
	public AudioInputStream menu;
	public AudioInputStream silence;
	public AudioInputStream set;
	public Clip clipMenu;
	public Clip clipSilence;
	public Clip clipSet;
	
	// construtor privado! só pode ser invocado dentro desta classe.
	private Recursos() {
		pontosJogador = 0;
		pontosInimigo = 0;
		fontPontuacao = new Font("Arial narrow", Font.LAYOUT_LEFT_TO_RIGHT, 40);
		maxPontos = 5;
		fontMenu = new Font("Arial narrow", Font.CENTER_BASELINE, 50);
		msgFim = "";
		pauseOpt = 0;
		carregarSons();
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
	
	public void carregarSons() {
		try {
			menu = AudioSystem.getAudioInputStream(getClass().getResource("audio/menu.wav"));
			silence = AudioSystem.getAudioInputStream(getClass().getResource("audio/silence.wav"));
			set = AudioSystem.getAudioInputStream(getClass().getResource("audio/set.wav"));

			clipMenu = AudioSystem.getClip();	
			clipSilence= AudioSystem.getClip();	
			clipSet = AudioSystem.getClip();	
			
			clipMenu.open(menu);
			clipSilence.open(silence);
			clipSet.open(set);
			
			clipSilence.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void tocarSomBolinha() {
		clipSet.setFramePosition(0);
		clipSet.start(); 
	}
	
	public void tocarSomMenu() {
		clipMenu.setFramePosition(0);
		clipMenu.start(); 
	}
	
}