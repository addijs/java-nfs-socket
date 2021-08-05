import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);

            System.out.println("=== Servidor iniciado ===\n");

            while (true) {
                Socket client = serverSocket.accept();
                String clientAddress = client.getInetAddress().getHostAddress();
                System.out.println(clientAddress + " se conectou!");

                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                DataInputStream dis = new DataInputStream(client.getInputStream());

                while (!client.isClosed()) {
                    String command = dis.readUTF();

                    if (command.equals("exit")) {
                        String exitMessage = String.format("Cliente %s desconectado.", clientAddress);
                        System.out.println(exitMessage);
                        client.close();
                    }

                    String[] list = command.split(" ");

                    switch (list[0]) {
                        /**
                         * list[0] é o primeiro pedaço do comando,
                         * ou seja, alguma das ações (readdir, rename, etc.)
                         */

                        case "readdir":
                            String response = "";

                            File path = new File(list[1]);

                            if (path.exists()) {
                                File[] files = path.listFiles();
                                for (File file : files) {
                                    response += file.getName() + " ";
                                }
                            } else {
                                response = "Esse diretório não existe.";
                            }

                            dos.writeUTF(response);
                            break;
                        case "rename":
                            File fileToRename = new File(list[1]);
                            File newName = new File(list[2]);

                            if(fileToRename.exists()) {
                                fileToRename.renameTo(newName);
                                dos.writeUTF("");
                            } else {
                                dos.writeUTF("Esse arquivo/diretório não existe.");
                            }

                            break;
                        case "create":
                            File newFile = new File(list[1]);
                            // list[1] é o nome do arquivo

                            if (!newFile.exists()) {
                                newFile.createNewFile();
                                dos.writeUTF("");
                            } else {
                                dos.writeUTF("Esse arquivo/diretório já existe.");
                            }

                            break;
                        case "remove":
                            File fileToRemove = new File(list[1]);
                            // list[1] é o nome do arquivo

                            if (fileToRemove.exists()) {
                                fileToRemove.delete();
                                dos.writeUTF("");
                            } else {
                                dos.writeUTF("Esse arquivo/diretório não existe.");
                            }

                            break;
                    }
                }

                client.close();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
