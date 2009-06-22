package com.wtf.client;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;

public class Config {
  private static Dictionary _options = null;

  public static void init() {
    try {
      _options = Dictionary.getDictionary("wtf_params");
    } catch (java.util.MissingResourceException e) {
      //There is no status bar
      Window.alert("[WTF] ERROR: wtf_params not defined");
      //block WTF
      StatusBar.setError("");
      return;
    }
  }

  public static int getOptionInt(String name, int def) {
    int ret;
    try {
      ret = Integer.parseInt(_options.get(name));
    } catch (java.util.MissingResourceException e) {
      return def;
    }
    catch (NumberFormatException e){
      return def;
    }
    return ret;
  }

  public static String getOptionString(String name, String[] possible, String def) {
    String opt = getOptionString(name, def);
    for(int i = 0; i < possible.length; i++) {
      if(possible[i].equals(opt))
        return opt;
    }
    return def;
  }

  public static String getOptionString(String name, String def) {
    try {
      return _options.get(name);
    } catch (java.util.MissingResourceException e) {
      return def;
    }
  }

  public static Boolean getOptionBoolean(String name, Boolean def) {
    try {
      String opt = _options.get(name);
      if(opt.toLowerCase().equals("true") || opt.equals("1"))
        return true;
      if(opt.toLowerCase().equals("false") || opt.equals("0"))
        return false;
      return def;
    } catch (java.util.MissingResourceException e) {
      return def;
    }
  }
}
