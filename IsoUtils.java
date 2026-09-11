import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class IsoUtils
{
   private static final int SECTOR_SIZE = 2048;
   private static final int INITIAL_ROOT_DIRECTORY_RECORD = 156;

   public static String getGameId(String gamePath)
   {
      try (RandomAccessFile gameIso = new RandomAccessFile(gamePath, "r"))
      {
         gameIso.seek(16L * SECTOR_SIZE);
         
         byte[] sector = new byte[SECTOR_SIZE];
         gameIso.readFully(sector);

         int root = INITIAL_ROOT_DIRECTORY_RECORD;

         int recordSize = sector[root] & 0xFF;

         int rootSector = readIntLE(sector, root + 2);
         int rootSize = readIntLE(sector, root + 10);

         return searchFile(gameIso, rootSector, rootSize);
      }
      catch (IOException e)
      {
         System.out.println(e);
      }

      return null;
   }

   private static String searchFile(RandomAccessFile iso, int sector, int size)
   {
      try
      {
         long start = (long) sector * SECTOR_SIZE;
         long end = (long) start + size;
         
         iso.seek(start);

         while (iso.getFilePointer() < end)
         {
            int recordSize = iso.read();

            if (recordSize == 0)
            {
               long nextSector = ((iso.getFilePointer() - 1) / SECTOR_SIZE + 1) * SECTOR_SIZE;

               iso.seek(nextSector);
               continue;
            }

            byte[] record = new byte[recordSize];

            iso.seek(iso.getFilePointer() - 1);
            iso.readFully(record);

            int flags = record[25] & 0xFF;
            int nameSize = record[32] & 0xFF;

            String name = new String(record, 33, nameSize, StandardCharsets.US_ASCII);
            
            if (!name.equals("\0") && !name.equals("\1"))
            {
               String nameSplited = name.split(";")[0];
               if (nameSplited.matches("[a-zA-Z]{4}_\\d{3}\\.\\d{2}"))
                  return nameSplited;
            }
         }
      }
      catch (IOException e)
      {
         System.out.println(e);
      }

      return null;
   }

   private static int readIntLE(byte[] data, int offset)
   {
      return (data[offset] & 0xFF)
             | ((data[offset + 1] & 0xFF) << 8)
             | ((data[offset + 2] & 0xFF) << 16)
             | ((data[offset + 3] & 0xFF) << 24);
   }
}
