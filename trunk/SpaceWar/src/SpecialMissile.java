import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class SpecialMissile {
	public static int SM_ST_DEATH = 0;
	public static int SM_ST_ALIVE = 1;
	public static int SM_ST_BLAST = 2;
	public static int SM_WIDTH = 120;
	public static int SM_HEIGHT = 120;
	
	int state;
	private int x, y;
	private int dy;
	private int count;
	private int boomcount = 100;
	
	private Rectangle bb;
	
	SpecialMissile() {
		state = SM_ST_DEATH;
		bb = new Rectangle(0, 0, SM_WIDTH, SM_HEIGHT);
	}
	
	int getState(){
		return state;
	}
	Rectangle getBBox(){
		return bb;
	}
	
	void shot (int x, int y){
		if(state == SM_ST_DEATH){
			state = SM_ST_ALIVE;
			this.x = x;
			this.y = y;
			dy = -10;
			
		}
	}
	
	void move() {
		if(state == SM_ST_ALIVE){
			y += dy;
			count = (count + 1) % 2;
			if(y == SpaceWar.FRAME_H/2 ){
				blast();
				bb = new Rectangle(x - SM_WIDTH/2, y - SM_HEIGHT/2, SM_WIDTH, SM_HEIGHT);
				boomcount = 150;
			}
		}
	}
	
	void blast(){
		state = SM_ST_BLAST;
	}
	
	void draw(Graphics g){
		
		if(state == SM_ST_ALIVE){
			g.setColor(Color.RED);
			g.fillOval(x - 8, y - 40, 16, 32);
			g.setColor(new Color(255, 255, 255));
			g.fillOval(x - 20, y, 40, 16);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(x - 8, y - 24, 16, 44);
			if( count == 1){
				g.setColor(Color.RED);
				g.fillOval(x - 8, y + 20, 16, 40);
				g.setColor(Color.YELLOW);
				g.fillOval(x - 4, y + 20, 8, 28);
			}
		}
		
		else if(state == SM_ST_BLAST){
			for(int i = 1; i < boomcount; i++){
				g.setColor(Util.randColor(128, 255));
				int x0 = Util.rand(-60, 60);
				int y0 = Util.rand(-60, 60);
				int r0 = Util.rand(5, 60);
				g.fillOval(x - x0 - r0/2, y - y0 -r0/2, r0, r0);
			}
			boomcount--;
			if(boomcount == 0)
				state = SM_ST_DEATH;
		}
		
	}
}
