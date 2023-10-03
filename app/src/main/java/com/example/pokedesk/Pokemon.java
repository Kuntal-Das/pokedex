package com.example.pokedesk;

public class Pokemon {
    private int number;
    private String name;
    private  String url;

    Pokemon(int number, String name, String url){
        this.number = number;
        this.name = name;
        this.url = url;
    }
    public int getNumber() { return number; }
    public String getName() {
        return name;
    }
    public String getUrl() { return url; }
}
