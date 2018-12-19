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
 * @Content ���� �׸� ã�⸦ ���� Card ��ü, JButton�� ��ӹ޾Ƽ� ī�����, �̹��������� �����Ѵ�.
 */

public class GameCard extends JButton implements CardStatus{
	// ī���� ���¸� ��Ÿ����. Default : 0, Reverse : 1, Fixed : 2
	private int CARD_STATUS = 0;	
	
	ImageIcon image; // IMG_ID�� ���� �׸��� �ֱ� ����.. 
	int imgID = 0;	 // 0~7, 8���� �̹����̹Ƿ� 8���� ID�� ������ �ȴ�.
	int myID = 0;	 
	boolean Clicked = false;	// ī�尡 ���ȴ���, �ȴ��ȴ��� ���¸� �˱�����.
	/** 
	 * myID : ī�尡 ������ ��, �ī�尡 ���ȴ��� �Ǻ��ϱ����ؼ� ���Ǵ� ����. 
	 *         �� �������� GameFrame���� �迭�� ������ ��� �ε����� ���� 0 ~ 15�� ����.
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
			setIcon(image);	// �������� �������ش�.
			changeStatus(1);	
		}
	}
	@Override
	public void CardOff() {
		/*
		 * ī�� �����Ⱑ �������ִ� ����, ī�忡 �̹����� ���� �ѷ��ִ� ���
		 * �̹����� Back�̹����� ��������.
		 * CARD_STATUS�� 2(������ �ִ»���)�� �ƴ� ��쿡�� Back �̹������� ��.
		 */
		setIcon(new ImageIcon("images/Front.png"));
		changeStatus(0); // ������ ���� ���·� �ǵ����ش�.
	}
	@Override
	public void changeStatus(int status) {
		// ������°� ������ �ִ� ���¶�� ���� �˷��ش�.
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
