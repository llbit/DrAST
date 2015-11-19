import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.graph.GraphView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import junit.framework.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by gda10jli on 11/17/15.
 */
public class UIComponentTestSuite extends UIApplicationTestHelper {

    protected Object getRootNode() {
        try {
            System.out.println("start UI tests");
            try{
                ConfigScanner scanner = new ConfigScanner(new FileReader("tests/uiTests/allBlocks/testInput.cfg"));
                ConfigParser parser = new ConfigParser();
                DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
                if (!program.errors().isEmpty()) {
                    System.err.println();
                    System.err.println("Errors: ");
                    for (ErrorMessage e: program.errors()) {
                        System.err.println("- " + e);
                    }
                } else {
                    return program;
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
                //System.exit(1);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected static UIMonitor mon;
    protected static JastAddAdAPI jastAddAd;
    protected static Controller con;

    @Override
    public void start(Stage stage) throws Exception {
        jastAddAd = new JastAddAdAPI(getRootNode());
        jastAddAd.run();
        mon = new UIMonitor(jastAddAd.api());
        FXMLLoader loader = new FXMLLoader();
        Parent rootView = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        con.init(mon, graphview);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTAPI.VERSION);
        stage.setScene(new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
        stage.setMaximized(true);
        stage.show();
        ScrollPane center = (ScrollPane) rootView.lookup("#graphView");
        //center.setContent(graphview);
    }

    @Test
    public void testMinimize() {
        clickOn("#minimizeConsole");
        clickOn("#minimizeRightSide");
        clickOn("#minimizeLeftSide");

        clickOn("#minimizeConsole");
        clickOn("#minimizeRightSide");
        clickOn("#minimizeLeftSide");

        push(KeyCode.F);
        push(KeyCode.F);
    }

    @Test
    public void testConsole() {
        clickOn("#consoleTabWarning");
        mon.getController().addWarning("Test warning");
        clickOn("#consoleTabError");
        mon.getController().addError("Test error");
        clickOn("#consoleTabMessage");
        mon.getController().addMessage("Test message");
        clickOn("#consoleTabAll");
        
    }



}
