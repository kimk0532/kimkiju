import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class CNServer implements Runnable {
	
	public static final int ServerPort = 9000;
	public static final String ServerIP = "192.168.0.20";
	
	@Override
	public void run() {
		try {
			System.out.println("S : connecting...");
			ServerSocket serverSocket = new ServerSocket(ServerPort);
			
			while(true){
				Socket client = serverSocket.accept();
				System.out.println("S : receiving...");
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String str = in.readLine();
					System.out.println("S : received "+str);
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
					out.println(str);
				} catch (Exception e) {
					System.out.println("S : error");
				}finally{
					client.close();
					System.out.println("S : done");
				}
			}
		} catch (Exception e) {
			System.out.println("S : error");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Thread serverThread = new Thread(new CNServer());
		serverThread.start();
	}
}
