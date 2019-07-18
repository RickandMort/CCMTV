package com.linlic.ccmtv.yx.activity.entity;

/**
 * NewEdit entity. @author MyEclipse Persistence Tools
 */

public class NewEdit implements java.io.Serializable {

	// Fields

	private Integer id; // id
	private Integer editNum; // 版本号
	private String editUrl; // 下载地址
	private String releaseTime; // 发布时间
	private String eidtIntroduce; // 版本介绍
	private String editName; // 版本名称

	// Constructors

	/** default constructor */
	public NewEdit() {
	}

	/** full constructor */
	public NewEdit(Integer editNum, String editUrl, String releaseTime,
			String eidtIntroduce, String editName) {
		this.editNum = editNum;
		this.editUrl = editUrl;
		this.releaseTime = releaseTime;
		this.eidtIntroduce = eidtIntroduce;
		this.editName = editName;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEditNum() {
		return this.editNum;
	}

	public void setEditNum(Integer editNum) {
		this.editNum = editNum;
	}

	public String getEditUrl() {
		return this.editUrl;
	}

	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}

	public String getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getEidtIntroduce() {
		return this.eidtIntroduce;
	}

	public void setEidtIntroduce(String eidtIntroduce) {
		this.eidtIntroduce = eidtIntroduce;
	}

	public String getEditName() {
		return this.editName;
	}

	public void setEditName(String editName) {
		this.editName = editName;
	}

}