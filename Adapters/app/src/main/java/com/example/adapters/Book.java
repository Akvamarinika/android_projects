package com.example.adapters;


public class Book {
    String author;
    String name;
    int cover;

    public Book(String author, String name, int cover) {
        this.author = author;
        this.name = name;
        this.cover = cover;

        if (cover ==0){
            this.cover = R.drawable.book;
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;

        if (cover == 0){
            this.cover = R.drawable.book;
        }
    }
}
