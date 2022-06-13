package TCPIP_Final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CreateThread extends Thread {
	// �������� �����ϴ� ������

	private BufferedReader reader;
	private UserVO user; // ��� ������ ���� ��ü
	private ArrayList<UserVO> userList; // ������ �������� ����Ʈ
	private Semaphore sema;

	public CreateThread(UserVO user, ArrayList<UserVO> userList, Semaphore sema) {
		// ������
		this.user = user;
		this.userList = userList;
		this.sema = sema;

		try {
			reader = new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream()));

			user.setUserId(reader.readLine()); // ���� �̸� ����
			user.setUserWriter(new PrintWriter(new OutputStreamWriter(user.getUserSocket().getOutputStream())));

			System.out.println(user.getUserId() + "���� �����ϼ̽��ϴ�.");

			notify(user.getUserId() + "���� �����ϼ̽��ϴ�.");

			sema.acquire(); // �������� ȹ��
			userList.add(user); // ���� ���� ����Ʈ�� �߰�
			sema.release(); // �������� �ݳ�

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public void run() {
		// ������ ����
		String input;

		try {
			while ((input = reader.readLine()) != null) {
				
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

					} else if (input.toUpperCase().equals("/PEOPLE")) {
						// /people, /PEOPLE �Է� �� ���� ���� ���� �ο� �� ���
						user.getUserWriter().println("���� ���� ���� �ο� : " + userList.size());
						user.getUserWriter().flush();

					} else if (input.toUpperCase().equals("/LIST")) {
						// ���� ���� ���� ����� ����Ʈ ����

						for (int i = 0; i < userList.size(); i++) {
							// ��ü ����� �˻�
							user.getUserWriter().println(userList.get(i).getUserId());
							user.getUserWriter().flush();

						}

					} else if (input.split(" ")[0].equals("/to")) {
						// /to �Է½� �������� �ӼӸ� ���

						if (input.split(" ").length < 3) {
							// �ӼӸ� �Է� ���� ��� (/to userid�� �Է��� ���)
							user.getUserWriter().println("�ӼӸ� ���Ŀ� �°� �Է��� �ּ���.");
							user.getUserWriter().println("�ӼӸ� : /to �������̵� ����");
							user.getUserWriter().flush();

						} else {

							if (input.split(" ")[1].equals("")) {
								// �ӼӸ� �Է� ���� ��� (/to userid msg) -> /to���� ���Ⱑ �� �� �̻� �ִ� ���
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

				// userList ���� ���� (ó�� �߿� �� �ٸ� �����尡 ����ϴ� ���� ����)
				sema.acquire(); // �������� ȹ��
				userList.remove(user);
				sema.release(); // �������� �ݳ�

				user.close(); // ������ Socket, PrintWriter ����

				if (reader != null) {
					// BufferedReader ����
					reader.close();

				}

			} catch (Exception closeE) {
				System.out.println("CreateThread���� close ����");
				closeE.printStackTrace();

			}

		}
	}

	private void notify(String msg) {
		// ������ ��������

		try {

			sema.acquire(); // �������� ȹ��
			for (int i = 0; i < userList.size(); i++) {
				// ������ �ִ� �������� �����ϱ�
				PrintWriter writer = userList.get(i).getUserWriter();
				if (writer != null) {
					writer.println(msg);
					writer.flush();
				}

			}
			sema.release(); // �������� �ݳ�

		} catch (Exception e) {
			e.printStackTrace();

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
