import java.awt.*;


public class Star {
	public static int MAX_STAR_RAD = 5;
	
	private Color color;
	private int x, y;
	private int dy, size;
	
	Star(){
		init();
	}
	
	void init(){
		color = Util.randColor(0, 127);
		x = Util.rand(SpaceWar.FRAME_W - 1);
		y = Util.rand(SpaceWar.FRAME_H - 1);
		dy = Util.rand(1, MAX_STAR_RAD);
		size = dy*2;
	}
	
	void move(){
		y += dy;
		if(y > SpaceWar.FRAME_H){
			init();
			y = -Util.rand(SpaceWar.FRAME_H/10);
		}
	}
	
	void draw(Graphics g){
		g.setColor(color);
		g.fillOval(x -dy, y - dy, size, size);
	}

}
