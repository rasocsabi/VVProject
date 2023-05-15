package com.example.signup;

public class Skill {
    private String username;
    private String skillName;
    private int skillLevel;

    public Skill(String skillName, int skillLevel) {


        this.skillName = skillName;
        this.skillLevel = skillLevel;

    }

    public Skill(String username, String skillName, int skillLevel) {
        this.username = username;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}