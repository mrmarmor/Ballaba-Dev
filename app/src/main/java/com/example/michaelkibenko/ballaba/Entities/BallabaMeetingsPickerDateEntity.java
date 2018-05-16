package com.example.michaelkibenko.ballaba.Entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BallabaMeetingsPickerDateEntity extends BallabaBaseEntity {
    public String formattedDate;
    public ArrayList<BallabaMeetingDate> dates;
    public boolean edited;
    public Date currentDate;
    public Calendar calendar;
}
