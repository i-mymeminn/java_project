package 로그인화면소스코드분할;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class correctFrame {
    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";

    private String userID;

    public correctFrame(String userID) {
        this.userID = userID;
    }
    
    

    public void displayUserInfo() {
    	
    	Font nanumFont = null; // Initialize with a default value
    	
        try {
            nanumFont = Font.createFont(Font.TRUETYPE_FONT, new File("NanumGothic.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(nanumFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        
        // 프레임 생성
        JFrame frame = new JFrame("User Information");
        frame.setSize(350, 150);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 비밀번호 입력 필드 추가
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(nanumFont.deriveFont(Font.PLAIN, 12));

        // 나눔 폰트로 비밀번호 라벨 생성
        JLabel notice = new JLabel("사용자 확인을 위해 비밀번호를 입력해주세요");
        notice.setFont(nanumFont.deriveFont(Font.PLAIN, 15));
        frame.add(notice);
        frame.add(passwordField);

        // 로그인 버튼 추가
        JButton loginButton = new JButton("Login");
        loginButton.setFont(nanumFont.deriveFont(Font.PLAIN, 12));

        frame.add(loginButton);
        
        // 비밀번호 입력 필드에서 엔터 키를 누를 경우 로그인 버튼이 자동으로 눌리도록 설정
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

     // 로그인 버튼의 액션 리스너 설정
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredPassword = new String(passwordField.getPassword());
                if (checkPassword(enteredPassword)) {
                    DisplayUserInfoFrame frame = new DisplayUserInfoFrame(userID);
                    frame.displayUserInfoFrame();
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(loginButton); // 현재 프레임 가져오기
                    currentFrame.dispose(); // 현재 프레임 닫기
                } else {
                    JOptionPane.showMessageDialog(frame, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText(""); // 비밀번호 입력 필드 초기화
                }
            }
        });


        
        frame.getContentPane().setBackground(Color.WHITE);

        frame.setLocationRelativeTo(null); // 사용자 화면의 정 가운데로 위치
        frame.setVisible(true);
    }

    private boolean checkPassword(String enteredPassword) {
        String query = "SELECT * FROM users WHERE id = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userID);
            statement.setString(2, enteredPassword);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    
}


