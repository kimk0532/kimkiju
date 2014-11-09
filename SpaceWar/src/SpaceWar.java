import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SpaceWarComponent extends JComponent{
	public static int TIME_SLICE = 30;
	public static int MAX_STAR = 1000;
	public static int MAX_UFO = 5;
	public static int MAX_MISSILE = 5;
	public static int MAX_BOMB = 3;
	public static int ST_TITLE = 0;
	public static int ST_GAME = 1;
	public static int ST_ENDING = 2;
	
	private int state;
	private int score;
	private int s_m;
	private int life;
	private int count;
	private Timer t;
	private Star[] star;
	private Ufo[] ufo;
	private MyShip me;
	private Missile[] myShot;
	private Bomb[] enemyShot;
	private SpecialMissile sm;
	SpaceWarComponent() {
		t = new Timer(TIME_SLICE, new TimerHandler());
		t.start();
		
		state = ST_TITLE;
		count = 0;
		
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);
		
		star = new Star[MAX_STAR];
		for (int i = 0; i < MAX_STAR; i++){
			star[i] = new Star();
		}
		
		ufo = new Ufo[MAX_UFO];
		for (int i = 0; i < MAX_UFO; i++){
			ufo[i] = new Ufo();
		}
		
		me = new MyShip();
		
		myShot = new Missile[MAX_MISSILE];
		for (int i = 0; i < MAX_MISSILE; i++){
			myShot[i] = new Missile();
		}
		
		enemyShot = new Bomb[MAX_BOMB];
		for (int i = 0; i < MAX_BOMB; i++){
			enemyShot[i] = new Bomb();
		}
		
		sm = new SpecialMissile();
	}
	
	class TimerHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			for(Star s: star){
				s.move();
			}
			for(Ufo u: ufo){
				if(u.getState() == Ufo.UFO_ST_DEATH && Util.prob100(10))
					u.birth();
				if (u.getState() == Ufo.UFO_ST_ALIVE && Util.prob100(1)){
					for(int i = 0; i < MAX_BOMB; i++){
						if(enemyShot[i].state == Bomb.BM_ST_DEATH && state == ST_GAME){
							enemyShot[i].shot((int)u.getX(), (int)u.getY(), me.getX(), me.getY());
							break;
						}
					}
				}
				u.move();
			}
			for(int i = 0; i < MAX_MISSILE; i++){
				if(myShot[i].state == Missile.MI_ST_ALIVE){
					myShot[i].move();
					for(Ufo u: ufo){
						if(u.getState() == Ufo.UFO_ST_ALIVE){
							if(myShot[i].getBBox().intersects(u.getBBox())){
								u.blast();
								myShot[i].blast();
								score += 10;
								break;
							}
						}
					}
				}
			}
			for(int i = 0; i < MAX_BOMB; i++){
				if(enemyShot[i].getState() == Bomb.BM_ST_ALIVE){
					enemyShot[i].move();
					if(me.getState() == MyShip.ME_ST_ALIVE){
						if(me.getBBox().intersects(enemyShot[i].getBBox())){
							me.blast();
							enemyShot[i].blast();
							life--;
							if(life == 0)
								state = ST_ENDING;
						}
					}
				}
			}
			if(sm.getState() == SpecialMissile.SM_ST_ALIVE)
				sm.move();
			else if(sm.getState() == SpecialMissile.SM_ST_BLAST){
				for(Ufo u : ufo){
					if(sm.getBBox().intersects(u.getBBox())){
						u.blast();
						score += 10;
					}
				}
			}
			count = (count +1) % 20;
			if(state == ST_ENDING){
				if(count == 0)
					me.blast();
			}
			repaint();
		}
	}
	
	class KeyHandler extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if(state == ST_TITLE){
				if(code == KeyEvent.VK_SPACE){
					state = ST_GAME;
					score = 0;
					life = 3;
					s_m = 3;
					me.startMyShip();
					for(int i = 0; i < MAX_UFO; i++)
						ufo[i].state = Ufo.UFO_ST_DEATH;
				}
			}
			else if(state == ST_GAME){
				if (code == KeyEvent.VK_LEFT)
					me.moveLeft();
				else if (code == KeyEvent.VK_RIGHT)
					me.moveRight();
				else if (code == KeyEvent.VK_SPACE){
					for(int i = 0; i < MAX_MISSILE; i++){
						if(myShot[i].state == Missile.MI_ST_DEATH){
							myShot[i].shot(me.getX(), me.getY());
							break;
						}
					}
				}
				else if (code == KeyEvent.VK_SHIFT){
					if(s_m > 0){
						if(sm.state == sm.SM_ST_DEATH)
							s_m--;
						sm.shot(me.getX(), me.getY());
					}
				}
			}
			else if(state == ST_ENDING){
				if(code == KeyEvent.VK_ENTER)
					state = ST_TITLE;
			}
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SpaceWar.FRAME_W, SpaceWar.FRAME_H);
		for(Star s: star)
			s.draw(g);
		for(Ufo u: ufo)
			u.draw(g);
		for(Missile m: myShot)
			m.draw(g);
		for(Bomb b: enemyShot)
			b.draw(g);
		sm.draw(g);
		if(state != ST_TITLE)
			me.draw(g);
		if(state == ST_TITLE){
			int tx = SpaceWar.FRAME_W/2 - 300;
			int ty = SpaceWar.FRAME_H/2 - 50;
			int i, j;
			
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 100);
			g.setFont(font);
			g.setColor(Color.WHITE);
			for(i = -5; i <= 5; i++){
				for(j = -5; j <= 5; j++)
					g.drawString("SPACE WAR", tx - i, ty - j);
			}
			g.setColor(Color.RED);
			g.drawString("SPACE WAR", tx, ty);
			font = new Font(Font.SANS_SERIF, Font.BOLD, 36);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("201000294", SpaceWar.FRAME_W/2 - 150, SpaceWar.FRAME_H/2 + 30);
			g.drawString("±è±âÁÖ         ", SpaceWar.FRAME_W/2 - 150, SpaceWar.FRAME_H/2 + 60);
			if(count < 10)
				g.drawString("PRESS SPACE KEY", SpaceWar.FRAME_W/2 - 150, SpaceWar.FRAME_H/4*3);
		}
		else if(state == ST_GAME){
			Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("SCORE: " + score, 20, 30);
			g.drawString(" LIFE: " + life, 20, 60);
			g.drawString("  S.M: " + s_m, 20, 90);
		}
		else if(state == ST_ENDING){
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 36);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("YOUR SCORE IS " + score, SpaceWar.FRAME_W/2 - 150, SpaceWar.FRAME_H/3);
			if(count < 10){
				g.drawString("PRESS ENTER KEY", SpaceWar.FRAME_W/2 - 150, SpaceWar.FRAME_H/3*2);
			}
		}
	}
}

public class SpaceWar {
	public static int FRAME_W = 800;
	public static int FRAME_H = 600;
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Space War");
		f.setSize(FRAME_W + 8, FRAME_H + 34);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpaceWarComponent sc = new SpaceWarComponent();
		f.add(sc);
		f.setVisible(true);
	}
}
