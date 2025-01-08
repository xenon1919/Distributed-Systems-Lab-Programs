import java.net.*;
import java.io.*;

class FTPCLIENT {
    public static void main(String args[]) throws Exception {
        Socket socket = new Socket("localhost", 6789);
        transferfileClient t = new transferfileClient(socket);
        t.displayMenu();
    }
}

class transferfileClient {
    Socket clientSocket;
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    transferfileClient(Socket socket) {
        try {
            clientSocket = socket;
            din = new DataInputStream(clientSocket.getInputStream());
            dout = new DataOutputStream(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void SendFile() throws Exception {
        System.out.print("Enter File Name: ");
        String filename = br.readLine();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not Exists...");
            dout.writeUTF("File Not Found");
            return;
        }
        dout.writeUTF(filename);
        String response = din.readUTF();
        if (response.equals("READY")) {
            System.out.println("Sending File ...");
            FileInputStream fin = new FileInputStream(file);
            int ch;
            do {
                ch = fin.read();
                dout.writeUTF(String.valueOf(ch));
            } while (ch != -1);
            fin.close();
            System.out.println(din.readUTF());
        } else {
            System.out.println(response);
        }
    }

    void ReceiveFile() throws Exception {
        System.out.print("Enter File Name: ");
        String filename = br.readLine();
        dout.writeUTF(filename);
        String response = din.readUTF();
        if (response.equals("READY")) {
            FileOutputStream fout = new FileOutputStream(filename);
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
            System.out.println(din.readUTF());
        } else {
            System.out.println(response);
        }
    }

    public void displayMenu() throws Exception {
        while (true) {
            System.out.println("[ MENU ]");
            System.out.println("1. Send File");
            System.out.println("2. Receive File");
            System.out.println("3. Exit");
            System.out.print("Enter Choice: ");
            int choice = Integer.parseInt(br.readLine());
            if (choice == 1) {
                dout.writeUTF("SEND");
                SendFile();
            } else if (choice == 2) {
                dout.writeUTF("GET");
                ReceiveFile();
            } else if (choice == 3) {
                dout.writeUTF("DISCONNECT");
                System.out.println("Disconnecting...");
                break;
            }
        }
    }
}

