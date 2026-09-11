import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.IllegalStateException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Covers
{
   public static void downloadCovers(String oplPath, String gameId, String coverType)
   {
      String urlBase = "https://archive.org/download/OPLM_ART_2024_09/OPLM_ART_2024_09.zip/PS2/";
      String coverFileName = gameId + "_" + coverType + ".png";

      try
      {
         URLConnection connection = URI.create(urlBase + gameId + "/" + coverFileName)
                                       .toURL()
                                       .openConnection();

         connection.setAllowUserInteraction(false);
         connection.setUseCaches(false);
         connection.connect();

         connection.getContent();

         InputStream input = connection.getInputStream();

         byte[] image = input.readAllBytes();

         OutputStream output = Files.newOutputStream(
            Path.of(oplPath, "ART", coverFileName), StandardOpenOption.CREATE);

         output.write(image);
      }
      catch (IOException e)
      {
         System.out.println(e); 
      }
      catch (IllegalStateException e)
      {
         System.out.println(e); 
      }  
   }
}
