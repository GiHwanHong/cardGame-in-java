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
 * @content JFrame을 이용해서 게임의 UI를 만드는 부분.
 */
public class GameFrame extends JFrame {
	int randImage[] = new int[16];
	int randNum1, randNum2, temp;
	GameCard[] card = new GameCard[16];
	JDialog viewRank = new JDialog(this,"게임결과");
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
		setTitle("같은 영화 포스터 찾기 게임 | 사용자이름 : "+ username);
		
		// 프레임 위치를 중앙 // 
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
		// randImage에는 0~7까지가 2번 들어감.
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
		 * randNum1,2는 랜덤한 숫자를 받는다.
		 * randImage의 불특정한 인덱스끼리 섞어주는 작업을 여러번(30) 반복해서
		 * randImage안에 기존의 0,1,2,..7,0,1,2,..7 순이 아닌 무작위의 순서로 숫자가 들어가고,
		 * imgID에 카드 번호와 다른 값을 넘겨준다.
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
	// 결과 표시를 위한 창에 결과를 표시하기 위한 메소드.
	public void setDialog(JDialog jD, int level, long gametime)  {
		int score;
		String rank = null;
		String levelName = null;
		score = (int) ((10 - (level / 2)) * (30 - gametime)); // 결과 산출을 위한 점수식.
		
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
				levelName = "고급";
				break;
			case 4:
				levelName = "중급";
				break;
			case 6:
				levelName = "초급";
				break;
		}
		Dialog(jD, rank ,levelName, score, gametime);
	}
	
	public void Dialog(JDialog jD, String rank, String levelName, int score, long gametime) {
		JPanel panel = new JPanel();
		JTextArea gameResult = new JTextArea();
		JButton viewBtn = new JButton("순위보기");
		
		gameResult.setText("");
		gameResult.setBounds(50, 50, 100,100);
		
		gameResult.append("RANK : " + rank + "\n");
		gameResult.append("난이도 : " + levelName + "\n");
		gameResult.append("게임 진행 시간 : " + gametime + "초\n");
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
		JFrame frm = new JFrame("순위보기");
		
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
