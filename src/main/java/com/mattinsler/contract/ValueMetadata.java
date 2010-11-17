package com.mattinsler.contract;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/5/10
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValueMetadata {
    public interface Option {
        void recognize(Method method, ValueMetadata metadata);
    }

    private final Map<Class<?>, Option> _options = new HashMap<Class<?>, Option>();

    public <OptionType extends Option> void putOption(OptionType option) {
        _options.put(option.getClass(), option);
    }

    public <OptionType extends Option> OptionType getOption(Class<OptionType> optionType) {
        return (OptionType)_options.get(optionType);
    }

    public <OptionType extends Option> boolean hasOption(Class<OptionType> optionType) {
        return _options.containsKey(optionType);
    }
}
