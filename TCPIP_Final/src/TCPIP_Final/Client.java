package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) {
		
		Socket socket = null;
		String userId; // 접속자의 아이디(닉네임)
		String serverIP; // 접속할 서버 IP
		int serverPort = 9190; // 접속할 서버 Port 9190으로 고정
		Scanner sc = new Scanner(System.in); 
		String input; // 접속자의 채팅 메세지
		
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
					
				} else if (Server.chkUserId(userId)) {
					// 아이디 중복 체크 (ture일 시 아이디 중복)
					System.out.println("이미 존재하는 ID 입니다.");
					
				} else {
					// 접속 가능
					
					break;
				}
			}
			
			socket.connect(new InetSocketAddress(serverIP, serverPort));
			System.out.println("서버에 연결이 완료되었습니다. 연결 종료 (Q or q)");
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			writer.println(userId);
			writer.flush();
			
			ReadThread readThread = new ReadThread(new BufferedReader(new InputStreamReader(socket.getInputStream())));
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
					writer.println(input); // 입력한 내용을 버퍼에 올림 << ????
					writer.flush(); // 입력한 내용을 보내줌 << ????
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
