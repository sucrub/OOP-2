package com.neet.Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.neet.Entity.Enemy;
import com.neet.Entity.Player;
import com.neet.Handlers.Content;
import com.neet.Main.GamePanel;
import com.neet.TileMap.TileMap;

public class BigBoss extends Enemy {
	
	private Player player;
	private boolean active;
	private ArrayList<Enemy> enemies;
	
	private BufferedImage[] jumpSprites;
	private BufferedImage[] idleSprites;
	private BufferedImage[] attackSprites;
	
	private boolean jumping;
	
	private static final int IDLE = 0;
	private static final int JUMPING = 1;
	private static final int ATTACKING = 2;
	
	private int attackTick;
	private int attackDelay = 30;
	private int step;
	
	
	
	
	public BigBoss(TileMap tm, Player p, ArrayList<Enemy> en) {
		
		super(tm);
		player = p;
		enemies = en;
		
		health = maxHealth = 50;
		
		width = 64;
		height = 62;
		cwidth = 56;
		cheight = 44;
		
		damage = 2;
		moveSpeed = 1.5;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		idleSprites = Content.BigBoss[0];
		jumpSprites = Content.BigBoss[0];
		attackSprites = Content.BigBoss[0];
		
		animation.setFrames(idleSprites);
		animation.setDelay(-1);
		
		left = true;
		facingRight = false;
		
		attackTick =0;
		
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
				SpitBullets de = new SpitBullets(tileMap);
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
	
	public void draw(Graphics2D g) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.draw(g);
		
	}
}
