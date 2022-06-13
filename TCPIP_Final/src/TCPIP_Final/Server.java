package TCPIP_Final;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server {
	
	private static ArrayList<UserVO> userList = new ArrayList<UserVO>(); // ������ ������ �ִ� ����Ʈ
	private static Semaphore sema = new Semaphore(1); // ������������, ���� ��������� ���ؽ��� ���� ��� ���� | synchronized�� mutex�� ���̴�?
	
	public static void main(String[] args) {
	
		try {
			
			ServerSocket serverSocket = new ServerSocket(9190); // ���� ���� ���� (��Ʈ�� 9190 ����)
			
			while(true) {
				System.out.println("���� ���� ��� ����...");
				
				UserVO user = new UserVO(); // ������ ����
				
				// ���� ��û �� ���� ����		
				user.setUserSocket(serverSocket.accept());
				
				CreateThread userThread = new CreateThread(user, userList, sema);
				userThread.start();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean chkUserId(String userId) {
		// ���� �ߺ��Ǵ� ���̵� �ִ��� Ȯ��
		for (int i = 0; i < userList.size(); i++) {
			
			if (userList.get(i).getUserId().equals(userId)) {
				return true; // ��� �Ұ���
				
			}			
		}
		
		return false; // ��� ����
	}
}
