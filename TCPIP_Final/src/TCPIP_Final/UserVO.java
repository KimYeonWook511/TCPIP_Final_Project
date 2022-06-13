package TCPIP_Final;

import java.io.PrintWriter;
import java.net.Socket;

public class UserVO {

	private String userId;
	private Socket userSocket;
	private PrintWriter userWriter;
	
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
		// TODO Auto-generated constructor stub
	}
	public UserVO(String userId) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
	}
}
