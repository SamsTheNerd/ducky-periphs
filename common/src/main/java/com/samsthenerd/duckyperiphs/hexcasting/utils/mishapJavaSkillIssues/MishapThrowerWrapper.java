package com.samsthenerd.duckyperiphs.hexcasting.utils.mishapJavaSkillIssues;

import com.samsthenerd.duckyperiphs.hexcasting.utils.mishapJavaSkillIssues.JavaMishapThrower;

import at.petrak.hexcasting.api.casting.mishaps.Mishap;

/*
 * this way all the error squiggles are confined to just this file !
 */
public class MishapThrowerWrapper {
    public static void throwMishap(Mishap mishap){
        JavaMishapThrower.throwMishap(mishap);
    }
}
