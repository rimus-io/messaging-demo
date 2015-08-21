import com.rimusdesign.messaging.app.AppPM;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * @author Rimas Krivickas.
 */
public class AppMain extends Application {


    private AppPM pm;


    @Override
    public void start (Stage primaryStage) throws Exception {

        pm = new AppPM(primaryStage);
        pm.onCreationComplete();
    }


    @Override
    public void stop () throws Exception {

        pm.exit();
    }


    public static void main (String[] args) {

        launch(args);
    }
}
