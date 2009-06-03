package com.wtf.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Command;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Config {
  private static Element _config = null;
  private static String _file_name;
  private static Command _after_config_load;

  public static List<String> getAddSelectors() {
    List<String> ret = new LinkedList<String>();
    if (_config == null)
      return ret;
    NodeList select_tags = _config.getElementsByTagName("add_root");
    for (int i = 0; i < select_tags.getLength(); i++) {
      Element elem = (Element) select_tags.item(i);
      if (elem.getFirstChild() != null) {
        String selector = elem.getFirstChild().getNodeValue();
        ret.add(selector);
        Debug.log("add_root: " + selector);
      }
    }
    return ret;
  }

  public static List<String> getExcludeSelectors() {
    List<String> ret = new LinkedList<String>();
    if (_config == null)
      return ret;
    NodeList select_tags = _config.getElementsByTagName("exclude_root");
    for (int i = 0; i < select_tags.getLength(); i++) {
      Element elem = (Element) select_tags.item(i);
      if (elem.getFirstChild() != null) {
        String selector = elem.getFirstChild().getNodeValue();
        ret.add(selector);
        Debug.log("exclude_root: " + selector);
      }
    }
    return ret;
  }

  public static int getOptionInt(String name, int def) {
    String v = getOption(name);
    if (v == "")
      return def;
    try {
      int val = Integer.parseInt(v);
      return val;
    } catch (NumberFormatException e) {
      return def;
    }
  }

  public static String getOptionString(String name, HashSet<String> possible,
      String def) {
    String val = getOptionString(name, def);
    if (possible.contains(val))
      return val;
    return def;
  }

  public static String getOptionString(String name, String def) {
    String val = getOption(name);
    if (val == "")
      return "";
    return val;
  }

  public static void init(Command after_config_load) {
    if (!readParams())
      return;
    _after_config_load = after_config_load;
    RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
        _file_name);
    try {
      requestBuilder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable e) {
          Debug.log(e.getMessage());
          StatusBar.setError(e.getMessage());
        }

        public void onResponseReceived(Request request, Response response) {
          if (response.getStatusCode() == 200) {
            try {
              _config = XMLParser.parse(response.getText()).getDocumentElement();
              _after_config_load.execute();
            } catch (DOMParseException e) {
              Debug.log("XML syntax error");
              StatusBar.setError("XML syntax error");
            }
          } else {
            if (response.getStatusCode() == 404) {
              Debug.log(_file_name + " does not exist");
              StatusBar.setError("no '" + _file_name + "' file");
            } else {
              Debug.log("ERROR " + response.getStatusCode());
              StatusBar.setError("ERROR " + response.getStatusCode());
            }
          }
        }
      });
    } catch (Throwable e) {
      Debug.log(e.getMessage());
      StatusBar.setError(e.getMessage());
    }
  }

  private static String getOption(String name) {
    if (_config == null)
      return "";
    NodeList options = _config.getElementsByTagName("option");
    for (int i = options.getLength() - 1; i >= 0; i--) {
      NamedNodeMap attrs = options.item(i).getAttributes();
      Node an = attrs.getNamedItem("name");

      if (an == null || !an.toString().equals(name))
        continue;
      Node av = attrs.getNamedItem("value");
      if (av == null)
        continue;
      return av.toString();
    }
    return "";
  }

  private static boolean readParams() {
    Dictionary params = null;
    try {
      params = Dictionary.getDictionary("wtf_params");
    } catch (java.util.MissingResourceException e) {
      StatusBar.setError("wtf_params not defined");
      return false;
    }

    try {
      _file_name = params.get("config_path");
    } catch (java.util.MissingResourceException e) {
      StatusBar.setError("config_path not defined");
      return false;
    }

    try {
      String orientation = params.get("statusbar_orientation");
      if (orientation.equals("left") || orientation.equals("right")) {
        StatusBar.setOrientation(orientation);
      } else {
        StatusBar.setOrientation("left");
      }
    } catch (java.util.MissingResourceException e) {
      StatusBar.setOrientation("left");
    }

    return true;
  }
}
