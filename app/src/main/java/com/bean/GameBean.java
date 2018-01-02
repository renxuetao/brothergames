package com.bean;

import java.io.Serializable;

public class GameBean implements Serializable{

	public long GameID = 0;
	public String GameName = "";
	public String GameCover = "";
	public String Introduction = "";
	public String Url = "";
	public int IsVertical = 0;
	public int IsNew = 0;
	public long RecommendTime = 0;
	public int GameType = 0;//游戏类型 (0:h5游戏;)
	public long Playing = 0;
	public long Player = 0;

	@Override
	public String toString() {
		return "GameBean{" +
				"GameID=" + GameID +
				", GameName='" + GameName + '\'' +
				", GameCover='" + GameCover + '\'' +
				", Introduction='" + Introduction + '\'' +
				", Url='" + Url + '\'' +
				", IsVertical=" + IsVertical +
				", IsNew=" + IsNew +
				", RecommendTime=" + RecommendTime +
				", GameType=" + GameType +
				", Playing=" + Playing +
				", Player=" + Player +
				'}';
	}
}
