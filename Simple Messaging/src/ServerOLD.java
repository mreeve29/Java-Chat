import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import BreezySwing.*;
import javax.swing.*;

public class ServerOLD extends GBFrame{
	
	private static Set<PrintWriter> writers = new HashSet<>();
	
	private JTextArea chatArea = addTextArea("",1,1,1,1);
	
	public static void main(String[] args) throws Exception{
		try(ServerSocket listener = new ServerSocket(59898)){
			System.out.println("Messaging Server is running...");
			ExecutorService pool = Executors.newFixedThreadPool(20);
			while(true) {
				pool.execute(new MessageHandeler(listener.accept()));
			}
		}
	}
	
	private static class MessageHandeler implements Runnable{
		private Socket socket;
		
		public MessageHandeler(Socket s) {
			socket = s;
		}

		@Override
		public void run() {
			System.out.println("connected " + socket);
			try {
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				writers.add(out);
				
				while(true) {
					String input = in.nextLine();
					for(PrintWriter pw : writers) {
						pw.println(input);
					}
				}
				
			}catch (Exception e) {
				System.out.println("Error " + socket);
			}
		}
		
	}
}
