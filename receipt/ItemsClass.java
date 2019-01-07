package joslabs.kbssmsapp.receipt;

/**
 * Created by OSCAR on 7/20/2018.
 */

public class ItemsClass {
    String item,qty;
    int price,amount;

    public ItemsClass() {
    }

    public ItemsClass(String item, String qty, int price, int amount) {
        this.item = item;
        this.qty = qty;
        this.price = price;
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
