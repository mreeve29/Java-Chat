import BreezySwing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;

public class ChatClient extends GBFrame {

	private JLabel nameLabel = addLabel("Set Name:", 3, 1, 1, 1);
	private JTextField nameField = addTextField("", 4, 1, 1, 1);
	private JButton enterChatButton = addButton("Enter Chat", 5, 1, 1, 1);
	
	private JTextField ipField = addTextField("",2,1,1,1);
	private JLabel ipLabel = addLabel("Enter IP(blank for localhost):",1,1,1,1);

	private JTextField messageField = addTextField("", 1, 1, 1, 1);

	private JTextArea chatArea;

	private String name;

	private Scanner in;
	private PrintWriter out;

	private String ip;
	
	private boolean joined = false;

	public void buttonClicked(JButton button) {
		if (button == enterChatButton) {
			name = nameField.getText();
			messageField.setVisible(true);
			chatArea = addTextArea("", 3, 1, 1, 1);
			chatArea.setEditable(false);
			nameField.setVisible(false);
			enterChatButton.setVisible(false);
			nameLabel.setVisible(false);
			ipField.setVisible(false);
			ipLabel.setVisible(false);
			
			ip = ipField.getText();
			if(ip.isEmpty())ip = "localhost";
			
			new Thread(connect).start();
		}
	}
	
	Runnable connect = new Runnable() {
		@Override
		public void run() {
			try {
				Socket socket = new Socket(ip, 59898);
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);

				while (in.hasNextLine()) {
					String line = in.nextLine();
					chatArea.append(line + '\n');
				}
			}catch (Exception e) {
				
			} finally {

			}
		}
		
	};

	public static void main(String[] args) throws IOException {
		ChatClient frm = new ChatClient();
	}

	public ChatClient() {
		setSize(400, 400);
		setTitle("Client");
		setVisible(true);
		
		messageField.setVisible(false);

		messageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(name + ": " + messageField.getText());
				messageField.setText("");
			}
		});
	}

}
