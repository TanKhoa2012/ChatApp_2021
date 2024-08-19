package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

public class loginChat implements ActionListener {
	JFrame frame;
	JLabel label;
	JLabel lbName;
	JLabel lbPass;
	JTextField textName;
	JPasswordField textPass;
	
	JButton btLogIn;
	JButton btSignUp;
	
	ImageIcon iconMain = new ImageIcon("D:/luutrujava/AppChat/src/Client/sceen.png");
	String host = "Localhost";
	int port = 5000;
	
	Socket socket;
	DataInputStream Dis;
	DataOutputStream Dos;
	Timer timer;
	public static void main(String[] args) {
		new loginChat();
	}
	
	
	
	public loginChat() {
		frame = new JFrame();
		frame.setIconImage(iconMain.getImage());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setTitle("Login Chat");
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setSize(500,350);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(0, 255, 255));
		
		
		
		
		label = new JLabel("LOGIN");
		label.setFont(new Font("abc", Font.HANGING_BASELINE, 40));
		label.setBackground(new Color(245, 245, 220));
	    label.setOpaque(true);
		label.setBounds(0, 0,frame.getWidth(),frame.getWidth()-((frame.getWidth()*85)/100));
	    label.setHorizontalAlignment(label.CENTER);
	    
	    lbName = new JLabel("UserName:");
	    lbName.setFont(new Font("abc", Font.BOLD, 15));
	    lbName.setBounds(frame.getWidth()-((frame.getWidth()*85)/100), frame.getWidth()-((frame.getWidth()*75)/100),frame.getWidth()-((frame.getWidth()*80)/100),frame.getWidth()-((frame.getWidth()*95)/100));
	    lbName.setHorizontalAlignment(lbName.CENTER);
	    
	    lbPass = new JLabel("UserPass:");
	    lbPass.setFont(new Font("abc", Font.BOLD, 15));
	    lbPass.setBounds(frame.getWidth()-((frame.getWidth()*85)/100), frame.getWidth()-((frame.getWidth()*60)/100),frame.getWidth()-((frame.getWidth()*80)/100),frame.getWidth()-((frame.getWidth()*95)/100));
	    lbPass.setHorizontalAlignment(lbPass.CENTER);
	    
	    textName = new JTextField();
	    textName.setFont(new Font("abc", Font.BOLD, 15));
	    textName.setBounds(frame.getWidth()-((frame.getWidth()*65)/100), frame.getWidth()-((frame.getWidth()*75)/100),frame.getWidth()-((frame.getWidth()*70)/100),frame.getWidth()-((frame.getWidth()*95)/100));
	    textName.setText("");
	    
	    textPass = new JPasswordField();
	    textPass.setFont(new Font("abc", Font.BOLD, 15));
	    textPass.setBounds(frame.getWidth()-((frame.getWidth()*65)/100), frame.getWidth()-((frame.getWidth()*60)/100),frame.getWidth()-((frame.getWidth()*70)/100),frame.getWidth()-((frame.getWidth()*95)/100));
	    textPass.setText("");
	    
	    btLogIn = new JButton("Login");
	    btLogIn.setFont(new Font(null, Font.BOLD, 15));
	    btLogIn.setBackground(Color.lightGray);
  	    btLogIn.setBorder(BorderFactory.createEtchedBorder());
  	    btLogIn.setBounds(frame.getWidth()-((frame.getWidth()*70)/100), frame.getWidth()-((frame.getWidth()*50)/100),frame.getWidth()-((frame.getWidth()*85)/100),frame.getWidth()-((frame.getWidth()*95)/100));
	    btLogIn.addActionListener(this);
	    btLogIn.setEnabled(false);
	    
