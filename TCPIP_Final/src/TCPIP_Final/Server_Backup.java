package TCPIP_Final;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Backup {
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket;
		
		try {
			// 서버 소켓 생성
			serverSocket = new ServerSocket(9190);
			
			// 연결 요청 받기
			System.out.println("연결 기다리는 중...");
			// 연결 요청 온 소켓 수락
			Socket socket = serverSocket.accept();
			System.out.println("연결 완료");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(true) {
				String clientMsg = reader.readLine();
				
				if (clientMsg.toUpperCase().equals("Q")) {
					System.out.println("클라이언트가 접속을 종료하셨습니다.");
					
					reader.close();
					writer.close();
					
					socket.close();
					serverSocket.close();
					break;
				}
				
				if (clientMsg.equals("")) System.out.println("빈칸이에용");
				
				System.out.println("클라이언트의 메세지 : " + clientMsg);
				
			}
			
//			while(true) {
//				// 연결 요청 받기
//				System.out.println("연결 기다리는 중...");
//				// 연결 요청 온 소켓 수락
//				Socket socket = serverSocket.accept();
//				
//				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//				
//				InetSocketAddress isa = (InetSocketAddress)socket.getRemoteSocketAddress();
//				System.out.println("연결 완료 (IP : " + isa.getHostName() + ")");
//				
//				
//				
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		if (!serverSocket.isClosed()) {
//			// 서버 소켓이 닫히지 않았으면 닫기
//			try {
//				// 서버 소켓 닫기
//				serverSocket.close();
//				
//			} catch (Exception e) {
//				// Excpetion 처리
//				e.printStackTrace();
//			}
//		}
	}
}
