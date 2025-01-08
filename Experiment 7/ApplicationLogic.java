import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLogic {
    private Connection conn;

    public ApplicationLogic() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:app_db.sqlite");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY, name TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItem(String item) {
        String sql = "INSERT INTO items (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getItems() {
        List<String> items = new ArrayList<>();
        String sql = "SELECT name FROM items";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

