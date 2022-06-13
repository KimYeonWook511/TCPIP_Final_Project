package TCPIP_Final;

import java.io.BufferedReader;

public class ReadThread extends Thread {
	// Ŭ���̾�Ʈ���� �����ϴ� ������
	private BufferedReader reader; // ������ ������ ���۸���
	
	public ReadThread(BufferedReader reader) {
		// ������
		this.reader = reader;
	}
	
	@Override
	public void run() {
		// ������ ����
		String msg;
		
		try {
			while((msg = reader.readLine()) != null) {
				System.out.println(msg);
				
			}
			reader.close();
			
		} catch (Exception e) {
			System.out.println("���������� ����Ǿ����ϴ�.");
			
		} finally {
			if (reader != null) {
				try {
					reader.close();
					
				} catch(Exception closeE) {
					System.out.println("������ ����");
					closeE.printStackTrace();
					
				}
			}
			
		}
	}
}
