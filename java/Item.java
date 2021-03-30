package com.example.myapplication;

public class Item {
    private String word;
    private String englishD;
    private String chineseD;

    public Item(String word, String englishD, String chineseD) {
        this.word = word;
        this.englishD = englishD;
        this.chineseD = chineseD;
    }

    public String getWord() {
        return word;
    }

    public String getEnglishD() {
        return englishD;
    }

    public String getChineseD() {
        return chineseD;
    }
}
