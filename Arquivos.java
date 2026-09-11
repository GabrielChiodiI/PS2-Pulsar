import java.io.File;
import java.nio.file.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Arquivos
{
   private Path path = 

   public static String chooseDirectoryPath()
   {
      try
      {
         File directory = new File("/home");
         DirectoryChooser chooseDirectory = new DirectoryChooser();
         Stage chooseDirectoryWindow = new Stage();

         chooseDirectory.setTitle("Selecionar Pasta");
         chooseDirectory.setInitialDirectory(directory);
         directory = chooseDirectory.showDialog(chooseDirectoryWindow);

         return directory.toString();
      }
      catch()
      {}
   }
}
