package com.sec.hong.samecardgame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * @author GiHwanHong
 * @Class CardListenner
 * @Content ī�带 ������ �� �߻��ϴ� �̺�Ʈ�� ���� ������ ����.
 */

public class CardListener implements ActionListener {

	int cardCnt = 0;
	GameCard gCard = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GameCard clickedCard = (GameCard) e.getSource();
		try {
			if(clickedCard.Status() == 1)
			// ī�尡 ������ ������ ���, �ٽ� ����� �����·� �ǵ�����.
			clickedCard.CardOff();
			else if(clickedCard.Status() != 2) {
				// ī���� ���°� 2(����)���¶��, ������ �ʿ䰡 ����.
				clickedCard.CardOn();
				clickedCard.Clicked=true; 
			}
		
			if(cardCnt == 1) {
				cardCnt = 0;
				// ī�尡 �� ��° ��������, cardCnt�� 1�� �����̹Ƿ�, ������ if���� �����ϰ� ��.
				if(clickedCard != gCard) {
					//Thread.sleep(1000);
					isResult(new JOptionPane(), clickedCard, gCard);
				}
				gCard = null;
				
			} else {
				gCard = clickedCard;
				cardCnt++;
			}
		// ���� ó���� ���õ� �κ�, ī�带 Ŭ���ϰ� �� ��� ���带 ����.
			clickedCard.playSound();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void isResult(JOptionPane j, GameCard a, GameCard b) throws InterruptedException {
		if(a.imgID == b.imgID) {
			a.changeStatus(2);
			b.changeStatus(2);
			a.setEnabled(false);
			b.setEnabled(false);
		}
		if(a.imgID != b.imgID) {
			ResCheck(j,"�ٸ���.");
			a.CardOff();
			b.CardOff();
		}
	}
	//�Ʒ��� ���� ���۽ÿ� ���̵��� �����ϱ� ���� �޼ҵ�.
		public static void ResCheck(JOptionPane j,String result) {
			j.showMessageDialog(j, "ù��° �׸��� �ι�° �׸��� "+ result, "", JOptionPane.INFORMATION_MESSAGE);
		}

}
