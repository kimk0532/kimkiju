import java.awt.*;

public class Ufo {
	public static int UFO_ST_DEATH = 0;
	public static int UFO_ST_ALIVE = 1;
	public static int UFO_ST_BLAST = 2;
	public static int UFO_WIDTH = 80;
	public static int UFO_HEIGHT = 30;
	
	public int state;
	private int dr, count;
	private Color cr1, cr2;
	private double x, y, dx, dy, tx, ty, len;
	private Rectangle bb;
	
	Ufo(){
		state = UFO_ST_DEATH;
		bb = new Rectangle(0,0,UFO_WIDTH,UFO_HEIGHT);
	}
	
	int getState(){
		return state;
	}
	
	double getX(){
		return x;
	}
	
	double getY(){
		return y;
	}
	
	Rectangle getBBox(){
		return bb;
	}
	
	void birth(){
		state = UFO_ST_ALIVE;
		cr1 = Util.randColor();
		cr2 = Util.randColor();
		
		if(Util.prob100(100)){
			x = -40;
			dx = Util.rand(5, 10);
		}
		
		else {
			x = SpaceWar.FRAME_W + 40;
			dx = -Util.rand(5, 10);
		}
		tx = SpaceWar.FRAME_W;
		ty = 0;
		y = 0;
		len = Math.sqrt(Math.pow(tx-x, 2) + Math.pow(ty-y, 2));
		dx = (int) ((tx-x)*30/len);
		dy = (int) ((ty-y)*30/len);
		dr = 0;
		
		bb.x = (int) (x - UFO_WIDTH/2);
		bb.y = (int) (y - UFO_HEIGHT/2);
	}
	
	void blast() {
		state = UFO_ST_BLAST;
		count = 15;
		bb.x = 0;
		bb.y = 0;
	}
	
	void move(){
		if(state == UFO_ST_ALIVE){
			x += dx;
			y += dy;
			if(x < -40 || SpaceWar.FRAME_W + 40 < x || y < -40 || SpaceWar.FRAME_H +40 < y){
				state = UFO_ST_DEATH;
			}
			if(Math.abs(x-tx) < 50 && dr == 0){
				dr = 1;
				ty += 100;
				dx = 0;
				dy = (int) ((ty-y)*100/len);
			}
			else if(Math.abs(y-ty) < 50 && dr == 1){
				dr = 0;
				dy = 0;
				if(tx == SpaceWar.FRAME_W){
					tx = 0;
					dx = (int) ((tx-x)*30/len);
				}
				else if (tx == 0){
					tx = SpaceWar.FRAME_W;
					dx = (int) ((tx-x)*30/len);
				}
			}
			
			bb.x = (int)x - UFO_WIDTH/2;
			bb.y = (int)y - UFO_HEIGHT/2;
			
		}
		else if ( state == UFO_ST_BLAST){
			count--;
			if(count == 0)
				state = UFO_ST_DEATH;
		}
	}
	
	void draw(Graphics g){
		if(state == UFO_ST_ALIVE){
			g.setColor(cr1);
			g.fillOval((int)x-20, (int)y-15, 40, 30);
			g.setColor(cr2);
			g.fillOval((int)x-40, (int)y, 80, 10);
		}
		else if(state == UFO_ST_BLAST){
			for(int i = 1; i < count; i++){
				g.setColor(Util.randColor(128, 255));
				int x0 = Util.rand(-30, 30);
				int y0 = Util.rand(-30, 30);
				int r0 = Util.rand(5, 30);
				g.fillOval((int)x - x0 - r0/2, (int)y - y0 - r0/2, r0, r0);
			}
		}
	}
}
