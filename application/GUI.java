/**
 *
 */
package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Yifei Miao
 *
 */
public class GUI extends Application {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 400;
    private static final String APP_TITLE = "CS400 My First JavaFX Program";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        Scene mainScene = new Scene(new GridPane(), 500, 500);
        primaryStage.setTitle("aaa");
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Stage FileManagerStage() {
        return null;
    }
}
