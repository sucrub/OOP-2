package com.neet.Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.neet.Audio.Audio;
import com.neet.Entity.Enemy;
import com.neet.Entity.Player;
import com.neet.Handlers.Content;
import com.neet.Main.GamePanel;
import com.neet.TileMap.TileMap;

public class Goblin extends Enemy {
	
	private BufferedImage[] sprites;
	private Player player;
	private boolean active;
	private boolean knockback;
	
	public Goblin(TileMap tm, Player p) {
		
		super(tm);
		player = p;
		

		 //SetDifficult
		if(com.neet.GameState.ChooseDifficultyState.hard()) {
			health = maxHealth =5;
		}
		else {
			health = maxHealth = 3;
		}

		width = 16;
		height = 16;
		cwidth = 8;
		cheight = 16;
		
		damage = 1;
		moveSpeed = 0.8;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		sprites = Content.Goblin[0];
		
		animation.setFrames(sprites);
		animation.setDelay(4);
		
		left = true;
		facingRight = false;
		
	}
	
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping && !falling) {
			dy = jumpStart;
		}
	}
	
	public void update() {
		
		if(!active) {
			if(Math.abs(player.getx() - x) < GamePanel.WIDTH) active = true;
			return;
		}
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		setPosition(xtemp, ytemp);
		
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		// update animation
		animation.update();
		
	}
	
	public void hit(int damage) {
		
		if(dead || flinching) return;
		Audio.play("enemyhit");
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
		flinching = true;
		flinchCount = 0;
		
		if (facingRight)
			dx = 5;
		else
			dx = -5;
		dy = -2;
		knockback = true;
		falling = true;
		jumping = false;
		
	}
	
	public void draw(Graphics2D g) {
		
//		if(flinching) {
//			if(flinchCount == 0 || flinchCount == 2) return;
//		}
		
		if (flinching) {
			if (flinchCount % 10 < 10)
				return;
		}
		
		super.draw(g);
		
	}
	
}
