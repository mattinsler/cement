package com.mattinsler.cement.routing;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mattinsler.cement.CementRequestParameters;
import com.mattinsler.cement.exception.CementNotFoundException;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 28, 2010
 * Time: 12:11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class CementMethodRouter {
    private static class PathNode {
        public CementParameter<?> parameter;
        public PathNode parameterChild;
        public Map<String, PathNode> literalChildren = new HashMap<String, PathNode>();
        public ParameterNode parameterRoot;
    }
    private static class ParameterNode {
        public CementParameter<?> parameter;
        public Map<String, ParameterNode> children = new HashMap<String, ParameterNode>();
        public CementMethod method;
    }

    public Map<CementMethodType, PathNode> pathRoots = new HashMap<CementMethodType, PathNode>();
    public List<CementMethod> methods = new ArrayList<CementMethod>();

    public final Class<?> cementType;
    public final String urlPattern;

    private Injector _injector;

    public CementMethodRouter(Class<?> cementType, String urlPattern) {
        this.cementType = cementType;
        this.urlPattern = urlPattern;
    }

    @Inject
    void initialize(Injector injector) {
        _injector = injector;

        for (Method classMethod : this.cementType.getDeclaredMethods()) {
            for (CementMethod method : CementMethod.create(classMethod, injector)) {
                this.methods.add(method);

                PathNode pathNode = this.pathRoots.get(method.type);
                if (pathNode == null) {
                    pathNode = new PathNode();
                    this.pathRoots.put(method.type, pathNode);
                }

                for (CementMethod.PathToken token : method.pathTokens) {
                    PathNode child;
                    if (token.isParameter) {
                        CementParameter<?> p = method.pathParameters.get(token.name);
                        if (pathNode.parameterChild != null) {
                            if (!p.equals(pathNode.parameterChild.parameter)) {
                                throw new RuntimeException("[" + classMethod + "] Cannot have more than one path parameters in the same position that don't match");
                            }
                            child = pathNode.parameterChild;
                        } else {
                            child = new PathNode();
                            child.parameter = p;
                            pathNode.parameterChild = child;
                        }
                    } else {
                        child = pathNode.literalChildren.get(token.name);
                        if (child == null) {
                            child = new PathNode();
                            pathNode.literalChildren.put(token.name, child);
                        }
                    }
                    pathNode = child;
                }

                ParameterNode parameterNode = pathNode.parameterRoot;
                if (parameterNode == null) {
                    parameterNode = new ParameterNode();
                    pathNode.parameterRoot = parameterNode;
                }

                try {
                    createParameterTree(parameterNode, method.parameters.keySet(), method);
                } catch (RuntimeException e) {
                    throw new RuntimeException("[" + classMethod + "]" + e.getMessage());
                }
            }
        }
    }

    private static void createParameterTree(ParameterNode node, Set<String> parameters, CementMethod method) {
        if (parameters.size() == 0) {
            if (node.method != null) {
                throw new RuntimeException("Duplicate parameter set found");
            }
            node.method = method;
            return;
        }

        for (String p : parameters) {
            ParameterNode child = node.children.get(p);
            if (child == null) {
                child = new ParameterNode();
                child.parameter = method.parameters.get(p);
                node.children.put(p, child);
            }
            Set<String> nextParameters = new HashSet<String>(parameters);
            nextParameters.remove(p);
            createParameterTree(child, nextParameters, method);
        }
    }

    public CementExecutableMethod route(CementMethodType type, String path, CementRequestParameters parameters) throws CementNotFoundException {
        String[] pathElements = path == null ? new String[0] : path.substring(1).split("/");

        CementExecutableParameter[] executableParameters = new CementExecutableParameter[16];

        PathNode pathNode = pathRoots.get(type);
        for (String element : pathElements) {
            PathNode child = pathNode.literalChildren.get(element);
            if (child == null) {
                child = pathNode.parameterChild;
                if (child == null) {
                    throw new CementNotFoundException();
                }
                executableParameters[child.parameter.index] = new CementBasicExecutableParameter(child.parameter, element);
            }
            pathNode = child;
        }

        ParameterNode parameterNode = pathNode.parameterRoot;
        for (String parameterName : parameters.parameterNames()) {
            parameterNode = parameterNode.children.get(parameterName);
            if (parameterNode == null) {
                throw new RuntimeException("No matching method because of parameters");
            }
            executableParameters[parameterNode.parameter.index] = new CementBasicExecutableParameter(parameterNode.parameter, parameters.get(parameterName));
        }

        for (CementParameter<?> p : parameterNode.method.injectableParameters) {
            executableParameters[p.index] = new CementInjectedExecutableParameter(p, _injector);
        }

        return new CementExecutableMethod(parameterNode.method, executableParameters);
    }
}
