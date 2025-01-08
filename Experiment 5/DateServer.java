import java.net.*;
import java.io.*;
import java.util.*;

class DateServer {
    public static void main(String args[]) throws Exception {
        ServerSocket s = new ServerSocket(5217);
        while (true) {
            System.out.println("Waiting For Connection ...");
            Socket soc = s.accept();
            // Missing code to print the date
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            Date date = new Date();
            out.println(date.toString()); // Sends the date to the client
            soc.close();
        }
    }
}

