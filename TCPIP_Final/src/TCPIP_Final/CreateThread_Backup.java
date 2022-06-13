package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CreateThread_Backup extends Thread {
	// �������� �����ϴ� ������
	
	private BufferedReader reader;
	private UserVO user; // ��� ������ ���� ��ü
	private ArrayList<UserVO> userList; // ������ �������� ����Ʈ
	private Semaphore sema = new Semaphore(1); // ������������, ���� ��������� ���ؽ��� ���� ��� ����
	
	public CreateThread_Backup(UserVO user, ArrayList<UserVO> userList) {
		// ������
		this.user = user;
		this.userList = userList;
			
		try {
			reader = new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream()));
			
			user.setUserId(reader.readLine()); // ���� �̸� ����
			user.setUserWriter(new PrintWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream())));
			
			System.out.println(user.getUserId() + "���� �����ϼ̽��ϴ�.");
			
			notify(user.getUserId() + "���� �����ϼ̽��ϴ�.");
			
			synchronized (userList) {
				// userList ���� ���� (�Ӱ� ���� �κ� �۵� �� �ٸ� ������ ���� ����)
				userList.add(user); 
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	@Override
	public void run() {
		// ������ ����
		String input;
		
		try {		
			while ((input = reader.readLine()) != null) { // .equals("")�� �ؾ��ϴ��� Ȯ��
				
				if (input.charAt(0) == '/') { 
					// ù �Է��� '/'�� ��� Ư����� �۵�
					
					if (input.toUpperCase().equals("/Q")) { 
						// /q, /Q �Է� �� ���� ���� 
						System.out.println(user.getUserId() + "���� ������ �����ϼ̽��ϴ�.");

						notify(user.getUserId() + "���� ������ �����ϼ̽��ϴ�.");
						
						break;
						
					} else if (input.equals("/?")) { 
						// /? �Է½� ���� ���		
						user.getUserWriter().println("---------------���� ����Դϴ�.---------------");
						user.getUserWriter().println("���� ���� : /q Ȥ�� /Q");
						user.getUserWriter().println("�ӼӸ� : /to �������̵� ����");
						user.getUserWriter().println("-------------------------------------------");
						user.getUserWriter().flush();
						
					} else if (input.toUpperCase().equals("/people")) {
						// /people, /PEOPLE �Է� �� ���� ���� ���� �ο� �� ���
						user.getUserWriter().println("���� ���� ���� �ο� : " + userList.size());
						user.getUserWriter().flush();
						
						
					} else if (input.split(" ")[0].equals("/to")) { 
						// /to �Է½� �������� �ӼӸ� ���		
						
						if (input.split(" ").length < 3) { 
							// �ӼӸ� �Է� ���� ��� (/to userid�� �Է��� ���)		
							user.getUserWriter().println("�ӼӸ� ���Ŀ� �°� �Է��� �ּ���.");
							user.getUserWriter().println("�ӼӸ� : /to �������̵� ����");
							user.getUserWriter().flush();
							
						} else { 
							
							if (input.split(" ")[1].equals("")) {
								// �ӼӸ� �Է� ���� ��� (/to  userid msg) -> /to���� ���Ⱑ �� �� �̻� �ִ� ���
								user.getUserWriter().println("�ӼӸ� ���Ŀ� �°� �Է��� �ּ���.");
								user.getUserWriter().println("�ӼӸ� : /to �������̵� ����");
								user.getUserWriter().flush();
								
							} else {
								// �ӼӸ� �Է� ���� ���
								String toUser = input.split(" ")[1];
								String msg = input.split(toUser + " ")[1];
								
								whisper(toUser, msg); // �ӼӸ� ������
							}
							
						}	
						
					} else {
						// /�Է� �� �߸��� �Է�
						user.getUserWriter().println("---------------�߸��� �Է��Դϴ�.---------------");
						user.getUserWriter().println("���� ���� : /q Ȥ�� /Q");
						user.getUserWriter().println("�ӼӸ� : /to �������̵� ����");
						user.getUserWriter().println("-------------------------------------------");
						user.getUserWriter().flush();
						
					}
					
				} else {
					// ä�� ������ (����� ����)
					notify(user.getUserId() + " : " + input);
					
				}
				
			} // while���� ��
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// ���� close ó��	
			try {

				synchronized (userList) {
					// userList ���� ���� (ó�� �߿� �� �ٸ� �����尡 ����ϴ� ���� ����)
					userList.remove(user);

				}
				
				user.close(); // ������ Socket, PrintWriter ����
				
				if (reader != null) {
					// BufferedReader ����
					reader.close();		
					
				}
				
			} catch(Exception closeE) {
				System.out.println("CreateThread���� close ����");
				closeE.printStackTrace();
				
			}
			
		}
	}
	
	private void notify(String msg) {
		// ������ ������ ��������(��ü �޼���)
		
		synchronized (userList) {
			// userList ���� ���� (ó�� �߿� �� �ٸ� �����尡 ����ϴ� ���� ����) 
			
			for (int i = 0; i < userList.size(); i++) {
				// ������ �ִ� �������� �����ϱ�
				PrintWriter writer = userList.get(i).getUserWriter(); 
				writer.println(msg);
				writer.flush();
				
			}
		}
		
	}
	
	private void whisper(String toUser, String msg) {
			
		for (int i = 0; i < userList.size(); i++) {
			// �ӼӸ� ��� ã��
			
			if (userList.get(i).getUserId().equals(toUser)) {
				// �ӼӸ� ��󿡰� �޼��� ������
				userList.get(i).getUserWriter().println(user.getUserId() + "(�ӼӸ�) : " + msg);
				userList.get(i).getUserWriter().flush();

			}
		}
	}
}
