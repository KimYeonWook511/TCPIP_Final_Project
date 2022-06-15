package TCPIP_Final;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server { // 서버 클래스
	
	private static ArrayList<UserVO> userList = new ArrayList<UserVO>(); // 접속한 유저들 있는 리스트
	private static Semaphore sema = new Semaphore(1); // 세마포어이자, 이진 세마포어로 뮤텍스와 같은 기능 가능 | synchronized와 mutex의 차이는?
	
	public static void main(String[] args) {
	
		try {
			
			ServerSocket serverSocket = new ServerSocket(9190); // 서버 소켓 생성 (포트는 9190 고정)
			
			while(true) {
				System.out.println("현재 접속 대기 상태...");
				
				UserVO user = new UserVO(); // 접속한 유저
				
				// 연결 요청 온 소켓 수락		
				user.setUserSocket(serverSocket.accept());
				
				CreateThread userThread = new CreateThread(user, userList, sema);
				userThread.start();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean chkUserId(String userId) {
		// 현재 중복되는 아이디가 있는지 확인
		for (int i = 0; i < userList.size(); i++) {
			
			if (userList.get(i).getUserId().equals(userId)) {
				return true; // 사용 불가능
				
			}			
		}
		
		return false; // 사용 가능
	}
}
