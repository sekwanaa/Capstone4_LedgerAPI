package org.yearup.models;

import java.math.BigDecimal;

public class Entry {
    private int entryId;
    private String description;
    private String vendor;
    private BigDecimal amount;


    public Entry(int entryId, String description, String vendor, BigDecimal amount) {
        this.entryId = entryId;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
