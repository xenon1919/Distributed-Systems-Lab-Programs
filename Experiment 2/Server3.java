import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
class Server3{
    public static void main(String a[]){
        ServerSocket sock;
        Socket client;
        DataInputStream input;
        PrintStream ps;
        String url, u, s;
        Connection con;
        Statement smt;
        ResultSet rs;
        try {
            s = u = "\0";
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            con = DriverManager.getConnection("jdbc:odbc:myserver", "system", "manager");
            smt = con.createStatement();
            sock = new ServerSocket(5125);
            while (true) {
                client = sock.accept();
                input = new DataInputStream(client.getInputStream());
                ps = new PrintStream(client.getOutputStream());
                url = input.readLine();
                StringTokenizer st = new StringTokenizer(url, ".");
                while (st.countTokens() > 1) {
                    s = s + st.nextToken() + ".";
                }
                s = s.substring(0, s.length() - 1).trim();
                u = st.nextToken();
                rs = smt.executeQuery("select port, ipadd from google where name='" + u + "'");
                if (rs.next()) {
                    ps.println(rs.getString(1));
                    ps.println(rs.getString(2));
                    ps.println(s);
                } else {
                    ps.println("Illegal address, please check the spelling again");
                    con.close();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
