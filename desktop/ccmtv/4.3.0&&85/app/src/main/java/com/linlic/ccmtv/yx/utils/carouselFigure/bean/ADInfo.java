package com.linlic.ccmtv.yx.utils.carouselFigure.bean;


/**
 * 描述：广告信息</br>
 */
public class ADInfo {

	String aid = "";
	String title = "";
	String picurl = "";
	boolean IsActivity;

	public void setAid(String aid) {
		this.aid = aid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public void setActivity(boolean activity) {
		IsActivity = activity;
	}

	public String getAid() {
		return aid;
	}

	public String getTitle() {
		return title;
	}

	public String getPicurl() {
		return picurl;
	}

	public boolean isActivity() {
		return IsActivity;
	}

	@Override
	public String toString() {
		return "ADInfo{" +
				"aid='" + aid + '\'' +
				", title='" + title + '\'' +
				", picurl='" + picurl + '\'' +
				", IsActivity=" + IsActivity +
				'}';
	}
}
