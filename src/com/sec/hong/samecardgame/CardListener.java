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
 * @Content 카드를 눌렀을 때 발생하는 이벤트에 대한 리스너 실행.
 */

public class CardListener implements ActionListener {

	int cardCnt = 0;
	GameCard gCard = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GameCard clickedCard = (GameCard) e.getSource();
		try {
			if(clickedCard.Status() == 1)
			// 카드가 뒤집힌 상태일 경우, 다시 뒤집어서 원상태로 되돌려줌.
			clickedCard.CardOff();
			else if(clickedCard.Status() != 2) {
				// 카드의 상태가 2(고정)상태라면, 뒤집을 필요가 없음.
				clickedCard.CardOn();
				clickedCard.Clicked=true; 
			}
		
			if(cardCnt == 1) {
				cardCnt = 0;
				// 카드가 두 번째 눌렸으면, cardCnt가 1인 상태이므로, 다음의 if문에 진입하게 됨.
				if(clickedCard != gCard) {
					//Thread.sleep(1000);
					isResult(new JOptionPane(), clickedCard, gCard);
				}
				gCard = null;
				
			} else {
				gCard = clickedCard;
				cardCnt++;
			}
		// 사운드 처리에 관련된 부분, 카드를 클릭하게 될 경우 사운드를 실행.
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
			ResCheck(j,"다르다.");
			a.CardOff();
			b.CardOff();
		}
	}
	//아래는 게임 시작시에 난이도를 설정하기 위한 메소드.
		public static void ResCheck(JOptionPane j,String result) {
			j.showMessageDialog(j, "첫번째 그림과 두번째 그림은 "+ result, "", JOptionPane.INFORMATION_MESSAGE);
		}

}
