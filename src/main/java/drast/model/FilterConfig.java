package drast.model;

import configAST.*;
import drast.model.nodeinfo.NodeInfo;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class contains the configurations specified by the configuration language.
 * It can read and parse the file with the configuration
 */

public class FilterConfig {
    private DebuggerConfig configs; //The top node of configurations
    private boolean noError;
    private ASTBrain api; //Reference to the ASTAPI, mainly used to contribute errors

    //Variable farm
    private final String filterFileName;
    private final String filterTmpFileName;

    public static final String DYNAMIC_VALUES = "dynamic-values";
    public static final String NTA_DEPTH = "NTA-depth";
    public static final String NTA_COMPUTED = "NTA-computed";

    private HashMap<String, ArrayList<Expr>> filterCache;
    private HashMap<String, ArrayList<Expr>> subTreeCache;
    private HashMap<String, HashMap<String, Value>> styleCache;
    private HashMap<String, HashSet<String>> showCache;
    private HashMap<String, Value> configsCache;

    private String filterDir; //Directory of the filter file.

    public FilterConfig(ASTBrain api, String filterPath){
        File file = new File(filterPath);
        filterFileName = file.getName();
        filterTmpFileName = "tmp_" + filterFileName;
        this.api = api;
        this.filterDir = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(file.separator)) + file.separator;
        filterCache = new HashMap<>();
        subTreeCache = new HashMap<>();
        styleCache = new HashMap<>();
        showCache = new HashMap<>();
        configsCache = new HashMap<>();
        noError = readFilter(filterFileName);
    }

    /**
     * Scans and parses the file with the given filename.
     * If no such file can found it will create a standard Configuration file, and then parse that one.
     * Sets the configuration to the new one if no errors are thrown.
     * @param fileName
     * @return
     */
    private boolean readFilter(String fileName){
        String fullFilePath = filterDir + fileName;
        //System.out.println(fullFilePath);
        DebuggerConfig tmpFilter;

        ConfigScanner scanner;
        try {
            //System.out.println("checking for filter:" + fullFilePath);
            // check if file exists
            File f = new File(fullFilePath);
            PrintWriter writer = null;
            if(!f.exists() || f.isDirectory()) {
                writer = new PrintWriter(f, "UTF-8");
                writer.print("/*\n" +
                        "This filter is autogenerated.\n\n" +
                        " - Add the name of the filters you want to use in the configs block. \n" +
                        " - Write the name of the node types you want to see in the AST inside your filters.\n\t" +
                        " . Add a : to the begining of each name.\n\t" +
                        " . Child classes of a type will also be included in the graph.\n\n" +
                        "You can filter on attributes and AST-position, show attributes in the graph and style the nodes. \n" +
                        "see https://bitbucket.org/jastadd/jastadddebugger-exjobb/wiki/The%20Filter%20Configuration%20Language\n" +
                        "for full documentation.\n" +
                        "*/\n" +
                        "configs{\n\t" +
                        "use = include;\n" +
                        "}\n" +
                        "filter include{\n\t" +
                        "/*\n\t" +
                        ":ASTNode{\n\t\t" +
                        "when{}\n\t\t" +
                        "show{}\n\t\t" +
                        "style{}\n\t" +
                        "}\n\t" +
                        "*/\n" +
                        "}");
                api.putMessage(AlertMessage.FILTER_WARNING, "No filter file found. One was created at: " + fullFilePath);
                writer.close();
            }
            // create the scanner
            scanner = new ConfigScanner(new FileReader(fullFilePath));
            try{

                ConfigParser parser = new ConfigParser();
                // parse the config file
                tmpFilter = (DebuggerConfig) parser.parse(scanner);
                if (!tmpFilter.errors().isEmpty()) {
                    // something went wrong, tell the user the error.
                    String error = "";
                    error += "Errors: ";
                    if(tmpFilter.errors().size() > 1 )
                        api.putMessage(AlertMessage.FILTER_ERROR, "Multiple errors: ");
                    else
                        api.putMessage(AlertMessage.FILTER_ERROR, "");
                    for (ErrorMessage e: tmpFilter.errors()) {
                        api.putMessage(AlertMessage.FILTER_ERROR, e.toString());
                        error += "\n " + e;
                    }
                    System.err.println(error);

                    return false;
                }

                configs = tmpFilter;
            } catch (IOException e) {
                api.putMessage(AlertMessage.FILTER_ERROR, "IOException when reading filter file");
                e.printStackTrace(System.err);
                return false;
            } catch (ConfigParser.SyntaxError e) {
                api.putMessage(AlertMessage.FILTER_ERROR, e.getMessage());
                return false;
            }catch (Exception e) {
                api.putMessage(AlertMessage.FILTER_ERROR, "Exception when reading filter file: " + e.toString());
                e.printStackTrace();
                return false;
            }
        }catch (FileNotFoundException e) {
            System.out.println("filter full path thing: " + fullFilePath);
            String errorText = "Could not create a filter file! Maybe this program does not have writing rights to: " + fullFilePath+ "?";
            api.putMessage(AlertMessage.FILTER_ERROR, errorText);
            System.out.println(errorText);
            e.printStackTrace();
            return false;
        }catch (UnsupportedEncodingException e) {
            String errorText = "Could not create a filter file! Maybe this program does not have writing rights to: " + fullFilePath+ "?";
            e.printStackTrace();
            api.putMessage(AlertMessage.FILTER_ERROR, errorText);
        }
        return true;
    }

    /**
     * Updates the configurations, will overwrite the old configs with the new if no errors has been thrown.'
     * Will also overwrite the old configuration file.
     * @param text
     * @return
     */
    public boolean saveAndUpdateConfig(String text){
        System.out.println("SAVE");
        String fullTmpFilePath = filterDir + filterTmpFileName;
        String fullFilePath = filterDir + filterFileName;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fullTmpFilePath, "UTF-8");
            writer.print(text);
            writer.close();
        } catch (FileNotFoundException e) {
            api.putMessage(AlertMessage.FILTER_ERROR, "File not found when writing to filter file");
            e.printStackTrace();
            noError = false;
        } catch (UnsupportedEncodingException e) {
            api.putMessage(AlertMessage.FILTER_ERROR, "Unsupported encoding exception");
            e.printStackTrace();
            noError = false;
        }
        noError = readFilter(filterTmpFileName);
        try {
            if (noError) {
                File oldFilter = new File(fullFilePath);
                File newFilter = new File(fullTmpFilePath);
                oldFilter.delete();
                newFilter.renameTo(oldFilter);
                clearCaches();
            } else {
                File newFilter = new File(fullTmpFilePath);
                newFilter.delete();
            }
        }catch(Exception e){
            e.printStackTrace();
            api.putMessage(AlertMessage.FILTER_ERROR, "Could not delete or remove new or old filter file. Permission problem?");
        }
        return noError;
    }

    private void clearCaches(){
        filterCache.clear();
        subTreeCache.clear();
        styleCache.clear();
        showCache.clear();
        configsCache.clear();
    }

    /**
     * Will fetch the names of the enabled filters, from the DebuggerConfig.
     * @return
     */
    public String getEnabledFilterNames(){
        return configs.getEnabledFilterNames();
    }

    /**
     * Get the int value for the config with the name "name"
     * @param name
     * @return
     */
    public int getInt(String name){
        Value v = getConfigValue(name);
        return v != null && v.isInt() ? v.getInt() : 0;
    }

    /**
     * Get the boolean value for the config with the name "name"
     * @param name
     * @return
     */
    public boolean getBoolean(String name){
        Value v = getConfigValue(name);
        return v != null && v.isBool() ? v.getBool() : false;
    }

    /**
     * Call to the debuggerConfig to get the config value, and caches the config
     * @param name
     * @return
     */

    private Value getConfigValue(String name) {
        Value v;
        if(configs == null)
            return null;
        if (configsCache.containsKey(name))
            v = configsCache.get(name);
        else {
            v = configs.getConfigs(name);
            configsCache.put(name, v);
        }
        return v;
    }

    /**
     * This method will determine if the given node is filtered or not.
     * It will compute the attributes specified in the configuration language, and check if they match with the one in the filter.
     * NOTE: if "filter" is set to false, in the -config block for configuration language, this method will always return true.
     * @param node
     * @return true if not filtered.
     */
    public boolean isEnabled(Node node){
        if(configs == null || node.isNull())
            return false;

        if(!configs.getConfigs().hasUse())
            return true;
        ArrayList<Expr> exprs;
        if(filterCache.containsKey(node.simpleNameClass))
            exprs = filterCache.get(node.simpleNameClass);
        else{
            exprs = configs.getFilterExpressions(node.node.getClass());
            filterCache.put(node.simpleNameClass, exprs);
        }

        if(exprs == null)
            return false;
        return getValidateExprs(exprs, node);
    }

    public boolean hasSubTree(Node node){
        if(configs == null || node.isNull())
            return false;

        if(!configs.getConfigs().hasUse())
            return true;

        ArrayList<Expr> exprs;
        if(subTreeCache.containsKey(node.simpleNameClass))
            exprs = subTreeCache.get(node.simpleNameClass);
        else{
            exprs = configs.getSubTreeExpressions(node.node.getClass());
            subTreeCache.put(node.simpleNameClass, exprs);
        }

        if(exprs == null)
            return false;
        return getValidateExprs(exprs, node);
    }

    private boolean getValidateExprs(ArrayList<Expr> exprs, Node node){
        boolean success = true;
        for(Expr expr : exprs){
            String decl = expr.getDecl().getID();

            if(!expr.isBinExpression()){
                success =  expr.validateExpr(node);
            }else {
                BinExpr be = (BinExpr) expr;
                Object a = api.computeMethod(node, decl);
                Method aM = api.getMethod(node, decl);
                if (a != null) {
                    if (be.isDoubleDecl()) {
                        String decl2 = ((LangDecl) be.getValue()).getID();
                        Object b = api.computeMethod(node, decl2);
                        Method bM = api.getMethod(node, decl2);
                        if (b == null)
                            success =  false;
                        else
                            success = aM.getReturnType().equals(bM.getReturnType()) && be.validateExpr(a, b, aM.getReturnType(), decl);
                    } else
                        success = be.validateExpr(a);
                } else
                    success = false;
            }
            if(!success)
                return false;
        }
        return success;
    }

    /**
     * Returns a HashMap with the styles for the the node. Where the String is the name of the style and the Value is obviously the value.
     * @param node
     * @return
     */
    public HashMap<String, Value> getNodeStyle(Node node){
        HashMap<String, Value> map;
        if(configs == null || node.isNull())
            return new HashMap<>();

        if(styleCache.containsKey(node.simpleNameClass))
            return styleCache.get(node.simpleNameClass);

        map = configs.getNodeStyles(node.node.getClass());
        styleCache.put(node.simpleNameClass, map);
        return map;
    }

    /**
     * Returns a HashSet of the attributes that should be displayed. for the specified node.
     * @param node
     * @return
     */
    public HashSet<String> getDisplayedAttributes(Node node){
        HashSet<String> show;
        if(configs == null || node.isNull()){
            return new HashSet<>();
        }
        if(showCache.containsKey(node.simpleNameClass))
            show = showCache.get(node.simpleNameClass);
        else{
            show = configs.getShow(node.node.getClass());
            showCache.put(node.simpleNameClass, show);
        }
        return show;
    }
}