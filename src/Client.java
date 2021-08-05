import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    /**
     * Para se referir aos arquivos, sempre usar endereços absolutos.
     * Ex: /home/usuario/teste.txt
     *
     */

    public static void main(String[] args) {
        System.out.println("== Cliente ==\n");
        System.out.println("Comandos disponíveis:\n" +
                "'readdir [PATH]' - Lista os arquivos e pastas de um diretório\n" +
                "'rename [NOME] [NOVO NOME]' - Renomeia um arquivo disponível\n" +
                "'create [NAME]' - Cria um arquivo disponível\n" +
                "'remove [NAME]' - Deleta um arquivo disponível\n" +
                "'exit' - Disconecta do servidor\n");

        try {
            Socket socket = new Socket("localhost", 6000);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            while (true) {
                System.out.print(">> ");
                Scanner keyboard = new Scanner(System.in);
                String command = keyboard.nextLine();

                dos.writeUTF(command);
                if (command.equals("exit")) {
                    socket.close();
                    break;
                }

                String receivedMessage = dis.readUTF();

                if (!receivedMessage.equals("")) System.out.println(receivedMessage);
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
