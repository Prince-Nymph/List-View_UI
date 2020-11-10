package com.example.myapplication;

public class newsItem {
    public String Title;
    public String text;
    public String Topic;

    public newsItem() {
        super();
    }

    public newsItem(String title, String text, String topic) {
        Title = title;
        this.text = text;
        Topic = topic;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    @Override
    public String toString() {
        return "newsItem{" +
                "Title='" + Title + '\'' +
                ", text='" + text + '\'' +
                ", Topic='" + Topic + '\'' +
                '}';
    }
}
