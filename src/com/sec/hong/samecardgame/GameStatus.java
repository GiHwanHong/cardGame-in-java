package com.sec.hong.samecardgame;

public interface GameStatus {
	public void initCard(GameCard c[]);
	public void oneTimeView(GameCard c[]);
	public boolean clearGame(GameCard c[]);
	public void reCard(GameCard c[]);
}
