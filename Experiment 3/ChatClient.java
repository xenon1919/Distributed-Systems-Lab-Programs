import java.net.*;
import java.io.*;

public class ChatClient implements Runnable {
    private ChatClientThread client = null;
    private Socket socket = null;
    private DataInputStream console = null;
    private PrintStream Out = null;
    private Thread thread = null;

    public ChatClient(String serverName, int serverPort) {
        System.out.println("Establishing connection, please wait...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            console = new DataInputStream(System.in);
            Out = new PrintStream(socket.getOutputStream());
            if (thread == null) {
                client = new ChatClientThread(this, socket);
                thread = new Thread(this);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void run() {
        while (thread != null) {
            try {
                Out.println(console.readLine());
                Out.flush();
            } catch (IOException e) {
                System.out.println("Sending error: " + e.getMessage());
                stop();
            }
        }
    }

    public void handle(String msg) {
        if (msg.equals("quit")) {
            System.out.println("Goodbye. Press RETURN to exit...");
            stop();
        } else {
            System.out.println(msg);
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (console != null) console.close();
            if (Out != null) Out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing: " + e);
        }
        client.close();
        client.stop();
    }

    public static void main(String args[]) {
        ChatClient client = null;
        if (args.length != 2)
            System.out.println("Usage: java ChatClient <host> <port>");
        else
            client = new ChatClient(args[0], Integer.parseInt(args[1]));
    }
}

