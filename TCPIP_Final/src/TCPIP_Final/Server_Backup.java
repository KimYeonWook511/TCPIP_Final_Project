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
			// ���� ���� ����
			serverSocket = new ServerSocket(9190);
			
			// ���� ��û �ޱ�
			System.out.println("���� ��ٸ��� ��...");
			// ���� ��û �� ���� ����
			Socket socket = serverSocket.accept();
			System.out.println("���� �Ϸ�");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(true) {
				String clientMsg = reader.readLine();
				
				if (clientMsg.toUpperCase().equals("Q")) {
					System.out.println("Ŭ���̾�Ʈ�� ������ �����ϼ̽��ϴ�.");
					
					reader.close();
					writer.close();
					
					socket.close();
					serverSocket.close();
					break;
				}
				
				if (clientMsg.equals("")) System.out.println("��ĭ�̿���");
				
				System.out.println("Ŭ���̾�Ʈ�� �޼��� : " + clientMsg);
				
			}
			
//			while(true) {
//				// ���� ��û �ޱ�
//				System.out.println("���� ��ٸ��� ��...");
//				// ���� ��û �� ���� ����
//				Socket socket = serverSocket.accept();
//				
//				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//				
//				InetSocketAddress isa = (InetSocketAddress)socket.getRemoteSocketAddress();
//				System.out.println("���� �Ϸ� (IP : " + isa.getHostName() + ")");
//				
//				
//				
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		if (!serverSocket.isClosed()) {
//			// ���� ������ ������ �ʾ����� �ݱ�
//			try {
//				// ���� ���� �ݱ�
//				serverSocket.close();
//				
//			} catch (Exception e) {
//				// Excpetion ó��
//				e.printStackTrace();
//			}
//		}
	}
}
