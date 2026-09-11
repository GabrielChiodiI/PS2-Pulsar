import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Game
{
   private String gameFolderPath;
   private StringProperty gameTitle;
   private StringProperty gameId;
   private String gameFileType;

   public Game(Path path)
   {
      gameFolderPath = path.getParent().toString();
      String[] titleAndType = path.getFileName().toString().split("\\.");
      setTitleAndType(titleAndType);
      setGameId(IsoUtils.getGameId(path.toString()));
   }

   // gameTitle StringProperty methods
   public void setGameTitle(String title)
   { 
      gameTitleProperty().set(title);
   }

   public void updateGameTitle(String newTitle)
   {
      try
      {
         if (Files.exists(makeGamePath(getGameTitle())))
         {
            Files.move(makeGamePath(getGameTitle()), makeGamePath(newTitle));
            gameTitleProperty().set(newTitle);
         }

         else
         {
            System.out.printf("O arquivo %s não existe nesse diretório!%n", getGameTitle() + "." + gameFileType);
         }
      }
      catch (IOException e)
      {
         System.out.println(e); 
      }
   }

   public String getGameTitle()
   {
      return gameTitleProperty().get();
   }

   public StringProperty gameTitleProperty()
   {
      if (gameTitle == null) gameTitle = new SimpleStringProperty(this, "gameTitle");

      return gameTitle;
   }

   // gameId StringProperty methods
   public void setGameId(String id)
   { 
      gameIdProperty().set(id);
   }

   public String getGameId()
   {
      return gameIdProperty().get();
   }

   public StringProperty gameIdProperty()
   {
      if (gameId == null) gameId = new SimpleStringProperty(this, "gameId");

      return gameId;
   }
     
   private void setTitleAndType(String[] titleAndType)
   {
     String title = ""; 
     gameFileType = titleAndType[titleAndType.length - 1];
      
      for (int count = 0; count < titleAndType.length - 1; count++)
         title += titleAndType[count];
         
      setGameTitle(title);
   }
   
   private Path makeGamePath(String title)
   {
      return Path.of(gameFolderPath, title + "." + gameFileType);
   }
   
   public String toString()
   {
      return getGameId();
      //getGameTitle() + "." + gameFileType;
   }
}
