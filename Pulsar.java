import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Pulsar extends Application
{
   @Override
   public void start(Stage stage) throws Exception
   {
      Parent root = FXMLLoader.load(getClass().getResource("Pulsar.fxml"));

      Scene scene = new Scene(root);
      stage.setTitle("PS2 Pulsar");
      stage.setScene(scene);
      stage.show();
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}
