package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CreateThread_Backup extends Thread {
	// 서버에서 생성하는 쓰레드
	
	private BufferedReader reader;
	private UserVO user; // 방금 접속한 유저 객체
	private ArrayList<UserVO> userList; // 접속한 유저들의 리스트
	private Semaphore sema = new Semaphore(1); // 세마포어이자, 이진 세마포어로 뮤텍스와 같은 기능 가능
	
	public CreateThread_Backup(UserVO user, ArrayList<UserVO> userList) {
		// 생성자
		this.user = user;
		this.userList = userList;
			
		try {
			reader = new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream()));
			
			user.setUserId(reader.readLine()); // 유저 이름 저장
			user.setUserWriter(new PrintWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream())));
			
			System.out.println(user.getUserId() + "님이 접속하셨습니다.");
			
			notify(user.getUserId() + "님이 접속하셨습니다.");
			
			synchronized (userList) {
				// userList 접근 제한 (임계 영역 부분 작동 시 다른 쓰레드 접근 제한)
				userList.add(user); 
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	@Override
	public void run() {
		// 쓰레드 시작
		String input;
		
		try {		
			while ((input = reader.readLine()) != null) { // .equals("")로 해야하는지 확인
				
				if (input.charAt(0) == '/') { 
					// 첫 입력이 '/'일 경우 특수기능 작동
					
					if (input.toUpperCase().equals("/Q")) { 
						// /q, /Q 입력 시 접속 종료 
						System.out.println(user.getUserId() + "님이 접속을 종료하셨습니다.");

						notify(user.getUserId() + "님이 접속을 종료하셨습니다.");
						
						break;
						
					} else if (input.equals("/?")) { 
						// /? 입력시 도움말 기능		
						user.getUserWriter().println("---------------도움말 기능입니다.---------------");
						user.getUserWriter().println("접속 종료 : /q 혹은 /Q");
						user.getUserWriter().println("귓속말 : /to 유저아이디 내용");
						user.getUserWriter().println("-------------------------------------------");
						user.getUserWriter().flush();
						
					} else if (input.toUpperCase().equals("/people")) {
						// /people, /PEOPLE 입력 시 현재 접속 중인 인원 수 출력
						user.getUserWriter().println("현재 접속 중인 인원 : " + userList.size());
						user.getUserWriter().flush();
						
						
					} else if (input.split(" ")[0].equals("/to")) { 
						// /to 입력시 유저에게 귓속말 기능		
						
						if (input.split(" ").length < 3) { 
							// 귓속말 입력 형식 벗어남 (/to userid만 입력한 경우)		
							user.getUserWriter().println("귓속말 형식에 맞게 입력해 주세요.");
							user.getUserWriter().println("귓속말 : /to 유저아이디 내용");
							user.getUserWriter().flush();
							
						} else { 
							
							if (input.split(" ")[1].equals("")) {
								// 귓속말 입력 형식 벗어남 (/to  userid msg) -> /to다음 띄어쓰기가 두 개 이상 있는 경우
								user.getUserWriter().println("귓속말 형식에 맞게 입력해 주세요.");
								user.getUserWriter().println("귓속말 : /to 유저아이디 내용");
								user.getUserWriter().flush();
								
							} else {
								// 귓속말 입력 형식 통과
								String toUser = input.split(" ")[1];
								String msg = input.split(toUser + " ")[1];
								
								whisper(toUser, msg); // 귓속말 보내기
							}
							
						}	
						
					} else {
						// /입력 후 잘못된 입력
						user.getUserWriter().println("---------------잘못된 입력입니다.---------------");
						user.getUserWriter().println("접속 종료 : /q 혹은 /Q");
						user.getUserWriter().println("귓속말 : /to 유저아이디 내용");
						user.getUserWriter().println("-------------------------------------------");
						user.getUserWriter().flush();
						
					}
					
				} else {
					// 채팅 보내기 (사용자 전부)
					notify(user.getUserId() + " : " + input);
					
				}
				
			} // while문의 끝
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// 이하 close 처리	
			try {

				synchronized (userList) {
					// userList 접근 제한 (처리 중에 또 다른 쓰레드가 사용하는 것을 방지)
					userList.remove(user);

				}
				
				user.close(); // 유저의 Socket, PrintWriter 종료
				
				if (reader != null) {
					// BufferedReader 종료
					reader.close();		
					
				}
				
			} catch(Exception closeE) {
				System.out.println("CreateThread에서 close 오류");
				closeE.printStackTrace();
				
			}
			
		}
	}
	
	private void notify(String msg) {
		// 서버가 보내는 공지사항(전체 메세지)
		
		synchronized (userList) {
			// userList 접근 제한 (처리 중에 또 다른 쓰레드가 사용하는 것을 방지) 
			
			for (int i = 0; i < userList.size(); i++) {
				// 접속해 있는 유저에게 공지하기
				PrintWriter writer = userList.get(i).getUserWriter(); 
				writer.println(msg);
				writer.flush();
				
			}
		}
		
	}
	
	private void whisper(String toUser, String msg) {
			
		for (int i = 0; i < userList.size(); i++) {
			// 귓속말 대상 찾기
			
			if (userList.get(i).getUserId().equals(toUser)) {
				// 귓속말 대상에게 메세지 보내기
				userList.get(i).getUserWriter().println(user.getUserId() + "(귓속말) : " + msg);
				userList.get(i).getUserWriter().flush();

			}
		}
	}
}
