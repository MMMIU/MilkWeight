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

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        Scene mainScene = new Scene(new GridPane(), 500, 500);
        primaryStage.setTitle("aaa");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

}
