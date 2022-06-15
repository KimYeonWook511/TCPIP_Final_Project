package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client2 { // 클라이언트 클래스
	
	public static void main(String[] args) {
		
		Socket socket = null;
		String userId; // 접속자의 아이디(닉네임)
		String serverIP; // 접속할 서버 IP
		final int serverPort = 9190; // 접속할 서버 Port 9190으로 고정
		Scanner sc = new Scanner(System.in); 
		String input; // 접속자의 채팅 메세지
		PrintWriter writer;
		BufferedReader reader;
		
		try {
			socket = new Socket();
						
			System.out.print("연결할 서버 IP : ");
			serverIP = sc.nextLine();
			
			while (true) {
				System.out.print("접속할 ID : ");
				userId = sc.nextLine();
				
				if (userId.trim().equals("")) {
					// ID 미입력 X
					System.out.println("한 글자 이상 입력해 주세요.");
					
				} else {
					break;
				}
			}
			
			
			while (true) {
				// 입력한 서버IP와 미리 지정해둔 서버 Port(9190)을 사용하여 소켓 연결 요청
				socket = new Socket();
				socket.connect(new InetSocketAddress(serverIP, serverPort));
				writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				writer.println(userId); // 아이디 중복 여부 확인을 위해 서버에 전송
				writer.flush();
				String chkId = reader.readLine(); // 중복 여부 결과를 받아옴
				
				if (chkId.equals("fail")) {
					// 아이디 중복 체크 (fail일 경우 아이디 중복)
					System.out.println("이미 존재하는 ID 입니다.");
					// 아이디 다시 입력 받음
					System.out.print("접속할 ID : ");
					userId = sc.nextLine();
					
					if (socket != null) {
						socket.close();
						
					}
					
				} else {
					// 접속 가능
					
					break;
				}
			}
			
			System.out.println("서버에 연결이 완료되었습니다. 연결 종료 (/Q or /q)");	
			
			writer.println(userId);
			writer.flush(); // stream을 flush하여 버퍼링되어 아직 기록되지 않은 데이터를 출력 스트림에 모두 출력
			
			// ReadThread : 서버에서 오는 메세지를 출력하는 쓰레드
			ReadThread readThread = new ReadThread(reader);
			readThread.start();
			
			while ((input = sc.nextLine()) != null) {
				
				if (input.toUpperCase().equals("/Q")) {
					// 접속 종료
					System.out.println("접속을 종료하셨습니다.");
					
					writer.println(input);
					
					if (writer != null) {
						// PrintWriter 종료
						writer.close(); // close 안에 flush 내장?????????
						
					}
					if (sc != null) {
						// Scanner 종료
						sc.close();
						
					}
					if (socket != null) {
						// Socket 종료
						socket.close();
						
					}
					
					break;
					
				} else if (!input.equals("")) { 
					// 빈칸 입력 방지
					writer.println(input);
					writer.flush(); // stream을 flush하여 버퍼링되어 아직 기록되지 않은 데이터를 출력 스트림에 모두 출력
					 
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
