package 로그인화면소스코드분할;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private JTextField couponTextField;
    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";

    public CouponFrame(String userID) { // Add userID as a parameter
        setTitle("Coupon Frame");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /*나눔 폰트 추가하는 부분*/
        Font nanumFont = null; // Initialize with a default value

        try {
            nanumFont = Font.createFont(Font.TRUETYPE_FONT, new File("NanumGothic.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(nanumFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
       
        
        /*로고 이미지 삽입*/
        ImageIcon logoIcon = new ImageIcon("main_icon.png");
        this.setIconImage(logoIcon.getImage());

        getContentPane().setBackground(Color.WHITE); // Set the background color of the frame to white

        setLayout(null);

        couponTextField = new JTextField();
        couponTextField.setBounds(50, 30, 200, 30);
        add(couponTextField);

        // Label to display user's point
        JLabel userPointLabel = new JLabel();
        userPointLabel.setBounds(50, 60, 300, 30);
        userPointLabel.setFont(nanumFont.deriveFont(Font.BOLD, 13)); // Set the font for the label
        add(userPointLabel);

        // Get user's point from database
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String query = "SELECT point FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userPoint = resultSet.getInt("point");
                userPointLabel.setText("보유중인 포인트 : " + userPoint);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        JButton confirmButton = new JButton("코드 교환");
        confirmButton.setBounds(260, 30, 100, 30);
        confirmButton.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
        add(confirmButton);

        //쿠폰 이름
        JLabel couponNameLabel = new JLabel();
        couponNameLabel.setBounds(50, 130, 300, 30);
        couponNameLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
        add(couponNameLabel);
        //쿠폰 포인트
        JLabel couponPointLabel = new JLabel();
        couponPointLabel.setBounds(50, 180, 300, 30);
        couponPointLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
        add(couponPointLabel);

        JButton useCouponButton = new JButton("쿠폰 사용하기");
        useCouponButton.setBounds(50, 290, 150, 30);
        useCouponButton.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
        useCouponButton.setForeground(new Color(51, 51, 102));
        add(useCouponButton);
        useCouponButton.setVisible(false); // Initially hidden

        JButton cancelButton = new JButton("취소");
        cancelButton.setBounds(280, 290, 80, 30);
        cancelButton.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
        add(cancelButton);
        cancelButton.setVisible(false); // Initially hidden
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 현재 프레임 닫기
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(cancelButton);
                frame.dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String couponCode = couponTextField.getText();

                try {
                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    String query = "SELECT couponname, couponpoint FROM coupon WHERE coupondata = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, couponCode);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String couponName = resultSet.getString("couponname");
                        int couponPoint = resultSet.getInt("couponpoint");

                        couponNameLabel.setText("쿠폰 이름: " + couponName);
                        couponPointLabel.setText("쿠폰 포인트: " + couponPoint);

                        useCouponButton.setVisible(true);
                        cancelButton.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "잘못된 코드입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        couponTextField.setText("");
                        couponNameLabel.setText("");
                        couponPointLabel.setText("");
                        useCouponButton.setVisible(false);
                        cancelButton.setVisible(false);
                    }

                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        useCouponButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "정말 사용하시겠습니까?", "쿠폰 사용", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                        // Get the coupon point
                        String couponCode = couponTextField.getText();
                        String query = "SELECT couponpoint FROM coupon WHERE coupondata = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, couponCode);
                        ResultSet resultSet = statement.executeQuery();

                        int couponPoint = 0;
                        if (resultSet.next()) {
                            couponPoint = resultSet.getInt("couponpoint");
                        }

                        // Update the user's point
                        query = "UPDATE users SET point = point + ? WHERE id = ?";
                        statement = connection.prepareStatement(query);
                        statement.setInt(1, couponPoint);
                        statement.setString(2, userID);
                        statement.executeUpdate();

                        // Delete the used coupon
                        query = "DELETE FROM coupon WHERE coupondata = ?";
                        statement = connection.prepareStatement(query);
                        statement.setString(1, couponCode);
                        statement.executeUpdate();

                        JOptionPane.showMessageDialog(null, "쿠폰이 성공적으로 사용되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        
                     // 현재 프레임 닫기
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(useCouponButton);
                        frame.dispose();

                        resultSet.close();
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        this.setLocationRelativeTo(null); // 위치를 화면 중앙으로 설정
        setVisible(true);
    }
}
