package com.mattinsler.cement.routing;

import com.google.inject.Injector;
import com.mattinsler.cement.annotation.*;
import com.mattinsler.cement.util.CollectionUtil;
import com.mattinsler.cement.util.Function;
import com.mattinsler.contract.IsContract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 28, 2010
 * Time: 1:51:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class CementMethod {
    public static class PathToken {
        public boolean isParameter;
        public String name;
    }

    private static final Function<Boolean, PathToken> IsPathTokenAParameter = new Function<Boolean, PathToken>() {
        public Boolean execute(PathToken argument) {
            return argument.isParameter;
        }
    };

    public Method method;
    public CementMethodType type;
    public String defaultResponseFormat;
    public Class<? extends IsContract> responseContractType;
    public Class<? extends IsContract> errorContractType;

    public Map<String, CementParameter<?>> parameters;

    public List<CementParameter<?>> injectableParameters;

    public List<PathToken> pathTokens;
    public Map<String, CementParameter<?>> pathParameters;

    private CementMethod() {
    }

    private static class IsPathToken implements Function<Boolean, PathToken> {
        private final String _name;
        public IsPathToken(String name) {
            _name = name;
        }
        public Boolean execute(PathToken argument) {
            return _name.equals(argument.name);
        }
    }

    private static final Set<Method> excludedMethods = new HashSet<Method>(Arrays.asList(Annotation.class.getDeclaredMethods()));
    private static Map<String, Object> getOptions(Annotation annotation) {
        Map<String, Object> options = new HashMap<String, Object>();

        for (Method method : annotation.annotationType().getMethods()) {
            if (!excludedMethods.contains(method)) {
                try {
                    options.put(method.getName(), method.invoke(annotation));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return options;
    }

    private static Map<CementMethodType, Map<String, Object>> getTypes(Method method) {
        Map<CementMethodType, Map<String, Object>> types = new HashMap<CementMethodType, Map<String, Object>>();

        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (Delete.class.equals(type)) {
                types.put(CementMethodType.Delete, getOptions(annotation));
            } else if (Get.class.equals(type)) {
                types.put(CementMethodType.Get, getOptions(annotation));
            } else if (Head.class.equals(type)) {
                types.put(CementMethodType.Head, getOptions(annotation));
            } else if (Options.class.equals(type)) {
                types.put(CementMethodType.Options, getOptions(annotation));
            } else if (Post.class.equals(type)) {
                types.put(CementMethodType.Post, getOptions(annotation));
            } else if (Put.class.equals(type)) {
                types.put(CementMethodType.Put, getOptions(annotation));
            } else if (Trace.class.equals(type)) {
                types.put(CementMethodType.Trace, getOptions(annotation));
            }
        }
        return types;
    }

    public static List<CementMethod> create(Method classMethod, Injector injector) {
        Map<CementMethodType, Map<String, Object>> methodTypes = getTypes(classMethod);
        if (methodTypes.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        Class<? extends IsContract> responseContractType = null;
        Class<? extends IsContract> errorContractType = null;
        Map<String, CementParameter<?>> parameters = new HashMap<String, CementParameter<?>>();
        List<CementParameter<?>> injectableParameters = new ArrayList<CementParameter<?>>();

        List<PathToken> pathTokens = new ArrayList<PathToken>();
        Map<String, CementParameter<?>> pathParameters = new HashMap<String, CementParameter<?>>();

        PathMapping pathMapping = classMethod.getAnnotation(PathMapping.class);
        if (pathMapping != null) {
            if (!pathMapping.value().startsWith("/")) {
                throw new RuntimeException("[" + classMethod + "] @PathMapping must begin with /");
            }
            for (String element : pathMapping.value().substring(1).split("/")) {
                PathToken token = new PathToken();
                if (element.startsWith("${") && element.endsWith("}")) {
                    token.isParameter = true;
                    token.name = element.substring(2, element.length() - 1);
                } else {
                    token.name = element;
                }
                pathTokens.add(token);
            }
        }

        Class<?>[] parameterTypes = classMethod.getParameterTypes();
        Annotation[][] parameterAnnotations = classMethod.getParameterAnnotations();

        for (int x = 0; x < parameterTypes.length; ++x) {
            CementParameter p = CementParameter.create(x, parameterTypes[x], parameterAnnotations[x]);
            if (p.bindingKey != null) {
                injectableParameters.add(p);
            } else if (CollectionUtil.contains(pathTokens, new IsPathToken(p.name))) {
                pathParameters.put(p.name, p);
            } else {
                parameters.put(p.name, p);
            }
        }

        if (pathParameters.size() != CollectionUtil.count(pathTokens, IsPathTokenAParameter)) {
            throw new RuntimeException("[" + classMethod + "] All parameters in @PathMapping must have a corresponding @Parameter");
        }

        Contract contractAnnotation = classMethod.getAnnotation(Contract.class);
        if (contractAnnotation != null) {
            responseContractType = contractAnnotation.value();
            errorContractType = contractAnnotation.error();
        }

        classMethod.setAccessible(true);

        List<CementMethod> methods = new ArrayList<CementMethod>();
        for (Map.Entry<CementMethodType, Map<String, Object>> entry : methodTypes.entrySet()) {
            CementMethod method = new CementMethod();
            method.method = classMethod;
            method.type = entry.getKey();
            method.defaultResponseFormat = (String)entry.getValue().get("defaultFormat");
            method.responseContractType = responseContractType;
            method.errorContractType = errorContractType;
            method.parameters = parameters;
            method.injectableParameters = injectableParameters;
            method.pathTokens = pathTokens;
            method.pathParameters = pathParameters;
            methods.add(method);
        }
        return methods;
    }
}
