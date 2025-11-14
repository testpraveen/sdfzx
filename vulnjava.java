import java.sql.*;
import java.nio.file.*;
import java.io.*;

public class VulnJavaExample {
    public static void main(String[] args) throws Exception {
        // SQL Injection
        String userInput = System.getenv("USER_INPUT");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        // Vulnerability: concatenated SQL
        stmt.executeQuery("SELECT * FROM users WHERE name = '" + userInput + "'");

        // Path Traversal
        String filename = userInput; 
        Path p = Paths.get("/var/data/" + filename);
        String content = new String(Files.readAllBytes(p));
        System.out.println(content);
    }
}
