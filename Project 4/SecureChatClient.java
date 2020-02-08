import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class SecureChatClient extends JFrame implements Runnable, ActionListener {

	public static final int PORT = 8765;

	ObjectInputStream  	myReader;
	ObjectOutputStream 	myWriter;
	JTextArea 			outputArea;
	JLabel 				prompt;
	JTextField 			inputField;
	String 				myName, serverName;
	Socket 				connection;
	BigInteger 			pubKey, pubMod, cipherKey;
	String 				cipherType;
	SymCipher 			cipher;


	public SecureChatClient() {

		try {

			myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
			serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
			connection = new Socket(InetAddress.getByName(serverName), PORT);
			// Connect to server with new Socket

			myWriter = new ObjectOutputStream(connection.getOutputStream());
			myWriter.flush();

			myReader = new ObjectInputStream(connection.getInputStream());

			pubKey = (BigInteger) myReader.readObject();
			pubMod = (BigInteger) myReader.readObject();
			cipherType = (String) myReader.readObject();

			System.err.println("E: " + pubKey);
			System.err.println("N: " + pubMod);
			System.err.println("Cipher: " + cipherType);

			if (cipherType.equals("Add")) { cipher = new Add128();     }
			else                          {	cipher = new Substitute(); }

			cipherKey = new BigInteger(1, cipher.getKey());

			myWriter.writeObject(cipherKey.modPow(pubKey, pubMod)); // RSA returns BigInteger
			myWriter.flush();

			myWriter.writeObject(cipher.encode(myName));
			myWriter.flush();

			Box b = Box.createHorizontalBox();  // Set up graphical environment for
			outputArea = new JTextArea(8, 30);  // user
			outputArea.setEditable(false);
			b.add(new JScrollPane(outputArea));

			outputArea.append("Welcome to the Chat Group, " + myName + "\n");

			inputField = new JTextField("");  // This is where user will type input
			inputField.addActionListener(this);

			prompt = new JLabel("Type your messages below:");
			Container c = getContentPane();

			c.add(b, BorderLayout.NORTH);
			c.add(prompt, BorderLayout.CENTER);
			c.add(inputField, BorderLayout.SOUTH);

			new Thread(this).start();

			addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					try { myWriter.writeObject("CLIENT CLOSING"); }
					catch (IOException e1) { e1.printStackTrace(); }
					System.exit(0);
				}
			});

			setSize(500, 200);
			setVisible(true);

		} catch (Exception e) {
			System.out.println("Problem starting client!\n");
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String msg = myName + ": " + e.getActionCommand(); // Get input value
		inputField.setText("");
		try {
			myWriter.writeObject(cipher.encode(msg));
		} catch (IOException e1) {
			System.out.println("Writing failed");
			e1.printStackTrace();
		}   // Add name and send it

	}

	@Override
	public void run() {

		while (true) {

			try {

				outputArea.append(
						new String(
								cipher.decode(
										(byte[]) myReader.readObject()).getBytes()) + "\n");

			} catch (Exception e) {
				System.out.println(e +  ", closing client!");
				e.printStackTrace();
				break;
			}
		}
		System.exit(0);

	}

	public static void main(String[] args) {
		SecureChatClient JR = new SecureChatClient();
		JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

}
