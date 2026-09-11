import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PulsarController
{
   private String directoryPath;

   private ObservableList<Game> games = FXCollections.observableArrayList();

   @FXML
   private ImageView covImageView;

   @FXML
   private ImageView icoImageView;

   @FXML
   private Label directoryPathLabel;

   @FXML
   private TableColumn<Game, String> gameTitleTableColumn;

   @FXML
   private TableColumn<Game, String> gameIdTableColumn;

   @FXML
   private TableView<Game> gamesListTableView;

   @FXML
   private void getDirectoryPath(ActionEvent event)
   {

      File initialDirectory = new File("/home");
      DirectoryChooser chooseDirectoryWindow = new DirectoryChooser();
      Stage chooseDirectoryWindowStage = new Stage();

      chooseDirectoryWindow.setTitle("Selecionar pasta");
      chooseDirectoryWindow.setInitialDirectory(initialDirectory);
      initialDirectory = chooseDirectoryWindow.showDialog(chooseDirectoryWindowStage);
      try
      {
         Files.createDirectories(initialDirectory.toPath());
      }
      catch (IOException e)
      {
         System.out.println(e);
      }
      directoryPath = initialDirectory.toString();
      createAllOplFolders();
      directoryPathLabel.setText("Pasta: " + directoryPath);
      fillGameTable();    
   }

   public boolean verifyPathFolder(int folder)
   {
      String[] defaultFolders = { 
         "APPS", "ART", "BOOT", "CD", "CFG", "CHT", "DVD", "INFO", "LNG", "OPL", "THM", "VMC"};

      return Files.exists(Path.of(directoryPath + File.separator + defaultFolders[folder]));
   }

   public void createFolder(int folder)
   {
      String[] defaultFolders = {
         "APPS", "ART", "BOOT", "CD", "CFG", "CHT", "DVD", "INFO", "LNG", "OPL", "THM", "VMC"};

      try
      {
         Files.createDirectory(Path.of(directoryPath + File.separator + defaultFolders[folder]));
      }
      catch (IOException e)
      {
         System.out.println(e);

      }
   }

   public void createAllOplFolders()
   {
      for (int counter = 0; counter < 12; counter++)
      {
         if (verifyPathFolder(counter) == false)
            createFolder(counter);
      }
   }

   public void fillGameTable()
   {
      try (Stream<Path> gamesStream = Stream.concat(getPs2GameFiles("CD"), getPs2GameFiles("DVD")))
      {
         gamesStream.sorted()
                    .forEach(game -> games.add(new Game(game)));
      }

      gamesListTableView.setItems(games);
         
      gameTitleTableColumn.setCellValueFactory(game -> game.getValue().gameTitleProperty());
      gameIdTableColumn.setCellValueFactory(game -> game.getValue().gameIdProperty());
   }

   /*@FXML
   public void startRenameGame(ActionEvent event)
   {}

   @FXML
   public void renameGame()
   {}

   @FXML
   public void cancelRenaming()
   {}*/

   Predicate<Path> hasIso = fileName -> fileName.toString().toLowerCase().endsWith(".iso");

   public Stream<Path> getPs2GameFiles(String folder)
   {
      try
      {     
         return Files.walk(Path.of(directoryPath, folder))
                     .filter(hasIso);
      }
      catch(IOException e)
      {
         System.out.println(e);
         return Stream.empty();
      }
   }

   @FXML
   private void getCovers(ActionEvent event)
   {
      games.forEach(game -> Covers.downloadCovers(directoryPath, game.getGameId(), "COV"));
      games.forEach(game -> Covers.downloadCovers(directoryPath, game.getGameId(), "ICO"));
   }

   @FXML
   private void rowSelection() throws IOException
   {
      TableSelectionModel lineSelection = gamesListTableView.getSelectionModel();
      
      String gameId = lineSelection.getSelectedItems()
                                   .getFirst()
                                   .toString();

      String imagePath = directoryPath + File.separator + "ART" + File.separator + gameId;

      covImageView.setImage(new Image(Files.newInputStream(Path.of(imagePath + "_COV.png"), StandardOpenOption.READ)));
      icoImageView.setImage(new Image(Files.newInputStream(Path.of(imagePath + "_ICO.png"), StandardOpenOption.READ)));
   }

   public void initialize()
   {
      directoryPathLabel.setText("Pasta: /home");
   }
}
