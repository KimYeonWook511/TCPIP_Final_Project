package TCPIP_Final;

import java.util.ArrayList;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		String input = sc.nextLine();
		System.out.println(input == null);
		System.out.println(input.equals(""));
		System.out.println(input.equals(" "));
		System.out.println(input.trim().equals(""));
		
//		System.out.println("�빮�� : " + input.toUpperCase());
//		System.out.println("�Է� : " + input);
//		System.out.println("charAt(0) : " + input.charAt(0));
//		System.out.println(input.charAt(0) == '/');
//		System.out.println("�Է� : " + input);
//		System.out.println(input.split("/")[0]);
//		System.out.println(input.split("/")[1]);
//		System.out.println("�Է� : " + input);
		
		if (input.charAt(0) == '/') { 
			// ù �Է��� '/'�� ��� Ư����� �۵�
			if (input.toUpperCase().equals("/Q")) { 
				// /q, /Q �Է½� ���� ���� 
				System.out.println("���� ����");
				
			} else if (input.equals("/?")) { 
				// /? �Է½� ���� ���
				System.out.println("---------------���� ����Դϴ�.---------------");
				System.out.println("���� ���� : /q Ȥ�� /Q");
				System.out.println("�ӼӸ� : /to �������̵� ����");
				System.out.println("-------------------------------------------");
				
			} else if (input.split(" ")[0].equals("/to")) { 
				// /to �Է½� �������� �ӼӸ� ���
				if (input.split(" ").length >= 3) { 
					String toUser = input.split(" ")[1];
					String content = input.split(toUser + " ")[1];
					
					// ���� �ӼӸ� ó��
					System.out.println("�ӼӸ� ó����..");
					System.out.println(toUser.equals(""));
					System.out.println(content);
					
					
				} else { // �ӼӸ� �Է� ���� ���
					System.out.println("�ӼӸ� ���Ŀ� �°� �Է��� �ּ���.");
					System.out.println("�ӼӸ� : /to �������̵� ����");
					
				}	
			}	
		}
		
//		ArrayList<String> list = new ArrayList<String>();
//		
//		list.add("1��");
//		list.add("2��");
//		list.add("3��");
//		
//		System.out.println(list.get(1));
//		System.out.println(list.remove("2��"));
//		System.out.println(list.get(1));
	}
}
