package com.example.soccer_league_fixture_determiner.model;

public class Fixture {
    String insideTeam;
    String outsideTeam;

    public Fixture(String insideTeam, String outsideTeam) {
        this.insideTeam = insideTeam;
        this.outsideTeam = outsideTeam;
    }

    public String getInsideTeam() {
        return insideTeam;
    }

    public void setInsideTeam(String insideTeam) {
        this.insideTeam = insideTeam;
    }

    public String getOutsideTeam() {
        return outsideTeam;
    }

    public void setOutsideTeam(String outsideTeam) {
        this.outsideTeam = outsideTeam;
    }
}
