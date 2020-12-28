package com.company.entity;

import java.io.Serializable;
import java.util.Date;

public class DateMessage implements Serializable {

    private Date date;
    private String message;

    public DateMessage(Date date, String message) {
        this.date = date;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return date + " -> '" + message + "'";
    }
}