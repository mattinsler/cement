package com.mattinsler.cement.serializer;

import com.mattinsler.cement.AbstractCementFormatSerializer;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/10/10
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class JacksonFormatSerializer extends AbstractCementFormatSerializer {
    private final ObjectMapper _objectMapper = new ObjectMapper();

    public JacksonFormatSerializer() {
        super("json");
    }

    @Override
    protected <T> String serialize(T value) {
        try {
            _objectMapper.writeValue(getOutputStream(), value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "text/plain";
    }
}
