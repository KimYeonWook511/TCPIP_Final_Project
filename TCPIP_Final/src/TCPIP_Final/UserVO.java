package TCPIP_Final;

import java.io.PrintWriter;
import java.net.Socket;

public class UserVO {

	private String userId; // 유저 아이디
	private Socket userSocket; // 유저의 소켓
	private PrintWriter userWriter; // 유저의 PrintWriter 출력 스트림
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Socket getUserSocket() {
		return userSocket;
	}

	public void setUserSocket(Socket userSocket) {
		this.userSocket = userSocket;
	}

	public PrintWriter getUserWriter() {
		return userWriter;
	}

	public void setUserWriter(PrintWriter userWriter) {
		this.userWriter = userWriter;
	}
	
	public void close() {
		try {
			
			if (userSocket != null) {
				// Socket 종료
				userSocket.close();
				
			}
			if (userWriter != null) {
				// PrintWriter 종료
				userWriter.close();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UserVO() {
		// 기본 생성자
	}
	
	public UserVO(String userId) {
		// 생성자 오버라이딩
		this.userId = userId;
	}
}
