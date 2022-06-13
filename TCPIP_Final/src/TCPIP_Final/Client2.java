package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
	
	public static void main(String[] args) {
		
		Socket socket = null;
		String userId; // �������� ���̵�(�г���)
		String serverIP; // ������ ���� IP
		int serverPort = 9190; // ������ ���� Port 9190���� ����
		Scanner sc = new Scanner(System.in); 
		String input; // �������� ä�� �޼���
		
		try {
			socket = new Socket();
			
			System.out.print("������ ���� IP : ");
			serverIP = sc.nextLine();
			
			System.out.print("ID�� �Է��� �ּ���. : ");
			userId = sc.nextLine();
			
			socket.connect(new InetSocketAddress(serverIP, serverPort));
			System.out.println("������ ������ �Ϸ�Ǿ����ϴ�. ���� ���� (Q or q)");
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			writer.println(userId);
			writer.flush();
			
			ReadThread readThread = new ReadThread(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			readThread.start();
			
			
			while ((input = sc.nextLine()) != null) {
				
				if (input.toUpperCase().equals("/Q")) {
					// ���� ����
					System.out.println("������ �����ϼ̽��ϴ�.");
					
					writer.println(input);
					
					if (writer != null) {
						// PrintWriter ����
						writer.close(); // close �ȿ� flush ����?????????
						
					}
					if (sc != null) {
						// Scanner ����
						sc.close();
					}
					if (socket != null) {
						// Socket ����
						socket.close();
					}
					
					break;
					
				} else if (!input.equals("")) { 
					// ��ĭ �Է� ����
					writer.println(input); // �Է��� ������ ���ۿ� �ø� << ????
					writer.flush(); // �Է��� ������ ������ << ????
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
