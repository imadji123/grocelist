package com.imadji.grocelist;

/**
 * Created by imadji on 4/5/18.
 */

public class Grocery {
    private String name;
    private int amount;
    private String note;
    private boolean checked;

    public Grocery(String name, int amount, String note) {
        this.name = name;
        this.amount = amount;
        this.note = note;
        this.checked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
