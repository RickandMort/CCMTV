package com.linlic.ccmtv.yx.activity.bigcase.entity;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseYearMonthEntrty {
    /**
     * case_id : ID
     * month : 月份
     * title : 标题
     */
    private String year;
    private String title;
    private String type;
    private String case_id;
    private String month;
    private String count;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCase_id() {
            return case_id;
        }

        public void setCase_id(String case_id) {
            this.case_id = case_id;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }


}
