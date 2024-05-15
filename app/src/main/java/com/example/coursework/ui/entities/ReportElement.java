package com.example.coursework.ui.entities;

public class ReportElement {
    private String name;
    private double revenue;
    private double profit;
    private int count;
    private boolean isProduction;

    public ReportElement(String name, double revenue, double profit, int count, boolean isProduction) {
        this.name = name;
        this.revenue = revenue;
        this.profit = profit;
        this.count = count;
        this.isProduction = isProduction;
    }

    public ReportElement(String name, int count) {
        this.name = name;
        this.revenue = 0;
        this.profit = 0;
        this.count = count;
        this.isProduction = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isProduction() {
        return isProduction;
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }
}
