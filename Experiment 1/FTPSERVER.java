import java.net.*;
import java.io.*;

public class FTPSERVER {
    public static void main(String args[]) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("FTP Server Started on Port 6789");
        while (true) {
            System.out.println("Waiting for Connection ...");
            Socket socket = serverSocket.accept();
            new transferfile(socket);
        }
    }
}

class transferfile extends Thread {
    Socket clientSocket;
    DataInputStream din;
    DataOutputStream dout;

    transferfile(Socket socket) {
        try {
            clientSocket = socket;
            din = new DataInputStream(clientSocket.getInputStream());
            dout = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("FTP Client Connected ...");
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void SendFile() throws Exception {
        String filename = din.readUTF();
        File file = new File(filename);
        if (!file.exists()) {
            dout.writeUTF("File Not Found");
            return;
        } else {
            dout.writeUTF("READY");
            FileInputStream fin = new FileInputStream(file);
            int ch;
            do {
                ch = fin.read();
                dout.writeUTF(String.valueOf(ch));
            } while (ch != -1);
            fin.close();
            dout.writeUTF("File Sent Successfully");
        }
    }

    void ReceiveFile() throws Exception {
        String filename = din.readUTF();
        File file = new File(filename);
        if (file.exists()) {
            dout.writeUTF("File Already Exists");
        } else {
            dout.writeUTF("READY");
            FileOutputStream fout = new FileOutputStream(file);
            int ch;
            String temp;
            do {
                temp = din.readUTF();
                ch = Integer.parseInt(temp);
                if (ch != -1) {
                    fout.write(ch);
                }
            } while (ch != -1);
            fout.close();
            dout.writeUTF("File Received Successfully");
        }
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for Command ...");
                String command = din.readUTF();
                if (command.equals("GET")) {
                    System.out.println("GET Command Received ...");
                    SendFile();
                } else if (command.equals("SEND")) {
                    System.out.println("SEND Command Received ...");
                    ReceiveFile();
                } else if (command.equals("DISCONNECT")) {
                    System.out.println("Disconnect Command Received ...");
                    clientSocket.close();
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
