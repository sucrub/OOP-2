package com.neet.GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.neet.Audio.Audio;
import com.neet.Entity.PlayerStatus;
import com.neet.Handlers.Keys;
import com.neet.Main.GamePanel;

public class MenuState extends GameState {
	
	private BufferedImage head;
	private BufferedImage bigHead;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Music",
		"Help",
		"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private Font font2;
	
	public MenuState(GameStateManager gsm) {
		
		super(gsm);
		
		try {
			
			// load floating head
			head = ImageIO.read(getClass().getResourceAsStream("/HUD/lifes_icon.png")).getSubimage(0, 0, 16, 16);
			bigHead = ImageIO.read(getClass().getResourceAsStream("/HUD/HEAD.png")).getSubimage(0, 0, 32, 32);
			
			// titles and fonts
			titleColor = Color.WHITE;
			titleFont = new Font("Times New Roman", Font.BOLD, 28);
			font = new Font("Times New Roman", Font.BOLD, 14);
			font2 = new Font("Times New Roman", Font.BOLD, 10);
			
			// load sound fx
			Audio.load("/SFX/menuoption.mp3", "menuoption");
			Audio.load("/SFX/menuselect.mp3", "menuselect");
			
			// load menu sound
			Audio.load("/Music/menusong.mp3", "menusong");
			Audio.loop("menusong", 600, Audio.getFrames("menusong") - 2200);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	
	public void update() {
		
		// check keys
		handleInput();
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("S U P E R    H E R O", 128, 90);
		g.drawImage(bigHead, 90, 65, null);
		
		// draw menu options
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Start", 230, 135);
		g.drawString("Music", 230, 155);
		g.drawString("Help", 230, 175);
		g.drawString("Quit", 230, 195);
		
		// draw floating head
		if(currentChoice == 0) g.drawImage(head, 210, 122, null);
		else if(currentChoice == 1) g.drawImage(head, 210, 142, null);
		else if(currentChoice == 2) g.drawImage(head, 210, 162, null);
		else if(currentChoice == 3) g.drawImage(head, 210, 182, null);
		
		// other
		g.setFont(font2);
		g.drawString("OOP Project!", 8, 232);
		
	}
	
	private void select() {
		
		if(currentChoice == 0) {
			Audio.play("menuselect");
			gsm.setState(GameStateManager.CHOOSEDIFFICULTYSTATE);
		}
		else if(currentChoice == 1) {
			Audio.play("menuselect");
			gsm.setState(GameStateManager.MUSICSTATE);
		}
		else if(currentChoice == 2) {
			Audio.play("menuselect");
			gsm.setState(GameStateManager.HELPSTATE);
		}
		
		else if(currentChoice == 3) {
			System.exit(0);
		}
	}
	
	public void handleInput() {
		
		if(Keys.isPressed(Keys.ENTER)) select();
		if(Keys.isPressed(Keys.UP)) {
			if(currentChoice > 0) {
				Audio.play("menuoption", 0);
				currentChoice--;
			}
		}
		if(Keys.isPressed(Keys.DOWN)) {
			if(currentChoice < options.length - 1) {
				Audio.play("menuoption", 0);
				currentChoice++;
			}
		}
	}
	
}










