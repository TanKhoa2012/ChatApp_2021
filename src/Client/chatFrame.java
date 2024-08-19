package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;







public class chatFrame extends JFrame {
JLabel nameMain;
JLabel OnlineLB;	
JLabel nameLB;
JButton sendFileLB;
JButton sendTextLB;
JTextArea textSent;
JPanel panelShowTextReceiver;
JPanel panelShowTextSend;
JPanel panelShowText;
JPanel panelShowUser;
JScrollPane sPane;
JScrollPane SPaneT;

Thread receiver;


String nameReceiver = "Receiver: ";
int numberST = 0;
saveText panelT[] = new saveText[3000];


File file;
int fileId = 0;
ArrayList<myFile> myFiles = new ArrayList<>();
ArrayList<String> userOnline = new ArrayList<>();

String userName;
DataInputStream DIS;
DataOutputStream DOS;
    ImageIcon iconSenFile = new ImageIcon("D:/luutrujava/AppChat/src/Client/sendFile.png");
	ImageIcon iconMain = new ImageIcon("D:/luutrujava/AppChat/src/Client/rubi.png");
	public chatFrame(String userName1,DataInputStream dis,DataOutputStream dos) {
		
		this.userName = userName1;
		this.DIS = dis;
		this.DOS = dos;
		this.setTitle("Rubi");
		this.setIconImage(iconMain.getImage());
		this.setSize(715,445);
		this.getContentPane().setBackground(new Color(79,92,70));
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		receiver = new Thread(new receiver(DIS));
		receiver.start();
		
		// hien thi user
		
		
		panelShowUser = new JPanel();
		panelShowUser.setLayout(new BoxLayout(panelShowUser, BoxLayout.Y_AXIS));
		panelShowUser.setBackground(new Color(79,92,70));
		
		nameMain = new JLabel("User: "+userName);
		nameMain.setFont(new Font("Arial", Font.BOLD,18));
		nameMain.setBounds(500,5,140,50);
		nameMain.setBorder(new EmptyBorder(10,10,10,10));
		nameMain.setBorder(new LineBorder(Color.CYAN));
		nameMain.setForeground(Color.CYAN);
		
		OnlineLB = new JLabel("   User Online");
		OnlineLB.setFont(new Font("Arial", Font.BOLD,18));
		OnlineLB.setBounds(15,5,140,50);
		OnlineLB.setBorder(new EmptyBorder(10,10,10,10));
		OnlineLB.setBorder(new LineBorder(Color.CYAN));
		OnlineLB.setForeground(Color.CYAN);
		
		nameLB = new JLabel("Receiver: ");
		nameLB.setFont(new Font("Arial", Font.BOLD,18));
		nameLB.setBounds(180,5,200,50);
		nameLB.setBorder(new EmptyBorder(10,10,10,10));
		nameLB.setBorder(new LineBorder(Color.CYAN));
		nameLB.setForeground(Color.CYAN);
		
		//hien thi text
	    panelShowText = new JPanel();
	    panelShowText.setLayout(new BoxLayout(panelShowText, BoxLayout.Y_AXIS));
        panelShowText.setBackground(Color.black);
        
		SPaneT = new JScrollPane(panelShowText);
		SPaneT.setVerticalScrollBarPolicy(SPaneT.VERTICAL_SCROLLBAR_ALWAYS);
		SPaneT.setHorizontalScrollBarPolicy(SPaneT.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		SPaneT.setBounds(170,60,this.getWidth()-170,this.getHeight()-170);
  		
		//nhap tin nhan gui
		JPanel panelText = new JPanel();
		panelText.setLayout(new BoxLayout(panelText,BoxLayout.X_AXIS));
		panelText.setBounds(170,60+this.getHeight()-160,this.getWidth()-190,this.getHeight()-(100+this.getHeight()-160));
		panelText.setPreferredSize(new Dimension(40,50));
		panelText.setBackground(new Color(156,244,110));
		
		textSent = new JTextArea();
		textSent.setFont(new Font("Arial", Font.PLAIN, 20));
		JScrollPane sp = new JScrollPane(textSent);
		sp.setVerticalScrollBarPolicy(sp.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		sendTextLB = new JButton("Send");
		sendTextLB.setFont(new Font("Arial",Font.PLAIN,30));
		sendTextLB.setBackground(Color.LIGHT_GRAY);
		sendTextLB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textSent.getText();
				if(!text.matches("\s+"))
				{
					
					String receiver = nameLB.getText();
					if(!receiver.substring(receiver.indexOf(" ")+1).equals(""))
					{
						try {
							DOS.writeUTF("Text");
							DOS.writeUTF(receiver.substring(receiver.indexOf(" ")+1));
							DOS.writeUTF(userName);
							DOS.writeUTF(text);
							DOS.flush();
							
							showMess(receiver.substring(receiver.indexOf(" ")+1), userName, text, true,false);
							getSaveText(receiver.substring(receiver.indexOf(" ")+1)).setText("rt "+text);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    }else
				    {
				    	JOptionPane.showMessageDialog(null,"You have not selected sender!","warning",JOptionPane.WARNING_MESSAGE);
				    }
					textSent.setText("");
				}
				
			}
		});
		
		
		
		sendFileLB = new JButton("☍");
		sendFileLB.setFont(new Font("",Font.BOLD,30));
		sendFileLB.setBackground(Color.LIGHT_GRAY);
		sendFileLB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String receiver = nameLB.getText();
				
					JFileChooser fChoose = new JFileChooser();
					fChoose.setDialogTitle("Choose File");
					//neu no chon file roi
						if(fChoose.showOpenDialog(null)== JFileChooser.APPROVE_OPTION)
						{
							try {
								file = fChoose.getSelectedFile();
								System.out.println(fChoose.getSelectedFile().getAbsolutePath());
								if(file!=null)
								{
								 FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
								
								 String nameFile =file.getName();
								
								 byte[]  fileNamebytes = nameFile.getBytes();
								
								
								 byte[] fileContentBytes = new byte[(int)file.length()];
								
								 inputStream.read(fileContentBytes);
								 String receiver1 = nameLB.getText();
								 if(!receiver1.substring(receiver1.indexOf(" ")+1).equals(""))
									{
									 DOS.writeUTF("file");
							         DOS.writeUTF(receiver.substring(receiver.indexOf(" ")+1));
							         DOS.writeUTF(userName);
									 DOS.writeInt(nameFile.length());
									 DOS.write(fileNamebytes);
									
									 DOS.writeInt(fileContentBytes.length);
									 DOS.write(fileContentBytes);
									 
									 
									 showMess(receiver.substring(receiver.indexOf(" ")+1), userName, nameFile, true,true);
									 
									 getSaveText(receiver.substring(receiver.indexOf(" ")+1)).setTextFile("rf "+nameFile,String.valueOf(fileId));
									 myFiles.add(new myFile(fileId, nameFile, getFileCheck(nameFile), fileContentBytes));
									 fileId++;
						         
									}else
									{
										JOptionPane.showMessageDialog(null,"You have not selected sender!","warning",JOptionPane.WARNING_MESSAGE);
										
								    }
								
								
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
			
				
				
			    }
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				try {
					dos.writeUTF("Log out");
					dos.flush();
					
					try {
						receiver.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					if (dos != null) {
						dos.close();
					}
					if (dis != null) {
						dis.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panelText.add(sendFileLB);
		panelText.add(sp);
		panelText.add(sendTextLB);
		
		this.add(panelText);
		this.add(nameLB);
		this.add(OnlineLB);
		this.add(nameMain);
		this.add(SPaneT);
		this.setVisible(true);
	}
public void showUser()
{
	
	panelShowUser.removeAll();
	if(userOnline.size()>0)
	{
		System.out.println("size:"+userOnline.size());
		for(String user : userOnline) System.out.println(user);
		System.out.println("--------------");
		for(String u:userOnline)
		{
			if(u.length()<19) 
			{
				for(int i=u.length();i<=19;i++) u+=" ";
			}
			JLabel nameFileLb = new JLabel(u);
		    nameFileLb.setFont(new Font("Arial",Font.PLAIN,15));
		    nameFileLb.setBorder(new EmptyBorder(10,0,10,0));
		    nameFileLb.setAlignmentX(Component.CENTER_ALIGNMENT);
		    
		    JPanel panelUser = new JPanel();
		    panelUser.setBorder(new EmptyBorder(10, 0, 10, 0));
		    panelUser.setBorder(new LineBorder(Color.BLACK));
		    panelUser.setName(u.trim());
		    panelUser.addMouseListener(getClickPanelName());
		    panelUser.setLayout(new BoxLayout(panelUser, BoxLayout.Y_AXIS));
		    panelUser.add(nameFileLb);
		    panelShowUser.add(panelUser);
		    panelShowUser.validate();
		    
		}
		if(sPane==null)
		{
		    sPane = new JScrollPane(panelShowUser);
		 	sPane.setVerticalScrollBarPolicy(sPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		 	sPane.setBounds(0,60,170,this.getHeight()-90);
		 	this.add(sPane);
		}else {
			sPane.removeAll();
			sPane.add(panelShowUser);
		}
		    
		    sPane.validate();
		    sPane.repaint();
		    this.validate();
		    this.repaint();
		
	}else
	{
		if(sPane==null)
		{
		    sPane = new JScrollPane(panelShowUser);
		 	sPane.setVerticalScrollBarPolicy(sPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		 	sPane.setBounds(0,60,170,this.getHeight()-90);
		 	this.add(sPane);
		}else {
			sPane.removeAll();
			sPane.add(panelShowUser);
		}
	}
	sPane.validate();
	sPane.repaint();
	this.validate();
	this.repaint();
	
}
public MouseListener getClickPanelName()
{
	return new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			JPanel panel = (JPanel) e.getSource();
			
			nameLB.setText(nameReceiver+panel.getName());
			nameLB.validate();
			
            String user = panel.getName();
            boolean bl =true;
            for(int i=0;panelT[i]!=null;i++)
            {
            	if(user.equals(panelT[i].getUser())) 
            	{
            		if(panelT[i].getText()[0]==null) bl = false;
            	}
            }
			
		 if(bl==true)
		 {
			 panelShowText.removeAll();
			 for(int j=0; panelT[j]!=null;j++)
			 {
						if(panelT[j].getUser().equals(panel.getName()))
						{
							int numberFile =0;
								for(int i=0;panelT[j].text[i]!=null;i++)
								{
									
									String check = panelT[j].text[i].substring(0,panelT[j].text[i].indexOf(" "));
									String text =  panelT[j].text[i].substring(panelT[j].text[i].indexOf(" ")+1);
									System.out.println(check+" "+text);
									if(check.equals("rf") || check.equals("lf")) showTextUser(text,String.valueOf(numberFile++), check);
									else showTextUser(text,null, check);
									
								}
								break;
						}
						
					
			 }
			 panelShowText.validate();
			 SPaneT.validate();
			 SPaneT.repaint();
		 }else
		 {
			 panelShowText.removeAll();
			 panelShowText.validate();
			 SPaneT.validate();
			 SPaneT.repaint();
		 }
			
			
			
			
		}
			
		
	};
			

}
public void showTextUser(String text,String fileID,String check)
{
	JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.black);
    
    JLabel nameFileLb = new JLabel(text);
    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
    nameFileLb.setBorder(new EmptyBorder(10,10,10,40));
    nameFileLb.setBorder(new LineBorder(Color.CYAN));
    nameFileLb.setForeground(Color.white);
    if(check.equals("lf") || check.equals("rf"))
    {
        if(check.equals("lf")) 
    	{
    	  panel.setName(fileID);
    	  panel.setBorder(new EmptyBorder(10,10,10,10));
	      panel.addMouseListener(getMouseListener());
	      nameFileLb.setBorder(new LineBorder(Color.red));
	      panel.add(nameFileLb);
    	}
    else if(check.equals("rf"))
    	{
	      int boderLeft = 450;
	      panel.setName(fileID);
	      panel.setBorder(new EmptyBorder(10,boderLeft-(nameFileLb.getPreferredSize().width -61),10,10));
	      panel.addMouseListener(getMouseListener());
	      nameFileLb.setBorder(new LineBorder(Color.red));
	      panel.add(nameFileLb);
    	}
    }else {
    	    if(check.equals("lt"))
    	    	{ 
    	    	    panel.setBorder(new EmptyBorder(10,10,10,10));
    	    	    JPanel panelCut = new JPanel();
    			    panelCut.setBorder(new EmptyBorder(10,10,10,40));
    			    panelCut.setLayout(new BoxLayout(panelCut, BoxLayout.Y_AXIS));
    			    panelCut.setBorder(new LineBorder(Color.CYAN));
    			    panelCut.setBackground(Color.black);
    			    
    			    double min = (int) Math.ceil((nameFileLb.getPreferredSize().getWidth()/350));
    			    String content[] = cutText(text, min);
    			    for(int i=0;content[i]!=null;i++)
    		    	{
    			    	System.out.println(content[i]);
    		    		JLabel nameFileCut = new JLabel(content[i]);
    			    	nameFileCut.setFont(new Font("Arial",Font.PLAIN,20));
    				    nameFileCut.setForeground(Color.white);
    				    panelCut.add(nameFileCut);
    		    	}
    			    panel.add(panelCut);
    			    
    	    	}
    	    else
    	    {
    	        int boderLeft = 450;
    	    	
    	    	 JPanel panelCut = new JPanel();
 			    panelCut.setBorder(new EmptyBorder(10,10,10,40));
 			    panelCut.setLayout(new BoxLayout(panelCut, BoxLayout.Y_AXIS));
 			    panelCut.setBorder(new LineBorder(Color.CYAN));
 			    panelCut.setBackground(Color.black);
 			    
 			    double min = (int) Math.ceil((nameFileLb.getPreferredSize().getWidth()/350));
 			    String content[] = cutText(text, min);
 			    for(int i=0;content[i]!=null;i++)
 		    	{
 			    	System.out.println(content[i]);
 		    		JLabel nameFileCut = new JLabel(content[i]);
 			    	nameFileCut.setFont(new Font("Arial",Font.PLAIN,20));
 				    nameFileCut.setForeground(Color.white);
 				    panelCut.add(nameFileCut);
 		    	}
 			    panel.setBorder(new EmptyBorder(10,boderLeft-(panelCut.getPreferredSize().width -61),10,10));
 			    panel.add(panelCut);
    	    }
    }

   
   
    
    
    
    String name =nameLB.getText().substring(nameLB.getText().indexOf(" ")+1);
    panelShowText.add(panel);
    
  
}
public String[] cutText(String content,double min)
{
	String text[] = new String[3000];
	    int numberText = 0;
	    int numberChar = (int) Math.ceil(content.length()/min);
	    
	    System.out.println(numberChar);
	    int n=0;
	    int m=numberChar;
	    boolean bl =true;
	    
	    while(true)
 	    {
 		try {
 		if(m>content.length())m=content.length();
 		String name = content.substring(0,m);
 		int c=name.lastIndexOf(" ");
 		if(c<m && m!=content.length() && c!=-1)
 		{
 			m=c;
 			bl=false;
 		}
 		text[numberText++] = content.substring(n,m);
 		if(content.length()==m)break;
 		System.out.println(content);
 		if(bl==false)
 		{
 		 n=m+1;
 		 m+=numberChar+1;
 		 bl=true;
 		}else
 		{
 			n=m;
 			m+=numberChar;
 		}
 		} catch (Exception e) {
 			// TODO: handle exception
 		}
 	}
	    return text;
	
}
public void showMess(String receiver,String sender,String nameFile,boolean yourSend, boolean file)
{
	if(yourSend == false)
	{
		if(sender.equals(nameLB.getText().substring(nameLB.getText().lastIndexOf(" ")+1)))
		  {
			JPanel panel =new JPanel();
		    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		    panel.setBorder(new EmptyBorder(10,10,10,10));
		    panel.setBackground(Color.black);
		    
		    JLabel nameFileLb = new JLabel(nameFile);
		    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
		    nameFileLb.setBorder(new EmptyBorder(10,10,10,40));
		    nameFileLb.setBorder(new LineBorder(Color.CYAN));
		    nameFileLb.setForeground(Color.white);
		    

		    JPanel panelCut = new JPanel();
		    panelCut.setBorder(new EmptyBorder(10,10,10,40));
		    panelCut.setLayout(new BoxLayout(panelCut, BoxLayout.Y_AXIS));
		    panelCut.setBorder(new LineBorder(Color.CYAN));
		    panelCut.setBackground(Color.black);
		    
		    double min = (int) Math.ceil((nameFileLb.getPreferredSize().getWidth()/350));
		    String content[] = cutText(nameFile, min);
		    for(int i=0;content[i]!=null;i++)
	    	{
		    	System.out.println(content[i]);
	    		JLabel nameFileCut = new JLabel(content[i]);
		    	nameFileCut.setFont(new Font("Arial",Font.PLAIN,20));
			    nameFileCut.setForeground(Color.white);
			    panelCut.add(nameFileCut);
	    		
	    	}
		    if(file==true)
		    {
		      panel.setName(String.valueOf(fileId));
		      panelCut.setBorder(new LineBorder(Color.red));
		      panel.addMouseListener(getMouseListener());
		    }
		    panel.add(panelCut);
		    String name =nameLB.getText().substring(nameLB.getText().lastIndexOf(" ")+1);
		    panelShowText.add(panel);
		    panelShowText.validate();
		    
		    if(sender.equals(name))
		    { 
		    	if(SPaneT == null)
		        {     
		    			SPaneT = new JScrollPane(panelShowText);
		    			SPaneT.setVerticalScrollBarPolicy(SPaneT.VERTICAL_SCROLLBAR_ALWAYS);
		    			SPaneT.setAutoscrolls(true);
		    			SPaneT.setBounds(170,60,this.getWidth()-170,this.getHeight()-170);
		    			this.add(SPaneT);
		        }
		    	SPaneT.repaint();
		    	SPaneT.validate();
		    	this.repaint();
		    }
		  }
	}else {
		if(receiver.equals(nameLB.getText().substring(nameLB.getText().lastIndexOf(" ")+1)))
		  {      
			    JLabel nameFileLb = new JLabel(nameFile);
			    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
			    nameFileLb.setBorder(new EmptyBorder(10,40,10,10));
			    nameFileLb.setBorder(new LineBorder(Color.CYAN));
			    nameFileLb.setForeground(Color.WHITE);
			 
			    int boderLeft = 450;
			    System.out.println(boderLeft);
			   
			    JPanel panel =new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    panel.setBackground(Color.black);
			    
			    JPanel panelCut = new JPanel();
			    panelCut.setBorder(new EmptyBorder(10,10,10,40));
			    panelCut.setLayout(new BoxLayout(panelCut, BoxLayout.Y_AXIS));
			    panelCut.setBorder(new LineBorder(Color.CYAN));
			    panelCut.setBackground(Color.black);
			
			    double min = (int) Math.ceil((nameFileLb.getPreferredSize().getWidth()/350));
			    String content[] = cutText(nameFile, min);
			    for(int i=0;content[i]!=null;i++)
		    	{
			    	System.out.println(content[i]);
		    		JLabel nameFileCut = new JLabel(content[i]);
			    	nameFileCut.setFont(new Font("Arial",Font.PLAIN,20));
				    nameFileCut.setForeground(Color.white);
				    panelCut.add(nameFileCut);
		    		
		    	}
		        
			    panel.setBorder(new EmptyBorder(10,(int) (boderLeft-(panelCut.getPreferredSize().width -61)),10,10));
			    System.out.println(panelCut.getPreferredSize().getWidth()+" "+panelCut.getPreferredSize().height);
			    if(file==true)
			    {
			      panel.setName(String.valueOf(fileId));
			      panelCut.setBorder(new LineBorder(Color.red));
			      panel.addMouseListener(getMouseListener());
			    }
			    panel.add(panelCut);
			    String name =nameLB.getText().substring(nameLB.getText().lastIndexOf(" ")+1);
			    panelShowText.add(panel);
			    
			    if(receiver.equals(name))
			    { 
			    	if(SPaneT == null)
			        {     
			    			SPaneT = new JScrollPane(panelShowText);
			    			SPaneT.setVerticalScrollBarPolicy(SPaneT.VERTICAL_SCROLLBAR_ALWAYS);
			    			SPaneT.setAutoscrolls(true);
			    			SPaneT.setBounds(170,60,this.getWidth()-170,this.getHeight()-170);
			    			this.add(SPaneT);
			        }
			    	panelShowText.validate();
			    	panelShowText.repaint();
			    	SPaneT.repaint();
			    	SPaneT.validate();
			    	this.repaint();
			    }
			  }
	}
}
/*
public void newMess(String receiver,String sender,String nameFile,boolean yourSend )
{
	panelShowText.removeAll();
	if(yourSend == false)
	{	
	  if(receiver.equals(userName))
	  {
		  if(sender.equals(nameLB.getText().substring(nameLB.getText().lastIndexOf(" ")+1)))
		  {
			     for(savePanel p: panels)
					if(p.getNameUser().equals(sender))
					{
						    if(p.getPanel()==null) p.setPanel(panelShowText);
						    else {
						    	panelShowText = p.getPanel();
						    }
						    
						     
						    JPanel panel =new JPanel();
						    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						    panel.setName(String.valueOf(fileId));
						    panel.setBorder(new EmptyBorder(10,10,10,200));
						    panel.setBackground(Color.black);
						    
						    JLabel nameFileLb = new JLabel(nameFile);
						    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
						    nameFileLb.setBorder(new EmptyBorder(10,10,10,40));
						    nameFileLb.setBorder(new LineBorder(Color.CYAN));
						    nameFileLb.setForeground(Color.white);
						    
						    panel.addMouseListener(getMouseListener());
						    panel.add(nameFileLb);
						    String name =nameLB.getText().substring(nameLB.getText().indexOf(" ")+1);
						    panelShowText.add(panel);
						    panelShowText.validate();
						    p.setPanel(panelShowText);
						    if(sender.equals(name))
						    { 
						    	if(SPaneT !=null)
						    	{
						    		SPaneT.removeAll();
						    		SPaneT.add(p.getPanel());
						    	}else
						    	{
						    		SPaneT = new JScrollPane(p.getPanel());
						    		SPaneT.setVerticalScrollBarPolicy(SPaneT.VERTICAL_SCROLLBAR_AS_NEEDED);
						    		SPaneT.setBounds(170,60,this.getWidth()-170,this.getHeight()-160);
						    	}
						    	
						    }
						    SPaneT.validate();
						    break;
					}
				  }
			  }else {
				  for(savePanel p : panels)
					{
						if(p.getNameUser().equals(sender))
						{
						   if(p.getPanel()==null) p.setPanel(panelShowText);
						   else {
							    	panelShowText = p.getPanel();
							    }
							
							JPanel panel =new JPanel();
						    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						    panel.setName(String.valueOf(fileId));
						    panel.setBorder(new EmptyBorder(10,10,10,200));
						    panel.setBackground(Color.black);
						    
						    JLabel nameFileLb = new JLabel(nameFile);
						    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
						    nameFileLb.setBorder(new EmptyBorder(10,10,10,40));
						    nameFileLb.setBorder(new LineBorder(Color.CYAN));
						    nameFileLb.setForeground(Color.white);
						    
						    panel.addMouseListener(getMouseListener());
						    panel.add(nameFileLb);
						    String name =nameLB.getText().substring(nameLB.getText().indexOf(" ")+1);
						    panelShowText.add(panel);
						    p.setPanel(panelShowText);
						    panelShowText.removeAll();
						    break;
						}
					}
				  
			  }
	  
	}else {
		  if(sender.equals(userName))
		  {
		    if(receiver.equals(nameLB.getText().substring(nameLB.getText().lastIndexOf(" ")+1)))
			  {
		    	for(savePanel p : panels)
				{
					if(p.getNameUser().equals(receiver))
					{
						    if(p.getPanel()==null) p.setPanel(panelShowText);
						    else {
						    	panelShowText = p.getPanel();
						    }
						    
						    int kiTu = nameFile.length();
						    int boderLeft = 450;
						    if(kiTu>8) boderLeft -= (kiTu-8)*8;
						    JPanel panel =new JPanel();
						    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						    panel.setBorder(new EmptyBorder(10,boderLeft,10,10));
						    panel.setName(String.valueOf(fileId));
						    panel.setBackground(Color.black);
						
						    JLabel nameFileLb = new JLabel(nameFile);
						    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
						    nameFileLb.setBorder(new EmptyBorder(10,40,10,10));
						    nameFileLb.setBorder(new LineBorder(Color.CYAN));
						    nameFileLb.setForeground(Color.WHITE);
						    
						    panel.addMouseListener(getMouseListener());
						    panel.add(nameFileLb);
						    String name =nameLB.getText().substring(nameLB.getText().indexOf(" ")+1);
						    panelShowText.add(panel);
						    panelShowText.validate();
						    p.setPanel(panelShowText);
						    if(receiver.equals(name))
						    { 
						    	if(SPaneT !=null)
						    	{
						    		SPaneT.removeAll();
						    		SPaneT.add(p.getPanel());
						    	}else
						    	{
						    		SPaneT = new JScrollPane(p.getPanel());
						    		SPaneT.setVerticalScrollBarPolicy(SPaneT.VERTICAL_SCROLLBAR_AS_NEEDED);
						    		SPaneT.setBounds(170,60,this.getWidth()-170,this.getHeight()-160);
						    	}
						    	
						    }
						    SPaneT.validate();
						    break;
					}
				  }
			  }else {
				  for(savePanel p : panels)
					{
						if(p.getNameUser().equals(receiver))
						{
							    if(p.getPanel()==null) p.setPanel(panelShowText);
							    else {
							    	panelShowText = p.getPanel();
							    }
							    
							    int kiTu = nameFile.length();
							    int boderLeft = 450;
							    if(kiTu>8) boderLeft -= (kiTu-8)*8;
							    JPanel panel =new JPanel();
							    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
							    panel.setBorder(new EmptyBorder(10,boderLeft,10,10));
							    panel.setName(String.valueOf(fileId));
							    panel.setBackground(Color.black);
							
							    JLabel nameFileLb = new JLabel(nameFile);
							    nameFileLb.setFont(new Font("Arial",Font.PLAIN,20));
							    nameFileLb.setBorder(new EmptyBorder(10,40,10,10));
							    nameFileLb.setBorder(new LineBorder(Color.CYAN));
							    nameFileLb.setForeground(Color.WHITE);
							    
							    panel.addMouseListener(getMouseListener());
							    panel.add(nameFileLb);
							    String name =nameLB.getText().substring(nameLB.getText().indexOf(" ")+1);
							    panelShowText.add(panel);
							    p.setPanel(panelShowText); 
							    break;
						}
					}
				  
			  }
		  }
		
		
	}
}
*/
public MouseListener getMouseListener() {
	return new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			JPanel panel = (JPanel) e.getSource();
			
			int fileID = Integer.parseInt(panel.getName());
			
			System.out.println(fileID);
			for(myFile file: myFiles)
			{
				if(file.getId()==fileID)
				{
				  createFrame(file.getName(),file.getData(),file.getFileExtention());
				}
			}
			
		}
	};
}
public void  createFrame(String fileName,byte[] fileConent,String fileExtention)
{
	
			JFileChooser chooser = new JFileChooser();
			File fileDowload = null ;
			if(chooser.showSaveDialog(null)== JFileChooser.APPROVE_OPTION)
			{
			  fileDowload = new File(chooser.getSelectedFile().getAbsolutePath()+"."+getFileCheck(fileName));
			}
			
			
			if(chooser.getSelectedFile()!=null)
			{
				FileOutputStream filOutputStream;
			try {
				filOutputStream = new FileOutputStream(fileDowload);
				filOutputStream.write(fileConent);
			    filOutputStream.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}

}

public String getFileCheck(String fileName)
{
	int i = fileName.lastIndexOf(".");
	if(i>0)
	{
		return fileName.substring( i+1);
	}else
		return fileName.substring( i+1);
}
public saveText getSaveText(String name)
{
	for(int i=0; panelT[i]!=null;i++)
	{
		if(panelT[i].getUser().equals(name)) return panelT[i];
	}
	return null;
}
class receiver implements Runnable
{
	private DataInputStream dis;
	public receiver(DataInputStream dis) {
		this.dis = dis;
	}

	@Override
	public void run() {
		try {
		while(true)	
		{
			String text = dis.readUTF();
			System.out.println(text);
			
			if(text.equals("file"))
			{
				String receiver = dis.readUTF();
				String sender = dis.readUTF();
				int nameLenght = dis.readInt();
				
				if(nameLenght >0)
				{
					byte[] nameBytes = new byte[nameLenght];
					dis.readFully(nameBytes,0,nameBytes.length);
					
					String nameFile = new String(nameBytes);
					
					System.out.println(nameFile);
					
					int contentLenght = dis.readInt();
					System.out.println(contentLenght);
					if(contentLenght>0)
					{
						byte contentFile[] = new byte[contentLenght];
						
						dis.readFully(contentFile,0, contentLenght);
						
					    showMess(receiver,sender,nameFile,false,true);
					    
					    getSaveText(sender).setTextFile("lf "+nameFile,String.valueOf(fileId));	
					    myFiles.add(new myFile(fileId, nameFile, getFileCheck(nameFile), contentFile));
					    fileId++;
					}
				}
			}
			else if(text.equals("userOnline"))
			{
				System.out.println("da nhan");
				String users[] = dis.readUTF().split(",");
				//removeAll
				userOnline.clear();
				
				
				//update
				
				for(String user : users)
				{
					System.out.println(user);
					if(user.equals(userName)==false)
					{   
						if(getSaveText(user)==null)
						{
							panelT[numberST++] = new saveText(user);
						}
						userOnline.add(user);
					}
				}
				showUser();
			}else if(text.equals("Text"))
			{
				
				String receiver = dis.readUTF();
				String sender = dis.readUTF();
			    String textContent = dis.readUTF();
				
				
				showMess(receiver, sender, textContent, false,false);
				getSaveText(sender).setText("lt "+textContent);
				
			}else if(text.equals("Safe to leave"))
			{
				
				break;
			}
		}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	


	
	
}
	
	
	
}
