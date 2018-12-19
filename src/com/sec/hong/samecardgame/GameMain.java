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
 * @Content 카드게임의 메인클래스.
 */

public class GameMain {
	
	public static void main(String[] args) throws InterruptedException{

		Logger log = Logger.getInstance();
		String[] levelOption = {"초급", "중급","고급"};
		GameFrame gameFrame1;
		GamePlay game_user1;
		String username="";
		
		Date dt,dt2;
		int startTime, endTime;
		int playingTime1;
		
		int viewTime1; 							   				// 카드를 보여줄 시간, 레벨에 따라 결정된다.
		
		JOptionPane startGame1 = new JOptionPane(); 			// 난이도 결정 창 활성화.
		// 1. 사용자의 이름을 입력받는다.
		username = gameStartUser(startGame1, username);
		log.writelog(GameMain.class.getSimpleName(),"Start Game. 사용자 이름 : " + username);
		// 2. 난이도를 설정한다.
		viewTime1 = gameStart(startGame1, levelOption, username);
		dt = new Date();					   				//시간초를 받기위한 Date 객체생성
		startTime = dt.getSeconds();		   							//시간초 받아옴
		gameFrame1 = new GameFrame(username);			   	//전체적인 틀을 생성함.
		game_user1 = new GamePlay(gameFrame1, viewTime1);
		game_user1.start();									// 게임 플레이에 관련된 스레드 작동
		game_user1.join();									// 스레드가 끝날때 까지 기다림.
		dt2 = new Date();
		endTime = dt2.getSeconds();
		playingTime1 = endTime - startTime - viewTime1;
		
		/* 나중에 받아온 시간초에서 처음 시간초를 빼고 보여준 시간을 빼서
		      순전히 카드를 뒤집어서 맞추는데에 사용한 시간을 계산함 */
		gameFrameProcess(gameFrame1,viewTime1,playingTime1);
		log.writelog(GameMain.class.getSimpleName(),"END Game. 사용자 이름 : " + username + " | 게임시간 : " + playingTime1);
	}
		
	public static String gameStartUser(JOptionPane j, String username) {
		username = JOptionPane.showInputDialog(j,"사용자 이름을 입력해주세요.","");
		return username;
	}
	
	//아래는 게임 시작시에 난이도를 설정하기 위한 메소드.
	public static int gameStart(JOptionPane j, String[] levelOption, String username) {
		int selectedLevel;
		j.setVisible(true);
		selectedLevel = j.showOptionDialog(j, "난이도를 선택해주세요", "난이도 설정 ",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, levelOption, levelOption[1]);
		
		ConnectServer(username, levelOption[selectedLevel]);
		
		return (((2 - selectedLevel) * 2) + 2);
	}
	
	public static void gameFrameProcess(GameFrame gameframe, int viewTime1, long playingTime1) {
		gameframe.setDialog(gameframe.viewRank, viewTime1, playingTime1);
		gameframe.viewRank.setVisible(true);						//보여지도록함.
		gameframe.viewRank.setSize(250,250);						//사이즈 정함.
		gameframe.viewRank.setLocationRelativeTo(gameframe);		//결과창을 프레임의 중앙으로.
		gameframe.viewRank.setResizable(false);						//결과창 사이즈 조절 불가.
		gameframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				int result = JOptionPane.showConfirmDialog(gameframe,"정말 종료 하실 껀가요?","확인", JOptionPane.YES_OPTION);
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
