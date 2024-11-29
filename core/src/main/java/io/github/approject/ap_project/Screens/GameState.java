package io.github.approject.ap_project.Screens;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {

    public int StateBirdIndex;
    public boolean laid_once;
    public ArrayList<Integer> bird_obj_numbers = new ArrayList<>();
    public ArrayList<Integer> pig_obj_numbers = new ArrayList<>();
    public ArrayList<Integer> mat_obj_numbers = new ArrayList<>();
    public ArrayList<Integer> egg_obj_numbers = new ArrayList<>();
    public ArrayList<BodyState> birds = new ArrayList<>();
    public ArrayList<BodyState> pigs = new ArrayList<>();
    public ArrayList<BodyState> materials = new ArrayList<>();
    public ArrayList<BodyState> eggs = new ArrayList<>(); // Added for eggs
}

