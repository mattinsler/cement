package com.mattinsler.cement;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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
    private final Map<String, String> _parameters;

    public CementRequestParameters(HttpServletRequest request) {
        this(request.getParameterMap());
    }

    public CementRequestParameters(Map<String, String[]> parameters) {
        _parameters = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            _parameters.put(entry.getKey(), entry.getValue()[0]);
        }
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

    public String get(String key) {
        return _parameters.get(key);
    }

    public String put(String key, String value) {
        return _parameters.put(key, value);
    }

    public String remove(String key) {
        return _parameters.remove(key);
    }

    public void clear() {
        _parameters.clear();
    }

    public Set<String> parameterNames() {
        return _parameters.keySet();
    }
}
