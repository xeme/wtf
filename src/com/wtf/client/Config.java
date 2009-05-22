package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class Config {
	private static Element _config = null;
	private static String _file_name;

	public static void init(String file_name) {
		_file_name = file_name;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, file_name);
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable e) {
					Debug.log(e.getMessage());
					StatusBar.setError(e.getMessage());
				}
				public void onResponseReceived(Request request, Response response) {
					if(response.getStatusCode() == 200) {
						try {
							_config = XMLParser.parse(response.getText()).getDocumentElement();
						} catch(Throwable e) {
							Debug.log("XML syntax error");
							StatusBar.setError("XML syntax error");
						}
					} else {
						if(response.getStatusCode() == 404) {
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

	public static List<String> getAddSelectors() {
		List<String> ret = new  LinkedList<String>();
		if(_config == null)
			return ret;
		NodeList select_tags = _config.getElementsByTagName("add_root");
		for(int i = 0; i < select_tags.getLength(); i++) {
			Element elem = (Element)select_tags.item(i);
			if(elem.getFirstChild() != null) {
				String selector = elem.getFirstChild().getNodeValue();
				ret.add(selector);
				Debug.log("add_root: " + selector);
			}
		}
		return ret;
	}
	
	public static List<String> getExcludeSelectors() {
		List<String> ret = new  LinkedList<String>();
		if(_config == null)
			return ret;
		NodeList select_tags = _config.getElementsByTagName("exclude_root");
		for(int i = 0; i < select_tags.getLength(); i++) {
			Element elem = (Element)select_tags.item(i);
			if(elem.getFirstChild() != null) {
				String selector = elem.getFirstChild().getNodeValue();
				ret.add(selector);
				Debug.log("exclude_root: " + selector);
			}
		}
		return ret;
	}
}
