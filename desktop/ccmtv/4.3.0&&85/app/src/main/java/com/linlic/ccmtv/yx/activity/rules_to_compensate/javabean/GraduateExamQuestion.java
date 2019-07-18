package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import java.util.List;

/**
 * Created by yu on 2018/6/20.
 */

public class GraduateExamQuestion {
    private String questionContent;
    private String standardScore;
    private List<String> scoreList;
    private String deductionScore;
    private String deductionReason;
    private String dataType;
    private String disabled;
    private String marking;
    private String serialNumber;
    private String scoreVisible;//得分布局显示与否  0为不显示    1为显示
    private String deduct_marks_account;
    private int curr_pos;

    public int getCurr_pos() {
        return curr_pos;
    }

    public void setCurr_pos(int curr_pos) {
        this.curr_pos = curr_pos;
    }

    public String getDeduct_marks_account() {
        return deduct_marks_account;
    }

    public void setDeduct_marks_account(String deduct_marks_account) {
        this.deduct_marks_account = deduct_marks_account;
    }

    @Override
    public String toString() {
        return "GraduateExamQuestion{" +
                "questionContent='" + questionContent + '\'' +
                ", standardScore='" + standardScore + '\'' +
                ", scoreList=" + scoreList +
                ", deductionScore='" + deductionScore + '\'' +
                ", deductionReason='" + deductionReason + '\'' +
                ", dataType='" + dataType + '\'' +
                ", disabled='" + disabled + '\'' +
                ", marking='" + marking + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", scoreVisible='" + scoreVisible + '\'' +
                '}';
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getStandardScore() {
        return standardScore;
    }

    public void setStandardScore(String standardScore) {
        this.standardScore = standardScore;
    }

    public List<String> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<String> scoreList) {
        this.scoreList = scoreList;
    }

    public String getDeductionReason() {
        return deductionReason;
    }

    public void setDeductionReason(String deductionReason) {
        this.deductionReason = deductionReason;
    }

    public String getDeductionScore() {
        return deductionScore;
    }

    public void setDeductionScore(String deductionScore) {
        this.deductionScore = deductionScore;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getScoreVisible() {
        return scoreVisible;
    }

    public void setScoreVisible(String scoreVisible) {
        this.scoreVisible = scoreVisible;
    }
}
