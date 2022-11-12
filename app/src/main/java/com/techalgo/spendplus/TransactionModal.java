package com.techalgo.spendplus;

public class TransactionModal {

    private double amt;
    private String type;
    private String date;
    private String note;
    private String ttype;
    private int id;

    public double getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionModal(int id, double amt, String type, String date, String note, String ttype) {
        this.amt = amt;
        this.type = type;
        this.date = date;
        this.note = note;
        this.ttype = ttype;
        this.id = id;
    }

}
