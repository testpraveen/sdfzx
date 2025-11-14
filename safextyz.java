import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class SuperInsecure {
    // Hardcoded credentials (secrets)
    private static final String API_KEY = "123456-SECRET-API-KEY";
    private static final String DB_PASSWORD = "rootpass";

    public static void main(String[] args) throws Exception {
        // ⚠️ Improper Randomness
        Random r = new Random(); 
        System.out.println("Weak random: " + r.nextInt());

        // ⚠️ Extremely unsafe: user input from environment
        String user = System.getenv("INPUT");

        // ⚠️ SQL Injection
        Connection conn = DriverManager.getConnection(
                "jdbc:h2:mem:test", "sa", ""
        );
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE users(name VARCHAR)");
        stmt.execute("INSERT INTO users VALUES('admin')");
        stmt.executeQuery("SELECT * FROM users WHERE name = '" + user + "'");

        // ⚠️ Command Injection
        Runtime.getRuntime().exec("sh -c \"echo " + user + "\"");

        // ⚠️ Path Traversal
        String file = "/tmp/" + user;
        String content = new String(Files.readAllBytes(Paths.get(file)));
        System.out.println("Read file: " + content);

        // ⚠️ Insecure Deserialization
        ByteArrayInputStream bais = new ByteArrayInputStream(user.getBytes());
        ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            Object o = ois.readObject();
            System.out.println("Deserialized: " + o);
        } catch (Exception ex) {
            // Ignore
        }

        // ⚠️ Reflection misuse
        Class cls = Class.forName(user);
        cls.newInstance();

        // ⚠️ Weak Cryptography
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        md.update("test".getBytes());
        byte[] digest = md.digest();
        System.out.println("Weak MD5 hash: " + Arrays.toString(digest));

        // ⚠️ eval() equivalent - Java script engine injection
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.eval(user);

        // ⚠️ Logging sensitive data
        System.out.println("API_KEY = " + API_KEY);
        System.out.println("DB_PASSWORD = " + DB_PASSWORD);

        // ⚠️ Unchecked deserialization from file
        FileInputStream fis = new FileInputStream("untrusted.ser");
        ObjectInputStream ois2 = new ObjectInputStream(fis);
        ois2.readObject();
    }
}
