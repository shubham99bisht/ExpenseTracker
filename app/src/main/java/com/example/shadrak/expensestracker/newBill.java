package com.example.shadrak.expensestracker;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class newBill {

    private String BillId;
    private String Amount;
    private String Date;
    private String Place;
    private String Category;
    private String Vendor;
    private String Status = "Not Verified";
    private String Link = NULL;
    private String Items = NULL;

    public newBill(String billId,String amount, String date, String place, String vendor, String category, String status, String link, String items) {
        this.BillId = billId;
        this.Amount = amount;
        this.Date = date;
        this.Place = place;
        this.Vendor = vendor;
        this.Category = category;
        this.Status = status;
        this.Link = link;
        this.Items = items;
    }

    //Getter

    public  String getBillId(){ return BillId;}
    public String getVendor() {
        return Vendor;
    }

    public String getDate() {
        return Date;
    }

    public String getAmount() {
        return Amount;
    }

    public String getPlace() {
        return Place;
    }

    public String getCategory() { return Category; }

    public String getStatus() { return Status; }

    public String getLink() { return Link; }

    public String getItems() { return Items; }

    //Setter

    public void setBillId(String billId){ BillId = billId;}
    public void setVendor(String name) {
        Vendor = name;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setAmount(String cost) {
        Amount = cost;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public void setCategory(String category) { Category = category; }
}
