package Client;

public class saveText {
	int numberText = 0;
	final int max = 3000;
	int numberFilde = 0;
	private String user;
	String text[] = new String[3000];
	String fileID[] = new String[max];
	
	
	
	public saveText(String name) {
		this.user = name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getNumberText() {
		return numberText;
	}
	public void setNumberText(int numberText) {
		this.numberText = numberText;
	}
	public String[] getText() {
		return text;
	}
	public void setText(String text) {
		this.text[numberText++] = text;
	}
	public void setTextFile(String text,String fileID)
	{
		this.text[numberText++] = text;
		this.fileID[numberFilde++] = fileID;
	}

}
