package com.sec.hong.samecardgame;

import java.time.chrono.IsoChronology;

import javax.swing.JOptionPane;

/**
 * @author GiHwanHong
 * @Class GamePlay
 * @Content �����带 ��ӹ޾� ī���� ������ ó���Ѵ�.
 */

public class GamePlay extends Thread implements GameStatus{
	GameFrame gf;
	int limitTime;			//���� ���̵��� ����, ī�� ������ �ð��� �Ѱܹ޴´�.
	
	public GamePlay(GameFrame g, int limitTime) {
		gf = g;
		this.limitTime = limitTime;
	}
	
	//�ش� �����忡�� .start()�޼ҵ带 �̿����� �� ȣ��Ǵ� run()�� ������ �κ�.
	public void run() {
		initCard(gf.card);
		oneTimeView(gf.card);
		try {
			sleep(1000 * limitTime);
			reCard(gf.card);
			
			while (!clearGame(gf.card)) {
			}//�ش� while������, ������ Ŭ����, �� ��� ī�尡 �� �������� ������ ���ѷ����� �������� �� ����.

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ī�带 ��� ������ ������ ����� �ֱ� ���� �޼ҵ�.
	@Override
	public void initCard(GameCard[] c) {
		for (int i = 0; i < c.length; i++) {
			if (c[i].Status() != 2)
				c[i].CardOff();
		}
	}

	@Override
	public void oneTimeView(GameCard[] c) {
		// ó�� ���� �����ֱ� ���� �޼ҵ�.
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

	//�����ִ� ȭ�鿡�� Ȥ�� ������ �������� ��쿡 ���, ���� ī�带 ó�����·� ����.
	@Override
	public void reCard(GameCard[] c) {
		for (int i = 0; i < c.length; i++) {
			c[i].changeStatus(0);//���¸� ����Ʈ������ 0������ �ٲپ��ְ�,
			c[i].setEnabled(true);//��ư�� ����� �� �ְ�����. (���߸� ������ ���⶧����) �������� ���߸� �������� ���� �߻�.
		}
		initCard(c);
	}
}
