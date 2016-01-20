package jastaddad.api;

import jastaddad.api.nodeinfo.Attribute;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.api.nodeinfo.NodeInfoHolder;
import jastaddad.api.nodeinfo.Token;
import org.w3c.dom.Attr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class holds all information about the a node, all attribute values and tokens
 * It can invoke methods with the compute methods.
 * Created by gda10jli on 10/14/15.
 */
public class NodeContent {

    private HashMap<Method, NodeInfo> attributes;
    private HashMap<Method,NodeInfo> tokens;
    private HashMap<Method,NodeInfo> NTAs;
    private HashMap<Method, NodeInfo> computedMethods;
    private Node node; //The node the content is a part of
    private Object nodeObject; //The node the content is a part of
    private boolean hasComputedCachedNTAS = false;

    /**
     * Constructor for the NodeContent, which will init the HashSet/HashMap
     * @param node
     */
    public NodeContent(Node node){
        attributes = new HashMap<>();
        tokens = new HashMap<>();
        NTAs = new HashMap<>();
        computedMethods = new HashMap<>();
        this.node = node;
        this.nodeObject = node.node;
    }

    protected NodeInfo getComputed(Method method){
        return computedMethods.get(method);
    }


    /**
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
     * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
     * @param nodeInfo
     * @param par
     * @return true if the invocation was successful.
     */
    protected Object compute(NodeInfo nodeInfo, Object[] par, ASTAPI api) {
        Object[] params;
        Method method = nodeInfo.getMethod();
        if ((par != null && par.length != method.getParameterCount()) || (par == null && method.getParameterCount() != 0)) {
            api.putError(AlertMessage.INVOCATION_ERROR, "Wrong number of arguments for the method: " + method);
            return null;
        }
        if(par == null)
            params = new Object[method.getParameterCount()];
        else
            params = par;
        if(!nodeInfo.isAttribute()){
            api.putError(AlertMessage.INVOCATION_ERROR, "Can only do compute on attributes");
            return  null;
        }

        Attribute attribute = (Attribute) nodeInfo;
        if(attribute.containsValue(params))
            return attribute.getComputedValue(params);

        try{
            Object obj =  method.invoke(nodeObject, params);
            attribute.addComputedValue(params, obj);
            return obj;
        }catch(Throwable e){
            addInvocationErrors(api, e, attribute.getMethod());
            Object message = e.getCause() != null ? e.getCause() : e.getMessage();
            attribute.addComputedValue(params, message);
            return message;
        }
    }

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @return
     */
    protected void compute(ASTAPI api, boolean reComputeNode, boolean forceComputation){
        if(reComputeNode){
            NTAs.clear();
            attributes.clear();
            tokens.clear();
            computedMethods.clear();
        }
        compute(api, nodeObject, forceComputation);
    }

    /**
     * Computes all methods of the given object, the values will be added to NodeContents value Lists.
     * NOTE: it will only compute the methods with annotations of the ASTAnnotation type.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param obj
     */
    protected void compute(ASTAPI api, Object obj, boolean forceComputation){
        if(node.isNull())
            return;
        for(Method m : obj.getClass().getMethods()){
            compute(api, m, null, forceComputation);
        }
    }

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    protected NodeInfo computeMethod(ASTAPI api, String method){
        return computeMethod(api, method, false);
    }

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    protected NodeInfo computeMethod(ASTAPI api, String method, boolean forceComputation){
        try{
            Method m = nodeObject.getClass().getMethod(method);
            return compute(api, m, null, forceComputation);
        }  catch (NoSuchMethodException e) {
            //api.putError(ASTAPI.INVOCATION_ERROR, "No such Method : " + e.getCause());
        }
        return null;
    }

