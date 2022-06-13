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
		
//		System.out.println("대문자 : " + input.toUpperCase());
//		System.out.println("입력 : " + input);
//		System.out.println("charAt(0) : " + input.charAt(0));
//		System.out.println(input.charAt(0) == '/');
//		System.out.println("입력 : " + input);
//		System.out.println(input.split("/")[0]);
//		System.out.println(input.split("/")[1]);
//		System.out.println("입력 : " + input);
		
		if (input.charAt(0) == '/') { 
			// 첫 입력이 '/'일 경우 특수기능 작동
			if (input.toUpperCase().equals("/Q")) { 
				// /q, /Q 입력시 접속 종료 
				System.out.println("접속 종료");
				
			} else if (input.equals("/?")) { 
				// /? 입력시 도움말 기능
				System.out.println("---------------도움말 기능입니다.---------------");
				System.out.println("접속 종료 : /q 혹은 /Q");
				System.out.println("귓속말 : /to 유저아이디 내용");
				System.out.println("-------------------------------------------");
				
			} else if (input.split(" ")[0].equals("/to")) { 
				// /to 입력시 유저에게 귓속말 기능
				if (input.split(" ").length >= 3) { 
					String toUser = input.split(" ")[1];
					String content = input.split(toUser + " ")[1];
					
					// 이하 귓속말 처리
					System.out.println("귓속말 처리중..");
					System.out.println(toUser.equals(""));
					System.out.println(content);
					
					
				} else { // 귓속말 입력 형식 벗어남
					System.out.println("귓속말 형식에 맞게 입력해 주세요.");
					System.out.println("귓속말 : /to 유저아이디 내용");
					
				}	
			}	
		}
		
//		ArrayList<String> list = new ArrayList<String>();
//		
//		list.add("1번");
//		list.add("2번");
//		list.add("3번");
//		
//		System.out.println(list.get(1));
//		System.out.println(list.remove("2번"));
//		System.out.println(list.get(1));
	}
}
