package com.sec.hong.samecardgame;

import java.time.chrono.IsoChronology;

import javax.swing.JOptionPane;

/**
 * @author GiHwanHong
 * @Class GamePlay
 * @Content 쓰레드를 상속받아 카드의 동작을 처리한다.
 */

public class GamePlay extends Thread implements GameStatus{
	GameFrame gf;
	int limitTime;			//게임 난이도에 따라서, 카드 보여줄 시간을 넘겨받는다.
	
	public GamePlay(GameFrame g, int limitTime) {
		gf = g;
		this.limitTime = limitTime;
	}
	
	//해당 스레드에서 .start()메소드를 이용했을 시 호출되는 run()을 구현한 부분.
	public void run() {
		initCard(gf.card);
		oneTimeView(gf.card);
		try {
			sleep(1000 * limitTime);
			reCard(gf.card);
			
			while (!clearGame(gf.card)) {
			}//해당 while문에서, 게임이 클리어, 즉 모든 카드가 다 뒤집히기 전까진 무한루프를 빠져나올 수 없다.

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 카드를 모두 뒤집기 전으로 만들어 주기 위한 메소드.
	@Override
	public void initCard(GameCard[] c) {
		for (int i = 0; i < c.length; i++) {
			if (c[i].Status() != 2)
				c[i].CardOff();
		}
	}

	@Override
	public void oneTimeView(GameCard[] c) {
		// 처음 사진 보여주기 위한 메소드.
		for (int i = 0; i < c.length; i++) {
			c[i].CardOn();
		}
	}

	@Override
	public boolean clearGame(GameCard[] c) {
		boolean isClear = true;
		for (int i = 0; i < c.length && isClear; i++) {
			if (c[i].Status() != 2)
				isClear = false;
		}
		return isClear;
	}

	//보여주는 화면에서 혹시 게임을 진행했을 경우에 대비, 게임 카드를 처음상태로 돌림.
	@Override
	public void reCard(GameCard[] c) {
		for (int i = 0; i < c.length; i++) {
			c[i].changeStatus(0);//상태를 디폴트상태인 0번으로 바꾸어주고,
			c[i].setEnabled(true);//버튼을 사용할 수 있게해줌. (맞추면 누를수 없기때문에) 시작전에 맞추면 못누르는 사태 발생.
		}
		initCard(c);
	}
}
