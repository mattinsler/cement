package com.mattinsler.cement.contract;

import com.mattinsler.cement.IsDataContract;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionFieldAccessorFactory implements DataContractFieldAccessorFactory {
    private enum State {
        Lower,
        Upper,
        Digit,
        Separator
    }
    private State getState(char c) {
        if (Character.isUpperCase(c)) {
            return State.Upper;
        } else if (Character.isLowerCase(c)) {
            return State.Lower;
        } else if (Character.isDigit(c)) {
            return State.Digit;
        } else {
            return State.Separator;
        }
    }
    private String normalize(String value) {
        StringBuilder normalized = new StringBuilder(value.length());
        State state = getState(value.charAt(0));

        for (int x = 0; x < value.length(); ++x) {
            char c = value.charAt(x);
            State newState = getState(c);

            if (State.Lower == state && State.Upper == newState) {
                normalized.append("_");
            }

            if (State.Upper == newState) {
                normalized.append(Character.toLowerCase(c));
            } else if (State.Separator == newState) {
                normalized.append("_");
            } else {
                normalized.append(c);
            }
            state = newState;
        }
        return normalized.toString();
    }

    private int longestCommonSubstring(String from, String to) {
        if (from.isEmpty() || to.isEmpty()) {
            return 0;
        }

        int[][] compareTable = new int[from.length()][to.length()];
        int maxLen = 0;

        for (int m = 0; m < from.length(); m++) {
            for (int n = 0; n < to.length(); n++) {
                compareTable[m][n] = (from.charAt(m) != to.charAt(n)) ? 0
                        : (((m == 0) || (n == 0)) ? 1
                        : compareTable[m - 1][n - 1] + 1);
                maxLen = (compareTable[m][n] > maxLen) ? compareTable[m][n]
                        : maxLen;
            }
        }
        return maxLen;
    }

    private static class MethodAccessor implements DataContractFieldAccessor {
        private final Method _method;
        private final String _contractFieldName;
        private final DataContractSerializer _serializer;
        public MethodAccessor(Field contractField, Method method) {
            this(contractField, method, null);
        }
        public MethodAccessor(Field contractField, Method method, DataContractSerializer serializer) {
            _contractFieldName = contractField.getName();
            _method = method;
            _method.setAccessible(true);
            _serializer = serializer;
        }
        public String getName() {
            return _contractFieldName;
        }
        public Object getValue(Object object) {
            try {
                return _method.invoke(object);
            } catch (Exception e) {
                return null;
            }
        }
    }
    private static class FieldAccessor implements DataContractFieldAccessor {
        private final Field _field;
        private final String _contractFieldName;
        public FieldAccessor(Field contractField, Field field) {
            _contractFieldName = contractField.getName();
            _field = field;
            _field.setAccessible(true);
        }
        public String getName() {
            return _contractFieldName;
        }
        public Object getValue(Object object) {
            try {
                return _field.get(object);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private DataContractFieldAccessor getAccessorFromMethod(Class<?> valueType, Field contractField, DataContractSerializationContext context) {
        String normalizedContractFieldName = normalize(contractField.getName());
        for (Method method : valueType.getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                String normalizedMethodName = normalize(method.getName().substring(3));
                if (normalizedContractFieldName.equals(normalizedMethodName)) {
                    if (contractField.getType().equals(method.getReturnType())) {
                        return new MethodAccessor(contractField, method);
                    } else if (IsDataContract.class.isAssignableFrom(contractField.getType())) {
                        DataContractSerializer s = context.getSerializer(method.getReturnType(), (Class<? extends IsDataContract>)contractField.getType());

                    }
                }
            }
        }
        return null;
    }
    private DataContractFieldAccessor getAccessorFromField(Class<?> valueType, Field contractField, DataContractSerializationContext context) {
        String normalizedContractFieldName = normalize(contractField.getName());
        for (Field field : valueType.getDeclaredFields()) {
            String normalizedFieldName = normalize(field.getName());
            if (normalizedContractFieldName.equals(normalizedFieldName) && contractField.getType().equals(field.getType())) {
                return new FieldAccessor(contractField, field);
            }
        }
        return null;
    }
    private DataContractFieldAccessor getAccessorFromTypes(Class<?> valueType, Field contractField) {
        String normalizedContractFieldName = normalize(contractField.getName());
        SortedMap<Integer, Field> fieldScoreMap = new TreeMap<Integer, Field>();
        SortedMap<Integer, Method> methodScoreMap = new TreeMap<Integer, Method>();

        for (Method method : valueType.getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                if (contractField.getType().equals(method.getReturnType())) {
                    String normalizedMethodName = normalize(method.getName().substring(3));

                    methodScoreMap.put(Math.min(
                            normalizedContractFieldName.length() - Math.max(
                                    longestCommonSubstring(normalizedContractFieldName, normalizedMethodName),
                                    longestCommonSubstring(normalizedMethodName, normalizedContractFieldName)
                            ),
                            Math.min(
                                StringUtils.getLevenshteinDistance(normalizedContractFieldName, normalizedMethodName),
                                StringUtils.getLevenshteinDistance(normalizedMethodName, normalizedContractFieldName)
                            )
                    ), method);
                }
            }
        }
        for (Field field : valueType.getDeclaredFields()) {
            String normalizedFieldName = normalize(field.getName());
            if (contractField.getType().equals(field.getType())) {
                fieldScoreMap.put(Math.min(
                        normalizedContractFieldName.length() - Math.max(
                                longestCommonSubstring(normalizedContractFieldName, normalizedFieldName),
                                longestCommonSubstring(normalizedFieldName, normalizedContractFieldName)
                        ),
                        Math.min(
                                StringUtils.getLevenshteinDistance(normalizedContractFieldName, normalizedFieldName),
                                StringUtils.getLevenshteinDistance(normalizedFieldName, normalizedContractFieldName)
                        )
                ), field);
            }
        }

        Map.Entry<Integer, Field> bestField = null;
        Map.Entry<Integer, Method> bestMethod = null;
        if (fieldScoreMap.size() > 0 && fieldScoreMap.firstKey() < normalizedContractFieldName.length() / 2) {
            bestField = fieldScoreMap.entrySet().iterator().next();
        }
        if (methodScoreMap.size() > 0 && methodScoreMap.firstKey() < normalizedContractFieldName.length() / 2) {
            bestMethod = methodScoreMap.entrySet().iterator().next();
        }

        if (bestField == null && bestMethod == null) {
            return null;
        } else if (bestField == null) {
            return new MethodAccessor(contractField, bestMethod.getValue());
        } else if (bestMethod == null) {
            return new FieldAccessor(contractField, bestField.getValue());
        } else if (bestMethod.getKey() <= bestField.getKey()) {
            return new MethodAccessor(contractField, bestMethod.getValue());
        } else {
            return new FieldAccessor(contractField, bestField.getValue());
        }
    }

    public List<DataContractFieldAccessor> createAccessors(Class<?> valueType, Class<? extends IsDataContract> contractType, DataContractSerializationContext context) {
        List<DataContractFieldAccessor> accessors = new LinkedList<DataContractFieldAccessor>();

//        Set<Field> contractFields = new HashSet<Field>(Arrays.asList(contractType.getDeclaredFields()));
//        Iterator<Field> iterator = contractFields.iterator();
//        while (iterator.hasNext()) {
//            DataContractFieldAccessor accessor = getAccessorFromMethod(valueType, iterator.next());
//            if (accessor != null) {
//                accessors.add(accessor);
//                iterator.remove();
//            }
//        }
//        iterator = contractFields.iterator();
//        while (iterator.hasNext()) {
//            DataContractFieldAccessor accessor = getAccessorFromField(valueType, iterator.next());
//            if (accessor != null) {
//                accessors.add(accessor);
//                iterator.remove();
//            }
//        }
//        iterator = contractFields.iterator();
//        while (iterator.hasNext()) {
//            DataContractFieldAccessor accessor = getAccessorFromTypes(valueType, iterator.next());
//            if (accessor != null) {
//                accessors.add(accessor);
//                iterator.remove();
//            }
//        }

        return accessors;
    }
}
