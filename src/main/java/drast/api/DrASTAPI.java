package drast.api;

import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import drast.api.filteredtree.GenericTreeNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is the main class for the DrAST system. This class will create an ASTAPI object that will generate the
 * filtered AST.
 *
 * DrAST can be started by calling run() method on this class or DrASTUI (in the DrAST.ui package).
 *
 */
public class DrASTAPI {
	public final static String FILE_NAME = "DrAST-result";
    public final static String CLUSTER_STRING = "cluster";
    public final static String DEFAULT_FILTER_NAME = "filter.fcl";

	private ASTAPI api;

    private Object root;
    private String filterPath;
    private boolean done;
	private boolean listRoot;
	private boolean holderAPI;
	
	public DrASTAPI(Object root){
        this.root = root;
		filterPath = DEFAULT_FILTER_NAME;
        done = false;
	}

	public DrASTAPI(Object root, boolean listRoot){
		this.root = root;
		filterPath = DEFAULT_FILTER_NAME;
		done = false;
		this.listRoot = listRoot;
	}

	public DrASTAPI(){
		done = false;
		holderAPI = true;
	}

    public boolean hasRun(){
        return done;
    }

    /**
     * Sets the directory of the projects
     * @param dir
     */
    public void setFilterPath(String dir){filterPath = dir;}

    /**
     * run() generates the AST
     */
	public void run(){
        api = holderAPI ? new ASTAPI() : new ASTAPI(root, filterPath, listRoot);
        done = true;
	}

    public ASTAPI api(){ return api; }

	public GenericTreeNode getFilteredTree(){
		return api.getFilteredTree();
	}

    /**
     * main function for starting a DrASTAPI session.
     * @param args
     */
	public static void main(String[] args) {
		String filename = "sample.fcl";
		try{
			ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
			ConfigParser parser = new ConfigParser();
			DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
			if (!program.errors().isEmpty()) {
				System.err.println();
				System.err.println("Errors: ");
				for (ErrorMessage e: program.errors()) {
					System.err.println("- " + e);
				}
			} else {
				DrASTAPI debugger = new DrASTAPI(program);
                debugger.run();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filename);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}