package 로그인화면소스코드분할;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FindIdFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private JTextField usernameTextField;
    private JTextField phonenumberTextField;

    public FindIdFrame() {
        setTitle("Find ID");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel usernameLabel = new JLabel("사용자 이름:");
        JLabel phonenumberLabel = new JLabel("전화번호 :");

        usernameTextField = new JTextField(20);
        phonenumberTextField = new JTextField(20);

        JButton findIdButton = new JButton("Find ID");
        findIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String phonenumber = phonenumberTextField.getText();

                // 아이디 찾기 로직
                String foundId = findId(username, phonenumber);
                if (foundId != null) {
                    JOptionPane.showMessageDialog(FindIdFrame.this,
                            "찾은 아이디: " + foundId,
                            "아이디 찾기 결과",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(FindIdFrame.this,
                            "일치하는 계정이 없습니다.",
                            "아이디 찾기 결과",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Enter 키 눌렀을 때도 검색 버튼을 클릭하도록 설정
        phonenumberTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findIdButton.doClick(); // 검색 버튼 누르기
            }
        });

        add(usernameLabel);
        add(usernameTextField);
        add(phonenumberLabel);
        add(phonenumberTextField);
        add(findIdButton);

        pack();
        setLocationRelativeTo(null); // 화면 중앙에 위치하도록 설정
    }

    // 아이디 찾기 메서드
    private String findId(String username, String phonenumber) {
        String query = "SELECT id FROM users WHERE username = ? AND phonenumber = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys", "admin", "song0303");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, phonenumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("id"); // 일치하는 아이디 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 일치하는 계정이 없는 경우 null 반환
    }
}
