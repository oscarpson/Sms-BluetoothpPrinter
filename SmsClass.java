package joslabs.kbssmsapp;

/**
 * Created by OSCAR on 7/18/2018.
 */

public class SmsClass {
    public String sender,Content,smstime;

    public SmsClass() {
    }

    public SmsClass(String sender, String content, String smstime) {
        this.sender = sender;
        Content = content;
        this.smstime = smstime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSmstime() {
        return smstime;
    }

    public void setSmstime(String smstime) {
        this.smstime = smstime;
    }
}
