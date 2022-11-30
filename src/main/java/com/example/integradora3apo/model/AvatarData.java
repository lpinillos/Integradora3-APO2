package com.example.integradora3apo.model;

public class AvatarData {

    private static AvatarData instance;
    private String namesPlayer1;
    private String namesPlayer2;



    private AvatarData(){
     namesPlayer1 = "Esotilin";
     namesPlayer2 = "Esotilin";
    }

    public String getNamesPlayer1() {
        return namesPlayer1;
    }

    public void setNamesPlayer1(String namesPlayer1) {
        this.namesPlayer1 = namesPlayer1;
    }

    public String getNamesPlayer2() {
        return namesPlayer2;
    }

    public void setNamesPlayer2(String namesPlayer2) {
        this.namesPlayer2 = namesPlayer2;
    }

    public static AvatarData getInstance(){
        if(instance == null){
            instance = new AvatarData();
        }
        return instance;
    }
}
