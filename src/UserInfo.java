
public class UserInfo {
	private int roomNum;
	private String nickname;
	private int x;
	private int preroomNum;
	private int y;
	
	UserInfo(){
		this.roomNum = 0;
		this.nickname = "noname";
		this.preroomNum = 0;
		this.x = 0;
		this.y = 0;
	}
	
	UserInfo(int roomNum, String nickname, int x, int y){
		this.roomNum = roomNum;
		this.nickname = nickname;
		this.preroomNum = 0;
		this.x = x;
		this.y = y;
	}
	
	public void setRoom(int roomNum){
		this.roomNum = roomNum;
	}
	
	public void setNickname(String nickname){
		this.nickname = nickname;
	}
	
	public void setX(int x){
		System.out.println("befor x : " + this.x);
		this.x = x;
		System.out.println("after x : " + this.x);
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getRoomNum() {
		return roomNum;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public void setPreroomNum(int preroomNum) {
		this.preroomNum = preroomNum;
	}
	public int getPreroomNum() {
		return preroomNum;
	}
	
	
}
