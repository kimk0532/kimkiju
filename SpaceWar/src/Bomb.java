import java.awt.*;


public class Bomb {
	public static int BM_ST_DEATH = 0;
	public static int BM_ST_ALIVE = 1;
	public static int BM_WIDTH = 28;
	public static int BM_HEIGHT = 28;
	
	int state;
	private int x, y;
	private int dx, dy;
	private int count;
	
	private Rectangle bb;
	
	Bomb(){
		state = BM_ST_DEATH;
		bb = new Rectangle(0, 0, BM_WIDTH, BM_HEIGHT);
	}
	
	int getState(){
		return state;
	}
	
	Rectangle getBBox(){
		return bb;
	}
	
	void shot(int x, int y, int mx, int my){
		if(state == BM_ST_DEATH){
			state = BM_ST_ALIVE;
			this.x = x;
			this.y = y;
			dx = (mx - x)/50;
			dy = (my - y)/50;
			if(dx == 0 && dy == 0)
				state = BM_ST_DEATH;
			bb = new Rectangle(x - BM_WIDTH/2, y - BM_HEIGHT/2, BM_WIDTH, BM_HEIGHT);
		}
	}
	
	void move() {
		if(state == BM_ST_ALIVE){
			x += dx;
			y += dy;
			bb.x = x - BM_WIDTH/2;
			bb.y = y - BM_HEIGHT/2;
			if(SpaceWar.FRAME_H + 40 < y || 0 > y || SpaceWar.FRAME_W + 20 < x || 0 > x)
				state = BM_ST_DEATH;
			count = (count + 1) % 2;
		}
	}
	
	void blast(){
		state = BM_ST_DEATH;
	}
	
	void draw(Graphics g){
		if(this.state == BM_ST_ALIVE){
			g.setColor(Color.RED);
			g.fillOval(x - 14, y - 2, 28, 4);
			g.fillOval(x - 2, y - 14, 4, 28);
			g.setColor(new Color(0, 96, 0));
			g.fillOval(x - 10, y - 4, 20, 8);
			g.fillOval(x - 4, y - 10, 8, 20);
			g.setColor(Color.YELLOW);
			g.fillOval(x - 8, y - 8, 16, 16);
			if( count == 1){
				g.setColor(Color.RED);
				g.fillOval(x - 4, y - 4, 8, 8);
			}
		}
	}
}

