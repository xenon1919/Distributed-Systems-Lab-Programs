import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {
    private ChatServer server = null;
    private Socket socket = null;
    private DataInputStream In = null;
    private PrintStream Out = null;
    private int ID = -1;

    public ChatServerThread(ChatServer serv, Socket sock) {
        super();
        server = serv;
        socket = sock;
        ID = socket.getPort();
    }

    public void send(String msg) {
        Out.println(msg);
        Out.flush();
    }

    public int getID() {
        return ID;
    }

    public void run() {
        System.out.println("Server thread " + ID + " running");
        while (true) {
            try {
                server.handle(ID, In.readLine());
            } catch (IOException e) {
                System.out.println(ID + " error reading: " + e.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }

    public void open() throws IOException {
        In = new DataInputStream(socket.getInputStream());
        Out = new PrintStream(socket.getOutputStream());
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
        if (In != null) In.close();
        if (Out != null) Out.close();
    }
}

