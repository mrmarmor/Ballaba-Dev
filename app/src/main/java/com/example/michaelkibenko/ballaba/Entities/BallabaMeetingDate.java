package com.example.michaelkibenko.ballaba.Entities;

import java.util.Date;

public class BallabaMeetingDate extends BallabaBaseEntity {
    public Date from, to;
    public boolean edited;
    public int fromEditCounter, toEditCounter;
    public boolean isRepeat;
    public int numberOfRepeats;
    public boolean isPrivate;
}
