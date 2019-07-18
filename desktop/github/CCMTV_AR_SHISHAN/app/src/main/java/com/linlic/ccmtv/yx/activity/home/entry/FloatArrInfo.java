package com.linlic.ccmtv.yx.activity.home.entry;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class FloatArrInfo {
    /* "id":"1",
                "name":"耳鼻喉头颈外科",
                "letter":"E"*/
    private String id;
    private String name;
    private String letter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "FloatArrInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", letter='" + letter + '\'' +
                '}';
    }
}