  	    btSignUp = new JButton("Register");
	    btSignUp.setFont(new Font(null, Font.BOLD, 15));
	    btSignUp.setBackground(Color.lightGray);
	    btSignUp.setBorder(BorderFactory.createEtchedBorder());
	    btSignUp.setBounds(frame.getWidth()-((frame.getWidth()*45)/100), frame.getWidth()-((frame.getWidth()*50)/100),frame.getWidth()-((frame.getWidth()*85)/100),frame.getWidth()-((frame.getWidth()*95)/100));
	    btSignUp.addActionListener(this);
	    btSignUp.setEnabled(false);
	    
	    frame.add(btSignUp);
	    frame.add(btLogIn);
	    frame.add(textPass);
	    frame.add(textName);
	    frame.add(lbPass);
	    frame.add(lbName);
		frame.add(label);
		frame.setVisible(true);
		
		
		timer = new Timer(1, this);
		timer.start();
		
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(textName.getText().equals("")==false)
		{
			String name = textName.getText();
			String pass = String.copyValueOf(textPass.getPassword());
			
			if(!pass.equals(""))
			{
				btLogIn.setEnabled(true);
				btSignUp.setEnabled(true);
				if(e.getSource()==btLogIn)
				{
					try {
						String resutl = Login(name, pass);
						if(resutl.equals("Login successful!!!"))
						{
							System.out.print("sdfsdf");
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										chatFrame frame = new chatFrame(name, Dis, Dos);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							frame.setVisible(false);
						
							
						}else
						{
							textPass.setText("");
							textName.setText("");
							btLogIn.setEnabled(false);
							btSignUp.setEnabled(false);
							
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}else if(e.getSource().equals(btSignUp))
				{
					
					JPasswordField confirm = new JPasswordField();
				    int action = JOptionPane.showConfirmDialog(null, confirm,"Comfirm your password",JOptionPane.OK_CANCEL_OPTION);
					if(action == JOptionPane.OK_OPTION)
					{
						try {
							if(String.copyValueOf(confirm.getPassword()).equals(String.copyValueOf(textPass.getPassword())))
							{
								String result = SignUp(name, pass);
								if(result.equals("Sign up successful"))
								{
									EventQueue.invokeLater(new Runnable() {
										
										@Override
										public void run() {
											try {
												int confirm1 = JOptionPane.showConfirmDialog(null, "Sign up successful\nWelcome to MANGO CHAT", "Sign up successful", JOptionPane.DEFAULT_OPTION);
												
												chatFrame frame = new chatFrame(name, Dis, Dos);
												frame.setVisible(true);
												
											} catch (Exception e2) {
												System.out.println("no open !!");
											}
											
											
										}
									});
									frame.setVisible(false);
									
								}
								else
								{
									textPass.setText("");
									textName.setText("");
									btLogIn.setEnabled(false);
									btSignUp.setEnabled(false);
									
								}
								
							}else
							{
								textPass.setText("");
								textName.setText("");
								btLogIn.setEnabled(false);
								btSignUp.setEnabled(false);
								
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					
					}else
					{
						textPass.setText("");
						textName.setText("");
						btLogIn.setEnabled(false);
						btSignUp.setEnabled(false);
						
					}
					
				}
				
				
			}
		}
		
		
		
		
	}
public String Login(String userName,String userPass) throws IOException
{
	
	Connect();
	Dos.writeUTF("login");
	Dos.writeUTF(userName);
	Dos.writeUTF(userPass);
	Dos.flush();
	
	String SVSend = Dis.readUTF();
	
    return SVSend;

}
public String SignUp(String userName,String userPass) throws IOException
{
	Connect();
	Dos.writeUTF("sin up");
	Dos.writeUTF(userName);
	Dos.writeUTF(userPass);
	Dos.flush();
	
	String SVSend = Dis.readUTF();
	
    return SVSend;
	
}

	
	
	
	
	
	
public void Connect() {
		try {
			if (socket != null) {
				socket.close();
			}
			socket = new Socket(host, port);
			this.Dis = new DataInputStream(socket.getInputStream());
			this.Dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
}
	
	
	
	
	
	
	
	
	

}
