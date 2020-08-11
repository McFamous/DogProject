package com.example.dogproject.pojo;

import java.util.ArrayList;

public class Dogs {
    private String breed;
    private int countSubBreed;
    private ArrayList<String> subBreed;

    public Dogs(String breed, int countSubBreed, ArrayList<String> subBreed) {
        this.setBreed(breed);
        this.setCountSubBreed(countSubBreed);
        if(countSubBreed == 0)
            this.subBreed = null;
        else
            this.subBreed = subBreed;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getCountSubBreed() {
        return countSubBreed;
    }

    public void setCountSubBreed(int countSubBreed) {
        this.countSubBreed = countSubBreed;
    }

    public ArrayList<String> getSubBreed() {
        return subBreed;
    }

    public void setSubBreed(ArrayList<String> subBreed) {
        this.subBreed = subBreed;
    }
}
