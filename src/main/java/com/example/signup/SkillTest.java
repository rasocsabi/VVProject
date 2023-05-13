package com.example.signup;

import org.junit.Test;

import static org.junit.Assert.*;

public class SkillTest {

    @Test
    public void testSkill() {
       {

            String username = "kis_pista";
            String skillName = "Programming";

            Skill skill = new Skill(username, skillName, 5);

            // Assert
            assertEquals(username, skill.getUsername());
            assertEquals(skillName, skill.getSkillName());
            assertEquals(1, skill.getSkillLevel());


       }
    }

    @Test
    public void testSettersAndGetters() {

        Skill skill = new Skill("kis_pista", "Programming", 5);

        skill.setUsername("mek_elek");
        skill.setSkillName("Debrecen");
        skill.setSkillLevel(3);

        assertEquals("mek_elek", skill.getUsername());
        assertEquals("Debrecen", skill.getSkillName());
        assertEquals(3, skill.getSkillLevel());
    }
}
