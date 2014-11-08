import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class MyShip {
	public static int ME_ST_ALIVE = 1;
	public static int ME_ST_BLAST = 2;
	public static int ME_WIDTH = 60;
	public static int ME_HEIGHT = 80;
	
	private int state;
	private int x, y;
	private int count;
	private Rectangle bb;
	
	int getState() { return state; }
	int getX() { return x; }
	int getY() { return y; }
	Rectangle getBBox() {
		return bb;
	}
	
	void startMyShip(){
		state = ME_ST_ALIVE;
		x = SpaceWar.FRAME_W / 2;
		y = SpaceWar.FRAME_H - 80;
		bb = new Rectangle(x - ME_WIDTH/2, y - ME_HEIGHT/2,ME_WIDTH,ME_HEIGHT);
	}
	
	void moveLeft(){
		if(state == ME_ST_ALIVE){
			if(x >= 70){
				x -= 20;
				bb.x = x - ME_WIDTH/2;
			}
		}
	}
	
	void moveRight(){
		if(state == ME_ST_ALIVE){
			if(x < SpaceWar.FRAME_W - 70){
				x += 20;
				bb.x = x - ME_WIDTH/2;
			}
		}
	}
	
	void blast(){
		state = ME_ST_BLAST;
		count = 30;
	}
	
	void draw(Graphics g) {
		g.setColor(new Color(0,96,0));
		g.fillOval(x - 25, y - 10, 50, 50);
		g.setColor(new Color(0, 128, 0));
		g.fillOval(x - 40, y + 5, 80, 40);
		g.setColor(new Color(0, 192, 0));
		g.fillOval(x -10, y-50, 20, 100);
		g.fillOval(x - 25, y - 10, 5, 55);
		g.fillOval(x + 20, y - 10, 5, 55);
		g.fillOval(x - 40, y + 5, 5, 35);
		g.fillOval(x + 35, y + 5, 5, 35);
		g.setColor(new Color(64, 255, 255));
		g.fillOval(x - 5, y - 25, 10, 30);
		g.setColor(Color.RED);
		g.fillOval(x - 40, y, 5, 10);
		g.fillOval(x - 25, y - 15, 5, 10);
		g.fillOval(x + 20, y - 15, 5, 10);

		g.fillOval(x + 35, y, 5, 10);
		if(state == ME_ST_BLAST){
			for(int i = 1; i < count; i++){
				g.setColor(Util.randColor(128, 255));
				int x0 = Util.rand(-40, 40);
				int y0 = Util.rand(-40, 40);
				int r0 = Util.rand(5, 40);
				g.fillOval(x - x0 - r0/2, y - y0 -r0/2, r0, r0);
			}
			count--;
			if(count == 0)
				state = ME_ST_ALIVE;
		}
	}
}
