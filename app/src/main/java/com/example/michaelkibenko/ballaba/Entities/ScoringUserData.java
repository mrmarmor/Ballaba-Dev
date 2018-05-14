package com.example.michaelkibenko.ballaba.Entities;

public class ScoringUserData  {
    public ScoringUserData() {
    }

    public ScoringUserData setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public ScoringUserData setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
        return this;
    }

    public ScoringUserData setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
        return this;
    }

    public ScoringUserData setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
        return this;
    }

    public ScoringUserData setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public ScoringUserData setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
        return this;
    }

    public ScoringUserData setWorkSeniority(boolean workSeniority) {
        this.workSeniority = workSeniority;
        return this;
    }

    public ScoringUserData setNumOfChilds(int numOfChilds) {
        this.numOfChilds = numOfChilds;
        return this;
    }

    public ScoringUserData setUserIncome(int userIncome) {
        this.userIncome = userIncome;
        return this;
    }

    private String birthDate , familyStatus , workStatus ,workEmail , userEmail ;
    private boolean hasCar , workSeniority;
    private int numOfChilds , userIncome;

    public ScoringUserData(String birthDate, String familyStatus, String workStatus, boolean workSeniority, String workEmail, String userEmail, boolean hasCar, int numOfChilds, int userIncome) {
        this.birthDate = birthDate;
        this.familyStatus = familyStatus;
        this.workStatus = workStatus;
        this.workSeniority = workSeniority;
        this.workEmail = workEmail;
        this.userEmail = userEmail;
        this.hasCar = hasCar;
        this.numOfChilds = numOfChilds;
        this.userIncome = userIncome;


    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public boolean getWorkSeniority() {
        return workSeniority;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isHasCar() {
        return hasCar;
    }

    public int getNumOfChilds() {
        return numOfChilds;
    }

    public int getUserIncome() {
        return userIncome;
    }
}

