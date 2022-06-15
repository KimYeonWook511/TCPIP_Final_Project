package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server { // 서버 클래스
	
	static ArrayList<UserVO> userList = new ArrayList<UserVO>(); // 접속한 유저들 있는 리스트
	static Semaphore sema = new Semaphore(1); // 세마포어이자, 이진 세마포어로 뮤텍스와 같은 기능 가능
	
	public static void main(String[] args) {
	
		try {
			
			ServerSocket serverSocket = new ServerSocket(9190); // 서버 소켓 생성 (포트는 9190 고정)
			while(true) {
				System.out.println("현재 접속 대기 상태...");
				
				UserVO user = new UserVO(); // 접속한 유저
				
				// 연결 요청 온 소켓 수락		
				user.setUserSocket(serverSocket.accept());
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream()));
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream()));
				String userId = reader.readLine(); // 접속하고자 하는 사용자의 아이디
				String chkId = "pass"; // 아이디 중복 체크 (pass일 경우 사용 가능)
				
				for (int i = 0; i < userList.size(); i++) {
					// 아이디 중복 검사
					System.out.println("아이디 중복 검사 : " + userList.size());
					System.out.println("입력한 아이디 : " + userId);
					if (userList.get(i).getUserId().equals(userId)) {
						 // 사용 불가능
						chkId = "fail";
					}			
				}
				
				writer.println(chkId); // 중복 검사 결과 보내기
				writer.flush();
				
				if (chkId.equals("fail")) {
					// 아이디가 중복하여 소켓 종료
					user.getUserSocket().close();
					
				} else {
					// 아이디 사용 가능
					// 쓰레드 생성
					CreateThread userThread = new CreateThread(user, sema); // 사용자객체와 세마포어를 이용하여 생성자 생성
					userThread.start();
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean chkUserId(String userId) {
		// 현재 중복되는 아이디가 있는지 확인
		System.out.println("유저 리스트 사이즈 : " + userList.size());
		
		
		
		return false; // 사용 가능
	}
}
