package com.neet.Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.neet.Audio.Audio;
import com.neet.Entity.Enemy;
import com.neet.Entity.Player;
import com.neet.Handlers.Content;
import com.neet.TileMap.TileMap;

public class Bomber extends Enemy {
	
	private Player player;
	private ArrayList<Enemy> enemies;
	
	private BufferedImage[] idleSprites;
	private BufferedImage[] jumpSprites;
	private BufferedImage[] attackSprites;
	
	private boolean jumping;
	private boolean knockback;
	
	private static final int IDLE = 0;
	private static final int JUMPING = 1;
	private static final int ATTACKING = 2;
	
	private int attackTick;
	private int attackDelay = 30;
	private int step;
	
	public Bomber(TileMap tm, Player p, ArrayList<Enemy> en) {
		
		super(tm);
		player = p;
		enemies = en;
		

		 //SetDifficult
		if(com.neet.GameState.ChooseDifficultyState.hard()) {
			health = maxHealth = 3;
		}
		else {
			health = maxHealth = 1;
		}

		width = 30;
		height = 30;
		cwidth = 8;
		cheight = 25;
		
		damage = 1;
		moveSpeed = 1.5;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		idleSprites = Content.Bomber[0];
		jumpSprites = Content.Bomber[1];
		attackSprites = Content.Bomber[2];
		
		animation.setFrames(idleSprites);
		animation.setDelay(-1);
		
		attackTick = 0;
		
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
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		if(player.getx() < x) facingRight = false;
		else facingRight = true;
		
		// idle
		if(step == 0) {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idleSprites);
				animation.setDelay(-1);
			}
			attackTick++;
			if(attackTick >= attackDelay && Math.abs(player.getx() - x) < 60) {
				step++;
				attackTick = 0;
			}
		}
		// jump away
		if(step == 1) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}
			jumping = true;
			if(facingRight) left = true;
			else right = true;
			if(falling) {
				step++;
			}
		}
		// attack
		if(step == 2) {
			if(dy > 0 && currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attackSprites);
				animation.setDelay(3);
				Bomb de = new Bomb(tileMap);
				de.setPosition(x, y);
				if(facingRight) de.setVector(3, 3);
				else de.setVector(-3, 3);
				enemies.add(de);
			}
			if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
				step++;
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}
		}
		// done attacking
		if(step == 3) {
			if(dy == 0) step++;
		}
		// land
		if(step == 4) {
			step = 0;
			left = right = jumping = false;
		}
		
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
				dx = 2;
			else
				dx = -2;
			dy = -2;
			knockback = true;
			falling = true;
			jumping = false;
			
		}
	
	public void draw(Graphics2D g) {
		
		if (flinching) {
			if (flinchCount % 10 < 10)
				return;
		}
		
		super.draw(g);
		
	}
	
}
