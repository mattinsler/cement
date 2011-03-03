package com.mattinsler.cement;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/16/10
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementRequestParameters {
  private final Map<String, String[]> _parameters;

  private String nullOrFirstItem(String[] items) {
    return items == null || items.length == 0 ? null : items[0];
  }

  public CementRequestParameters(HttpServletRequest request) {
    this(new HashMap<String, String[]>(request.getParameterMap()));
  }

  public CementRequestParameters(Map<String, String[]> parameters) {
    _parameters = parameters;
  }

  public int size() {
    return _parameters.size();
  }

  public boolean isEmpty() {
    return _parameters.isEmpty();
  }

  public boolean contains(String key) {
    return _parameters.containsKey(key);
  }

  public boolean containsArray(String key) {
    return _parameters.containsKey(key + "[]");
  }

  public String get(String key) {
    return nullOrFirstItem(_parameters.get(key));
  }

  public String[] getArray(String key) {
    return _parameters.get(key + "[]");
  }

  public String getAndRemove(String key) {
    return nullOrFirstItem(_parameters.remove(key));
  }

  public String[] getAndRemoveArray(String key) {
    return _parameters.remove(key + "[]");
  }

  public Set<String> parameterNames() {
    return _parameters.keySet();
  }
}
