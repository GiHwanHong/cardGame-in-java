package com.sec.hong.samecardgame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author GiHwanHong
 * @Class GameCard
 * @Content 같은 그림 찾기를 위함 Card 객체, JButton을 상속받아서 카드상태, 이미지정보를 저장한다.
 */

public class GameCard extends JButton implements CardStatus{
	// 카드의 상태를 나타낸다. Default : 0, Reverse : 1, Fixed : 2
	private int CARD_STATUS = 0;	
	
	ImageIcon image; // IMG_ID에 따른 그림을 넣기 위함.. 
	int imgID = 0;	 // 0~7, 8쌍의 이미지이므로 8개의 ID만 있으면 된다.
	int myID = 0;	 
	boolean Clicked = false;	// 카드가 눌렸는지, 안눌렸는지 상태를 알기위함.
	/** 
	 * myID : 카드가 눌렸을 때, 어떤카드가 눌렸는지 판별하기위해서 사용되는 변수. 
	 *         이 변수에는 GameFrame에서 배열을 생성할 경우 인덱스가 들어가며 0 ~ 15가 들어간다.
	 */
	
	@Override
	public void CardOn() {
		if(CARD_STATUS != 2) {
			switch (imgID) {
				case 0:
					image = new ImageIcon("images/movie1.jpg");
					break;
				case 1:
					image = new ImageIcon("images/movie2.jpg");
					break;
				case 2:
					image = new ImageIcon("images/movie3.jpg");
					break;
				case 3:
					image = new ImageIcon("images/movie4.jpg");
					break;
				case 4:
					image = new ImageIcon("images/movie5.jpg");
					break;
				case 5:
					image = new ImageIcon("images/movie6.jpg");
					break;
				case 6:
					image = new ImageIcon("images/movie7.jpg");
					break;
				case 7:
					image = new ImageIcon("images/movie8.jpg");
					break;
			}
			setIcon(image);	// 아이콘을 설정해준다.
			changeStatus(1);	
		}
	}
	@Override
	public void CardOff() {
		/*
		 * 카드 뒤집기가 뒤집혀있는 상태, 카드에 이미지를 새로 뿌려주는 방식
		 * 이미지를 Back이미지로 변경해줌.
		 * CARD_STATUS가 2(뒤집혀 있는상태)가 아닐 경우에만 Back 이미지여야 함.
		 */
		setIcon(new ImageIcon("images/Front.png"));
		changeStatus(0); // 뒤집기 전의 상태로 되돌려준다.
	}
	@Override
	public void changeStatus(int status) {
		// 현재상태가 뒤집혀 있는 상태라는 것을 알려준다.
		CARD_STATUS = status;
	}
	
	public int Status() {
		return CARD_STATUS;
	}
	public void playSound() throws UnsupportedAudioFileException,
									IOException, LineUnavailableException{
		AudioInputStream  soundFile = AudioSystem.getAudioInputStream(new File("sound/effect.wav"));
		
		Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
		clip.open(soundFile);
		clip.start();
	}
	public ImageIcon getImage() {
		return image;
	}
	
}
