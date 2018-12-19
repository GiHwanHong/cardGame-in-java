package com.sec.hong.samecardgame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * @author GiHwanHong
 * @Class GameMain 
 * @Content ī������� ����Ŭ����.
 */

public class GameMain {
	
	public static void main(String[] args) throws InterruptedException{

		Logger log = Logger.getInstance();
		String[] levelOption = {"�ʱ�", "�߱�","���"};
		GameFrame gameFrame1;
		GamePlay game_user1;
		String username="";
		
		Date dt,dt2;
		int startTime, endTime;
		int playingTime1;
		
		int viewTime1; 							   				// ī�带 ������ �ð�, ������ ���� �����ȴ�.
		
		JOptionPane startGame1 = new JOptionPane(); 			// ���̵� ���� â Ȱ��ȭ.
		// 1. ������� �̸��� �Է¹޴´�.
		username = gameStartUser(startGame1, username);
		log.writelog(GameMain.class.getSimpleName(),"Start Game. ����� �̸� : " + username);
		// 2. ���̵��� �����Ѵ�.
		viewTime1 = gameStart(startGame1, levelOption, username);
		dt = new Date();					   				//�ð��ʸ� �ޱ����� Date ��ü����
		startTime = dt.getSeconds();		   							//�ð��� �޾ƿ�
		gameFrame1 = new GameFrame(username);			   	//��ü���� Ʋ�� ������.
		game_user1 = new GamePlay(gameFrame1, viewTime1);
		game_user1.start();									// ���� �÷��̿� ���õ� ������ �۵�
		game_user1.join();									// �����尡 ������ ���� ��ٸ�.
		dt2 = new Date();
		endTime = dt2.getSeconds();
		playingTime1 = endTime - startTime - viewTime1;
		
		/* ���߿� �޾ƿ� �ð��ʿ��� ó�� �ð��ʸ� ���� ������ �ð��� ����
		      ������ ī�带 ����� ���ߴµ��� ����� �ð��� ����� */
		gameFrameProcess(gameFrame1,viewTime1,playingTime1);
		log.writelog(GameMain.class.getSimpleName(),"END Game. ����� �̸� : " + username + " | ���ӽð� : " + playingTime1);
	}
		
	public static String gameStartUser(JOptionPane j, String username) {
		username = JOptionPane.showInputDialog(j,"����� �̸��� �Է����ּ���.","");
		return username;
	}
	
	//�Ʒ��� ���� ���۽ÿ� ���̵��� �����ϱ� ���� �޼ҵ�.
	public static int gameStart(JOptionPane j, String[] levelOption, String username) {
		int selectedLevel;
		j.setVisible(true);
		selectedLevel = j.showOptionDialog(j, "���̵��� �������ּ���", "���̵� ���� ",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, levelOption, levelOption[1]);
		
		ConnectServer(username, levelOption[selectedLevel]);
		
		return (((2 - selectedLevel) * 2) + 2);
	}
	
	public static void gameFrameProcess(GameFrame gameframe, int viewTime1, long playingTime1) {
		gameframe.setDialog(gameframe.viewRank, viewTime1, playingTime1);
		gameframe.viewRank.setVisible(true);						//������������.
		gameframe.viewRank.setSize(250,250);						//������ ����.
		gameframe.viewRank.setLocationRelativeTo(gameframe);		//���â�� �������� �߾�����.
		gameframe.viewRank.setResizable(false);						//���â ������ ���� �Ұ�.
		gameframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				int result = JOptionPane.showConfirmDialog(gameframe,"���� ���� �Ͻ� ������?","Ȯ��", JOptionPane.YES_OPTION);
					if(result == 0) {
						System.exit(0);
					}
			}
		});
	}
	public static void ConnectServer(String username, String levelOption) {
		Socket socket;
		DataOutputStream out;
		try {
			socket = new Socket("127.0.0.1",9000);
			out = new DataOutputStream(socket.getOutputStream());
			
			out.writeUTF(username);
			out.writeUTF(levelOption);
			
			Thread receiver = new Thread(new ClientReceiver(socket));
			receiver.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
}
