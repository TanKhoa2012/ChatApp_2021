package Server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;



public class serverDisplay {
Server server ;
JFrame frame;
JLabel label;
JButton button;

int x = 500;
int y = 350;
Timer timer;
ImageIcon icon = new ImageIcon("D:/luutrujava/AppChat/src/Server/server.png");
	public serverDisplay() {
		frame = new JFrame("Server Mess");
		frame.setLayout(null);
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(x, y);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        button = new JButton("Open Server");
        button.setBounds((x-x/4)-x/2,y/2+y/10,x/2,y/10);
        button.setFont(new Font("abc",Font.BOLD , 15));
        button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						try {
							server  = new Server();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
			
				
			}
		});
    
        label = new JLabel("Setup Server");
        label.setFont(new Font("abc",Font.BOLD , 40));
        label.setBounds((x-x/4)-x/2,y/5,x/2,y/10);
        
      
        
        frame.add(label);
        frame.add(button);
        frame.setVisible(true);
       
        
        
	}

	public static void main(String[] args) {
		
			
		
					new serverDisplay();
					
				
					
					
			
	}
	
}
