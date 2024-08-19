package Client;

public class myFile {
	private int id;
	private String name;
	private String fileExtention;
	private byte[] data;
	
	
	public myFile(int id, String name, String fileExtention, byte[] data) {
		super();
		this.id = id;
		this.name = name;
		this.fileExtention = fileExtention;
		this.data = data;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileExtention() {
		return fileExtention;
	}
	public void setFileExtention(String fileExtention) {
		this.fileExtention = fileExtention;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

}
