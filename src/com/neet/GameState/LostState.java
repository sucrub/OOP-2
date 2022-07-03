package com.neet.GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.neet.Handlers.Content;
import com.neet.Handlers.Keys;
import com.neet.Main.GamePanel;

public class LostState extends GameState {

	private Font font;
	private BufferedImage lost;
	
	public LostState(GameStateManager gsm) {
		super(gsm);
		
	}

	public void init() {}
	
	public void update() {
		
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		try {
			BufferedImage Lost = ImageIO.read(getClass().getResourceAsStream("/Sprites/Other/lost.png"));
			lost = Lost.getSubimage(0, 0, 500, 240);
		}catch(Exception e) {
			e.printStackTrace();
		}
		g.drawImage(lost, 0, 0, null );

	}
	
	public void handleInput() {
		
		if(Keys.isPressed(Keys.ESCAPE))
			gsm.setState(GameStateManager.MENUSTATE);
	}
}