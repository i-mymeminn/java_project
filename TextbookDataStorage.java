package 로그인화면소스코드분할;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class TextbookDataStorage {
    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";

    public static void saveTextbook(int studentId, String department, int grade, String subject, String professor, String textbookName, String writer, String price) {

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // 중복 체크를 위해 이미 장바구니에 해당 책이 존재하는지 확인
        	String checkDuplicateQuery = "SELECT * FROM basket WHERE studentid = ? AND textbook_name = ?";
        	PreparedStatement duplicateStatement = conn.prepareStatement(checkDuplicateQuery);
        	duplicateStatement.setInt(1, studentId);
        	duplicateStatement.setString(2, textbookName);
        	ResultSet duplicateResultSet = duplicateStatement.executeQuery();

            if (duplicateResultSet.next()) {
                // 이미 장바구니에 담겨있는 책인 경우 메시지 표시
                JOptionPane.showMessageDialog(null, "이미 장바구니에 담겨있는 책입니다.", "중복된 책", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // 중복된 책이 아닌 경우 장바구니에 추가
                String insertQuery = "INSERT INTO basket (studentid, department, grade, subject, professor, textbook_name, writer,price) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setInt(1, studentId);
                insertStatement.setString(2, department);
                insertStatement.setInt(3, grade);
                insertStatement.setString(4, subject);
                insertStatement.setString(5, professor);
                insertStatement.setString(6, textbookName);
                insertStatement.setString(7, writer);
                insertStatement.setString(8, price);
                insertStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "장바구니에 책이 추가되었습니다.", "장바구니 추가", JOptionPane.INFORMATION_MESSAGE);
            }

            // 자원 해제
            duplicateResultSet.close();
            duplicateStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
