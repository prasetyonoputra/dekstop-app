package entities;

import java.util.Date;

public class Expense {
    private long id;
    private String description;
    private double amount;
    private Date createdDate;
    private Date updatedDate;

    public Expense(long id, String description, double amount, Date createdDate, Date updatedDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Expense(long id, String description, double amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }
}
