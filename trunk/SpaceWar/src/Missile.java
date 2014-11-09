import java.awt.*;


public class Missile {
	public static int MI_ST_DEATH = 0;
	public static int MI_ST_ALIVE = 1;
	public static int MI_WIDTH = 20;
	public static int MI_HEIGHT = 30;
	
	int state;
	private int x, y;
	private int dy;
	private int count;
	
	private Rectangle bb;
	
	Missile() {
		state = MI_ST_DEATH;
		bb = new Rectangle(0, 0, MI_WIDTH, MI_HEIGHT);
	}
	
	int getState(){
		return state;
	}
	Rectangle getBBox(){
		return bb;
	}
	
	void shot (int x, int y){
		if(state == MI_ST_DEATH){
			state = MI_ST_ALIVE;
			this.x = x;
			this.y = y;
			dy = -20;
			bb = new Rectangle(x - MI_WIDTH/2, y - MI_HEIGHT/2, MI_WIDTH, MI_HEIGHT);
		}
	}
	
	void move() {
		if(state == MI_ST_ALIVE){
			y += dy;
			bb.y = y -MI_HEIGHT/2;
			if(y < -40 )
				state = MI_ST_DEATH;
			count = (count + 1) % 2;
		}
	}
	
	void blast(){
		state = MI_ST_DEATH;
	}
	
	void draw(Graphics g){
		if(state == MI_ST_ALIVE){
			g.setColor(Color.RED);
			g.fillOval(x - 4, y - 20, 8, 16);
			g.setColor(new Color(0, 128, 0));
			g.fillOval(x - 10, y, 20, 8);
			g.setColor(new Color(0, 192, 0));
			g.fillRect(x - 4, y - 12, 8, 22);
			if( count == 1){
				g.setColor(Color.RED);
				g.fillOval(x - 4, y +10, 8, 20);
				g.setColor(Color.YELLOW);
				g.fillOval(x - 2, y + 10, 4, 14);
			}
		}
	}
}
