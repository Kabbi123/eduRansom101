import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// create *.txt in ressources folder to test encryption

public class DecryptDES {
    public static void main(String[] args) {



        //!!!always check the path!!!
        // never choose any needed directories!
        String directory = "/Users/christian/IdeaProjects/eduRansom101/ressources";


        int sleepTime = 1000; // Sleep time in milliseconds

        encryptDir(directory, sleepTime);
    }

    public static void encryptDir(String d, int mili) {
        File dirToEncrypt = new File(d);
        File[] files = dirToEncrypt.listFiles();

        if (files != null) {
            for (File currentFile : files) {
                if (!currentFile.isDirectory() && !currentFile.getName().toLowerCase().endsWith(".exe")) {
                    String key = "R?\n??i??";

                    // if text file has no .axx, it gets lost
                    encryptFile(currentFile.getAbsolutePath(), currentFile.getAbsolutePath().replace(".axx", ""), key);
                    currentFile.delete();
                    try {
                        Thread.sleep(mili);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }




    public static void encryptFile(String sInputFilename, String sOutputFilename, String sKey) {
        try {
            FileInputStream fsInput = new FileInputStream(sInputFilename);
            FileOutputStream fsEncrypted = new FileOutputStream(sOutputFilename);

            SecretKeySpec secretKey = new SecretKeySpec(sKey.getBytes(), "DES");

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            CipherOutputStream cipherOutputStream = new CipherOutputStream(fsEncrypted, cipher);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fsInput.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }

            cipherOutputStream.close();
            fsInput.close();
            fsEncrypted.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





