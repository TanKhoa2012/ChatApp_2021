package Server;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;



public class Server  {
	
	ServerSocket serverSocket;
	Socket socket;
static	ArrayList<Handler> user = new ArrayList<Handler>();
static int i=0;
static	DataInputStream Dis;
static	DataOutputStream Dos;
JFrame frame;
JButton button;

Timer timer;
	public Server() throws IOException {
               serverSocket = new ServerSocket(5000);
				
			    while(i==0)
			    {
					System.out.println("server is listening!!!");

					socket = serverSocket.accept();
					
					Dis = new DataInputStream(socket.getInputStream());
					Dos = new DataOutputStream(socket.getOutputStream());
					
					String login = Dis.readUTF();
					//dang nhap
					if(login.equals("login"))
					{
						String nameLogin = Dis.readUTF();
						String passLogin = Dis.readUTF();
						if(checkName(nameLogin)==true)
						{
					     if(!checkOnline(nameLogin))
					      {
								for(Handler handler: user)
								{
									if (handler.getNameLogin().equals(nameLogin)) {
										if(handler.getPassLogin().equals(passLogin))
										{
											System.out.println("1");
											
											Handler newHandler = handler;
											newHandler.setSocket(socket);
											newHandler.setCheckLogin(true);
											
											
											Thread th = new Thread(newHandler);
											th.start();
											
											Dos.writeUTF("Login successful!!!");
											Dos.flush();
											
											updateOnlineUsers();
									
										}
										else
										{
											Dos.writeUTF("Incorrect account information !!!");
											Dos.flush();
										}
										break;
									}
								}
							}else
							{
								Dos.writeUTF("Incorrect account information !!!");
								Dos.flush();
							}
					  }
					else
					 {
							Dos.writeUTF("Incorrect account information !!!");
							Dos.flush();
					 }
					}
					// dang kí
					else if(login.equals("sin up"))
					{
						String nameLogin = Dis.readUTF();
						String passLogin = Dis.readUTF();
						if(checkName(nameLogin)==false)
						{
							Handler handler = new Handler(socket, nameLogin, passLogin, true);
							
							user.add(handler);
							
							Dos.writeUTF("Sign up successful");
							Dos.flush();
							
							Thread th = new Thread(handler);
							th.start();
							updateOnlineUsers();
							
						}else
						{
							Dos.writeUTF("the name was used!!!");
							Dos.flush();
							
						}
					}
					// thoat
					else{
						Dos.writeUTF("This username is not exist");
						Dos.flush();
					}
			    }
			

	}

public boolean checkName(String name)
{
	for(Handler nameHandler : user )
	{
		if(nameHandler.getNameLogin().equals(name)== true) return true;
		
	}
	
	return false;
}
public boolean checkPass(String pass)
{
	for(Handler passHandler : user )
	{
		if(passHandler.getPassLogin().equals(pass)== true) return true;
		
	}
	
	return false;
}
public boolean checkOnline(String name)
{
	for(Handler h: user)
	{
		if(h.getNameLogin().equals(name))
		{
			if(h.isCheckLogin()==true) return true;
		}
	}
	return false;
}
public static void updateOnlineUsers() {
	String message = "";
	
	for (Handler client: user) {
		if (client.isCheckLogin() == true) {
			message += client.getNameLogin();
			message += ",";
		}
	}
	
	for (Handler client: user) {
		if (client.isCheckLogin() == true) {
			try {
				client.getDos().writeUTF("userOnline");
				client.getDos().writeUTF(message);
				client.getDos().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
class Handler implements Runnable {
		
	String nameLogin;
	String passLogin;
	Socket socket;
	boolean checkLogin;
	
	DataInputStream Dis;
	DataOutputStream Dos;
	
	
	
	
	
	

	public Handler(Socket socket, String nameLogin, String passLogin, boolean isLoggedIn) throws IOException {
		this.socket = socket;
		this.nameLogin = nameLogin;
		this.passLogin = passLogin;
		this.Dis = new DataInputStream(socket.getInputStream());
		this.Dos = new DataOutputStream(socket.getOutputStream());
		this.checkLogin = isLoggedIn;
		
	}
	public DataInputStream getDis() {
		return Dis;
	}
	public void setDis(DataInputStream dis) {
		Dis = dis;
	}
	public DataOutputStream getDos() {
		return Dos;
	}
	public void setDos(DataOutputStream dos) {
		Dos = dos;
	}	
	public boolean isCheckLogin() {
		return checkLogin;
	}
	public void setCheckLogin(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.Dis = new DataInputStream(socket.getInputStream());
			this.Dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void closeSocket() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String getNameLogin() {
		return nameLogin;
	}
	public void setNameLogin(String nameLogin) {
		this.nameLogin = nameLogin;
	}
	public String getPassLogin() {
		return passLogin;
	}
	public void setPassLogin(String passLogin) {
		this.passLogin = passLogin;
	}




	@Override
	public void run() {
		
		while(true)
		{
			try {
			String text = null;
			
				text = Dis.readUTF();
				if(text.equals("Log out"))
				{
					Dos.writeUTF("Safe to leave");
					Dos.flush();
					socket.close();
					
					this.checkLogin = false;
					
					Server.updateOnlineUsers();
					
					break;					
				}
				// start send text
				else if(text.equals("Text"))
				{
					String receiver = Dis.readUTF();
					String sender = Dis.readUTF();
					String content = Dis.readUTF();
					
					for(Handler handler : Server.user)
					{
						if(handler.getNameLogin().equals(receiver)==true)
						{
							handler.Dos.writeUTF("Text");
							handler.Dos.writeUTF(receiver);
							handler.Dos.writeUTF(sender);
							handler.Dos.writeUTF(content);
							handler.Dos.flush();
							break;
						}
						
						
					}
					
				}
				//start send icon
				
				else if(text.equals("icon"))
				{
					String receiver = Dis.readUTF();
					String icon  = Dis.readUTF();
					
					for(Handler handler : Server.user)
					{
						if(handler.getNameLogin().equals(receiver)==true)
						{
							handler.Dos.writeUTF("Text");
							handler.Dos.writeUTF(this.nameLogin);
							handler.Dos.writeUTF(icon);
							handler.Dos.flush();
							break;
						}
						
						
					}
					
				}else if(text.equals("file"))
				{
					String receiver = Dis.readUTF();
					String sender = Dis.readUTF();
					String nameFile;
					int nameLenght = Dis.readInt();
					byte[] nameBytes;
					int contentLenght;
					byte contentFile[];
					
					if(nameLenght >0)
					{
						nameBytes = new byte[nameLenght];
						Dis.readFully(nameBytes,0,nameBytes.length);
						
						nameFile = new String(nameBytes);
						
						System.out.println(nameFile);
						
						contentLenght = Dis.readInt();
						System.out.println(contentLenght);
						if(contentLenght>0)
						{
						    contentFile = new byte[contentLenght];
							
							Dis.readFully(contentFile,0, contentLenght);
							for(Handler handler : user)
							{
								if(handler.getNameLogin().equals(receiver)==true)
								{
									handler.Dos.writeUTF("file");
									handler.Dos.writeUTF(receiver);
									handler.Dos.writeUTF(sender);
									handler.Dos.writeInt(nameFile.length());
									handler.Dos.write(nameBytes);
									
									handler.Dos.writeInt(contentFile.length);
									handler.Dos.write(contentFile);
									break;
								}
								
								
							}
						}
					}
					
				}
					
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("1234124134");
			}
				
			
			
			
			
			
		}
		
		
	}
	
		
		
		
		
		
		
	}

}
