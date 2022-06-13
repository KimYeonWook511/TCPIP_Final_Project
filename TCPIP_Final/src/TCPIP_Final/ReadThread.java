package TCPIP_Final;

import java.io.BufferedReader;

public class ReadThread extends Thread {
	// 클라이언트에서 생성하는 쓰레드
	private BufferedReader reader; // 접속한 유저의 버퍼리더
	
	public ReadThread(BufferedReader reader) {
		// 생성자
		this.reader = reader;
	}
	
	@Override
	public void run() {
		// 쓰레드 시작
		String msg;
		
		try {
			while((msg = reader.readLine()) != null) {
				System.out.println(msg);
				
			}
			reader.close();
			
		} catch (Exception e) {
			System.out.println("정상적으로 종료되었습니다.");
			
		} finally {
			if (reader != null) {
				try {
					reader.close();
					
				} catch(Exception closeE) {
					System.out.println("종료중 오류");
					closeE.printStackTrace();
					
				}
			}
			
		}
	}
}
