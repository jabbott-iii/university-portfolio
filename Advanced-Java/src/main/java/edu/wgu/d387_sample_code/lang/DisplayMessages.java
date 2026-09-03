package edu.wgu.d387_sample_code.lang;

import java.util.Locale;
import java.util.ResourceBundle;

public class DisplayMessages {
    private Locale locale;
    private ResourceBundle bundle;

    public String getWelcomeMessages(){
        return bundle.getString("welcome");
    }

    public DisplayMessages(Locale locale) {
        this.locale = locale;
    }

    public DisplayMessages(String lang, String country){
        locale = new Locale(lang, country);
        bundle = ResourceBundle.getBundle("message", locale);
    }
}