    /**
     * Computes the given method, and depending on the parameter @param add it to the list with the computed Attributes.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param m
     * @return
     */
    protected NodeInfo compute(ASTAPI api, Method m, Object[] params, boolean forceComputation){
        if(computedMethods.containsKey(m) && !forceComputation) {
            NodeInfo info = computedMethods.get(m);
            if(info != null && info.isAttribute())
                invokeAndSetValue((Attribute)info, api, node.node, m, params, forceComputation);
            return info;
        }
        NodeInfo info = null;
        for (Annotation a : m.getAnnotations()) {
            if (ASTAnnotation.isAttribute(a)) {
                info = computeAttribute(api, nodeObject, m, params, forceComputation);
                if(info.isNTA())
                    NTAs.put(m, info);
                else
                    attributes.put(m ,info);
                break;
            } else if (ASTAnnotation.isToken(a)) {
                info = computeToken(api, nodeObject, m);
                tokens.put(m, info);
                break;
            }
        }
        computedMethods.put(m, info);
        return info;
    }

    /**
     * Creates a Attribute and invokes the method with the supplied parameters, if any.
     * Will also add the specific information about the Attribute, which is derived form the annotations.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param obj
     * @param m
     * @param params
     * @param forceComputation
     * @return
     */
    protected Attribute computeAttribute(ASTAPI api, Object obj, Method m, Object[] params, boolean forceComputation){
        Attribute attribute = new Attribute(m.getName(), null, m);
        attribute.setParametrized(m.getParameterCount() > 0);
        for(Annotation a : m.getAnnotations()) { //To many attribute specific methods so I decided to iterate through the Annotations again instead of sending them as parameters.
            if (ASTAnnotation.isAttribute(a)){
                attribute.setKind(ASTAnnotation.getKind(a));
                attribute.setCircular(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_CIRCULAR));
                attribute.setNTA(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA));
            }else if(ASTAnnotation.isSource(a)){
                attribute.setAspect(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_ASPECT));
                attribute.setDeclaredAt(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_DECLARED_AT));
            }
        }
        invokeAndSetValue(attribute, api, obj, m, params, forceComputation);
        return attribute;
    }

    private void invokeAndSetValue(Attribute attribute, ASTAPI api, Object obj, Method m, Object[] params, boolean forceComputation){
        try {
            if ((attribute.isParametrized() || attribute.isNTA())) {
                if(attribute.isNTA() && !attribute.isParametrized() && forceComputation)
                    attribute.setValue(m.invoke(obj));
                else
                    attribute.setValue(null);
            }else if(params != null && params.length == m.getParameterCount())
                attribute.setValue(m.invoke(obj, params));
            else if(obj != null)
                attribute.setValue(m.invoke(obj));
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            attribute.setValue(e.getCause());
        }
        if(api.getfilterConfig().getBoolean(Config.CACHED_VALUES)) {
            addCachedValues(m, attribute, false);
        }
    }

    /**
     * Get the Token of the method in the obj.
     * @param obj
     * @param m
     * @return
     */
    private Token computeToken(ASTAPI api, Object obj, Method m){
        String name = m.getName();
        try{
            return new Token(name, m.invoke(obj), m);
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            return new Token(name, e.getCause().toString(), m);
        }
    }

    /**
     *
     * @param e
     */
    private void addInvocationErrors(ASTAPI api, Throwable e, Method m){
        String message = String.format("Error while computing %s in node %s. Cause : %s", m.getName(), node.node, e.getCause() != null ? e.getCause().toString() : e.getMessage());
        api.putError(AlertMessage.INVOCATION_ERROR, message);
        //e.printStackTrace();
    }

    /**
     * Creates a list of all attributes, tokens and invokedValues
     * @return
     */
    protected ArrayList<NodeInfoHolder> toArray(){
        ArrayList<NodeInfoHolder> temp = new ArrayList<>();
        temp.addAll(NTAs.values().stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        temp.addAll(attributes.values().stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        temp.addAll(tokens.values().stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        return temp;
    }

    public Collection<NodeInfo> getAttributes(){ return attributes.values(); }
    public Collection<NodeInfo> getNTAs(){ return NTAs.values(); }
    public Collection<NodeInfo> getTokens(){ return tokens.values(); }

    //HERE BE DRAGONS, this code is so fricking bad!

    public void addCachedValues(Method m, Attribute attribute){
        addCachedValues(m, attribute, false);
    }

    public void addCachedValues(Method m, Attribute attribute, boolean force){
        if(attribute == null || (attribute.isNTA() && !force))
            return;
        if(attribute.isParametrized()) {
            Map map = (Map) findFieldName(node.node, m.getName(), node.node.getClass());
            if (map == null)
                return;
            for (Map.Entry par : (Set<Map.Entry>) map.entrySet()) {
                if (m.getParameterCount() > 1)
                    attribute.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
                else
                    attribute.addComputedValue(new Object[]{par.getKey()}, par.getValue());
            }
        }else if(attribute.isNTA() && force) {
            Object obj = findFieldName(node.node, m.getName(), node.node.getClass());
            attribute.setValue(obj);
        }
    }

    private Object findFieldName(Object obj, String methodName, Class clazz){
        if(clazz == null)
            return null;
        for(Field field : clazz.getDeclaredFields()){
            if(field.getName().contains(methodName +"_") && field.getName().contains("_value")){
                field.setAccessible(true);
                try {
                    return field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return findFieldName(obj, methodName, clazz.getSuperclass());
    }

    protected Collection<Node> computeCachedNTAS(ASTAPI api){
        HashMap<Object, Method> values = new HashMap<>();
        try {
            for(Map.Entry<Method, Object> e : findFieldNames(api.getNTAMethods(node.getClass())).entrySet()){
                Attribute attri = (Attribute) computedMethods.get(e.getKey());
                if(attri != null){
                    if(!attri.isParametrized()) {
                        if(attri.getValue() == null)
                            attri.setValue(e.getValue());
                        values.put(attri.getValue(), e.getKey());
                    }
                    else{
                        Map map = (Map) e.getValue();
                        if (map == null)
                            continue;
                        for (Map.Entry par : (Set<Map.Entry>) map.entrySet()) {
                            if (e.getKey().getParameterCount() > 1)
                                attri.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
                            else
                                attri.addComputedValue(new Object[]{par.getKey()}, par.getValue());
                        }
                        for(Object obj : attri.getComputedValues()){
                            values.put(obj, e.getKey());
                        }
                    }
                }else {
                    if (attri == null)
                            attri = (Attribute) compute(api, e.getKey(), null, false);
                    if (attri.isParametrized()) {
                        Map map = (Map) e.getValue();
                        if (map == null)
                            continue;
                        for (Map.Entry par : (Set<Map.Entry>) map.entrySet()) {
                            if (e.getKey().getParameterCount() > 1)
                                attri.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
                            else
                                attri.addComputedValue(new Object[]{par.getKey()}, par.getValue());
                            values.put(par.getValue(), e.getKey());
                        }
                    } else {
                        if (e.getValue() != null) {
                            values.put(e.getValue(), e.getKey());
                            attri.setValue(e.getValue());
                        }
                    }
                }
            }

        }catch (ClassCastException e){
            api.putError(AlertMessage.INVOCATION_ERROR, e.getMessage());
        }
        ArrayList<Node> nodes = new ArrayList<>();
        for (Map.Entry<Object, Method> e : values.entrySet()) {
            Object obj = e.getKey();
            if (node.NTAChildren.containsKey(obj))
                nodes.add(node.NTAChildren.get(obj));
            else {
                Node temp = Node.getNTANode(obj, node, api);
                node.NTAChildren.put(obj, temp);
                nodes.add(temp);
            }
        }
        return nodes;
    }

    private HashMap<Method, Object> findFieldNames(ArrayList<Method> methods){
        Class clazz = node.node.getClass();
        HashMap<Method, Object> values = new HashMap<>();
        if(methods == null || methods.size() == 0)
            return values;
        while(clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(values.size() == methods.size())  //have found all the fields, can leave now
                    return values;
                for(Method m : methods){
                    if(values.containsKey(m)) //have already found a field for this method
                       continue;
                    if (field.getName().contains(m.getName() + "_") && field.getName().contains("_value")) {
                        field.setAccessible(true);
                        try {
                            if(field.get(node.node) != null)
                                values.put(m, field.get(node.node));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return values;
    }
}
