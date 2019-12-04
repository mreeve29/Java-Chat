import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import BreezySwing.GBFrame;

public class ChatServer extends GBFrame{

	private JTextArea chatArea = addTextArea("",2,1,1,1);
	
	private ServerSocket serverSocket = null;
    private Socket socket = null;
    
    private ArrayList<PrintWriter> clients = new ArrayList<PrintWriter>();
    
	public static void main(String[] args) {
		ChatServer frm = new ChatServer();
		frm.setSize(400,400);
		frm.setTitle("Sever");
		frm.setVisible(true);
	}
	
	public ChatServer() {
		addLabel("Server is running",1,1,1,1);
		chatArea.setEditable(false);
		new Thread(server).start();
	}

	
	Runnable server = new Runnable() {

		@Override
		public void run() {
			try {
	            serverSocket = new ServerSocket(59898);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        while (true) {
	            try {
	                socket = serverSocket.accept();
	            } catch (IOException e) {
	                System.out.println(e);
	            }
	            new ClientThread(socket).start();
	        }
		}
		
	};
	
	public class ClientThread extends Thread {
	    private Socket socket;

	    public ClientThread(Socket clientSocket) {
	        this.socket = clientSocket;
	    }

	    public void run() {	        
	        Scanner in;
	        PrintWriter out;
	        
	        try {
	        	in = new Scanner(socket.getInputStream());
	            out = new PrintWriter(socket.getOutputStream(), true);
	        } catch (IOException e) {
	            return;
	        }
	        clients.add(out);
	        String line;
	        out.println("ENTERNAME");
	        while (true) {
	            try {
	                line = in.nextLine();
	                System.out.println(line);
	                if(line.startsWith("NAME")) {
	                	chatArea.append(line.substring(4) + " joined\n");
	                	out.println(line.substring(4) + " joined");
	                	out.flush();
	                	return;
	                }
                	chatArea.append(line + '\n');
                	for(PrintWriter w : clients) {
                		w.println(line);
                	}
                	out.flush();
	                
	            } finally {
	            	
	            }
	        }
	    }
	}
}
