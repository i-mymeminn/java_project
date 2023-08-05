//userdatastorage
package 로그인화면소스코드분할;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDataStorage {
    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";

    public static void saveUser(String username, String birthday, String phonenumber, String id, String password) {
        String query = "INSERT INTO users (username,birthday, phonenumber, id, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
 
            // 데이터 바인딩
            statement.setString(1, username);
            statement.setString(2, birthday);
            statement.setString(3, phonenumber);
            statement.setString(4, id);
            statement.setString(5, password);

            // 쿼리 실행
            statement.executeUpdate();
            System.out.println("User data saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isIdExists(String id) {
        String query = "SELECT id FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean checkUserCredentials(String id, String password) {
        String query = "SELECT id FROM users WHERE id = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next(); // 일치하는 데이터가 존재하면 true를 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }	
}