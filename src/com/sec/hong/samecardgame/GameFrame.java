package com.sec.hong.samecardgame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.sec.hong.samecardgame.DB.DBConnection;
import com.sec.hong.samecardgame.DB.DBQuery;

/** 
 * @author GiHwanHong
 * @Class GameFrame
 * @content JFrame�� �̿��ؼ� ������ UI�� ����� �κ�.
 */
public class GameFrame extends JFrame {
	int randImage[] = new int[16];
	int randNum1, randNum2, temp;
	GameCard[] card = new GameCard[16];
	JDialog viewRank = new JDialog(this,"���Ӱ��");
	String username;
	DBQuery db = DBQuery.getInstance();
	DBConnection dbConn = DBConnection.getInstance();
	Dimension s, s1;
	int x , y;
	
	public GameFrame(String username) {
		
		this.username = username;
		setResizable(false);
		setSize(600,600);
		setLayout(new BorderLayout());
		setTitle("���� ��ȭ ������ ã�� ���� | ������̸� : "+ username);
		
		// ������ ��ġ�� �߾� // 
		s = Toolkit.getDefaultToolkit().getScreenSize();
		s1 = getSize();
		
		x = ((int)s.getWidth() / 2) - ((int)s1.getWidth() / 2);
		y = ((int)s.getHeight() / 2) - ((int)s1.getHeight() / 2);
		setLocation(x, y);
		/*-----------------*/
		
		JPanel upperPane = new JPanel();
		upperPane.setLayout(new GridLayout(4, 4, 0, 0));
		upperPane.setVisible(true);
		
		for (int i = 0; i < card.length; i++) {
			card[i] = new GameCard();
			upperPane.add(card[i]);
		}
		
		for(int i = 0; i<randImage.length; i++) {
			randImage[i] = i;
			if(i > 6) {
				randImage[i] -= 7 ;
			}
		}
		
		// [0]=0,[1]=1,...,[15]=8
		// randImage���� 0~7������ 2�� ��.
		randImage[15]--;

		// shuffle algorithm
		Random random = new Random();
		for (int i = 0; i < 30; i++) {
			
			randNum2 = random.nextInt(16);
			temp = randImage[randNum1];
			
			randImage[randNum1] = randImage[randNum2];
			randImage[randNum2] = temp;
		}
		
		/*
		 * randNum1,2�� ������ ���ڸ� �޴´�.
		 * randImage�� ��Ư���� �ε������� �����ִ� �۾��� ������(30) �ݺ��ؼ�
		 * randImage�ȿ� ������ 0,1,2,..7,0,1,2,..7 ���� �ƴ� �������� ������ ���ڰ� ����,
		 * imgID�� ī�� ��ȣ�� �ٸ� ���� �Ѱ��ش�.
		 */
		
		CardListener cardListener = new CardListener();
		for (int i = 0; i < card.length; i++) {
			card[i].imgID = randImage[i];
			card[i].addActionListener(cardListener);
			card[i].myID = i;
			setVisible(true);
			add(upperPane);
		}
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	// ��� ǥ�ø� ���� â�� ����� ǥ���ϱ� ���� �޼ҵ�.
	public void setDialog(JDialog jD, int level, long gametime)  {
		int score;
		String rank = null;
		String levelName = null;
		score = (int) ((10 - (level / 2)) * (30 - gametime)); // ��� ������ ���� ������.
		
		if (score >= 110)
			rank = "S";
		else if (score >= 100)
			rank = "A+";
		else if (score >= 90)
			rank = "A";
		else if (score >= 80)
			rank = "B+";
		else if (score >= 70)
			rank = "B";
		else if (score >= 60)
			rank = "C+";
		else if (score >= 50)
			rank = "C";
		else
			rank = "F";
		
		switch (level) {
			case 2:
				levelName = "���";
				break;
			case 4:
				levelName = "�߱�";
				break;
			case 6:
				levelName = "�ʱ�";
				break;
		}
		Dialog(jD, rank ,levelName, score, gametime);
	}
	
	public void Dialog(JDialog jD, String rank, String levelName, int score, long gametime) {
		JPanel panel = new JPanel();
		JTextArea gameResult = new JTextArea();
		JButton viewBtn = new JButton("��������");
		
		gameResult.setText("");
		gameResult.setBounds(50, 50, 100,100);
		
		gameResult.append("RANK : " + rank + "\n");
		gameResult.append("���̵� : " + levelName + "\n");
		gameResult.append("���� ���� �ð� : " + gametime + "��\n");
		gameResult.setEditable(false);
		panel.add(gameResult);
		panel.add(viewBtn);
		//db.Insert(username,levelName,rank,score,gametime);
		jD.add(panel);
		viewBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==viewBtn) {
					ViewRank();
				}
			}
		});
	}
	
	public String getUsername() {
		return username;
	}
	
	public void ViewRank() {
		JFrame frm = new JFrame("��������");
		
		JTable table = new JTable();
		JPanel tablepanel = new JPanel();
		JPanel btnpanel = new JPanel();
		
		String columnsName[] = {"username", "levelname", "rank", "score", "time"};
		JButton btn = new JButton("ToExcel");
		DefaultTableModel model = new DefaultTableModel();
		Object[] rowData = new Object[100];
		tablepanel.setLayout(new BorderLayout());
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String,String>();
		
		model.setColumnIdentifiers(columnsName);
		list = db.select();	
		
		for (int i = 0; i<list.size();i++) {
			map = list.get(i);
			rowData[0] = map.get("username");
			rowData[1] = map.get("levelname");
			rowData[2] = map.get("rank");
			rowData[3] = map.get("score");
			rowData[4] = map.get("time");
			
			model.addRow(rowData);
		}
		
		table.setModel(model);
		JScrollPane pane = new JScrollPane(table);
		
		btnpanel.add(btn);
		tablepanel.add(pane);
		tablepanel.setSize(400, 300);
		
		frm.setLayout(new BorderLayout());
		frm.setSize(400,400);
		
		frm.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		
		frm.add(btnpanel,BorderLayout.NORTH);
		frm.add(tablepanel,BorderLayout.CENTER);
		frm.setLocation(x, y);
		
		//frm.setContentPane(tablepanel);
		frm.setVisible(true);
	}
	
}
