package joslabs.kbssmsapp.SMSservice;

import joslabs.kbssmsapp.SmsClass;

/**
 * Created by OSCAR on 11/1/2016.
 */
public interface SmsListener {
    public void messageReceived(String messageText);
    void Smsclass(SmsClass sms);
}
