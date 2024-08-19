package Client;

import java.util.ArrayList;

import javax.swing.JPanel;
class savePanel{
	private String nameUser;
	private JPanel panel;
	
	public savePanel(String nameUser,JPanel panel) {
		this.nameUser =nameUser;
		this.panel = panel;
	}
	public String getNameUser() {
		return nameUser;
	}
	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}
	public JPanel getPanel() {
		return this.panel;
	}
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	
}
 


