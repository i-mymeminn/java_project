package 로그인화면소스코드분할;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import javax.swing.JButton;


public class DisplayUserInfoFrame {
    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";

    private String userID;

    public DisplayUserInfoFrame(String userID) {
        this.userID = userID;
    }


    public void displayUserInfoFrame() {
        // 프레임 생성
        JFrame frame = new JFrame("User Information");
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setSize(800, 550);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon logoIcon = new ImageIcon("main_icon.png");
        frame.setIconImage(logoIcon.getImage());

        // topbackground 이미지 추가
        ImageIcon topBackgroundIcon = new ImageIcon("topbackgroundsmal.png");
        JLabel topBackgroundLabel = new JLabel(topBackgroundIcon);
        topBackgroundLabel.setBounds(0, 0, topBackgroundIcon.getIconWidth(), topBackgroundIcon.getIconHeight());
        frame.getContentPane().add(topBackgroundLabel);

        // 데이터베이스로부터 자신의 데이터를 가져옴
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String phonenumber = resultSet.getString("phonenumber");
                int point = resultSet.getInt("point");

                Font nanumFont = null; // Initialize with a default value

                try {
                    nanumFont = Font.createFont(Font.TRUETYPE_FONT, new File("NanumGothic.ttf"));
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(nanumFont);
                } catch (IOException | FontFormatException e) {
                    e.printStackTrace();
                }

                // 사용자 정보 표시를 위한 라벨과 텍스트필드 추가

                JLabel basic = new JLabel("사용자 기본 정보");
                basic.setBounds(50, 130, 200, 30);
                basic.setFont(nanumFont.deriveFont(Font.BOLD, 21)); // Set the font for the label
                frame.add(basic);

                JLabel idLabel = new JLabel("학번 : " + userID);
                idLabel.setBounds(50, 180, 200, 30);
                idLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(idLabel);

                JLabel nameLabel = new JLabel("사용자 이름 : " + username);
                nameLabel.setBounds(50, 230, 200, 30);
                nameLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(nameLabel);

                JLabel phonenumberLabel = new JLabel("전화번호 : " + phonenumber);
                phonenumberLabel.setBounds(50, 280, 200, 30);
                phonenumberLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(phonenumberLabel);

                JLabel pointLabel = new JLabel("JB포인트 : " + point);
                pointLabel.setBounds(50, 330, 200, 30);
                pointLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(pointLabel);
                
                // 기본 배송지 라벨
                JLabel defaultAddressLabel = new JLabel("기본 배송지:");
                defaultAddressLabel.setBounds(50, 380, 100, 30);
                defaultAddressLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(defaultAddressLabel);

                // 배송지 입력란
                JTextField addressTextField = new JTextField();
                addressTextField.setBounds(170, 380, 350, 30);
                
                

                // 데이터베이스에서 주소를 불러와서 입력란에 설정
                String address = getAddress(userID); // 데이터베이스에서 주소를 가져오는 메서드 호출
                addressTextField.setText(address);

                frame.add(addressTextField);
                
             // 변경 버튼
                JButton changeButton = new JButton("변경");
                changeButton.setBounds(530, 380, 100, 30);
                frame.add(changeButton);
                
                changeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String enteredAddress = addressTextField.getText(); // 변경된 주소

                        // 주소 업데이트
                        boolean isAddressUpdated = updateAddress(userID, enteredAddress);

                        if (isAddressUpdated) {
                            // 주소 업데이트 성공
                            JOptionPane.showMessageDialog(frame, "변경이 완료되었습니다"); // 알림 메시지                   
                        } else {
                        }
                    }
                });

                
                

                JLabel passwordchangeLabel = new JLabel("새 비밀번호");
                passwordchangeLabel.setBounds(400, 180, 200, 30);
                passwordchangeLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(passwordchangeLabel);

                JLabel passwordconfirm = new JLabel("새 비밀번호 확인");
                passwordconfirm.setBounds(400, 230, 200, 30);
                passwordconfirm.setFont(nanumFont.deriveFont(Font.PLAIN, 15)); // Set the font for the label
                frame.add(passwordconfirm);

                JPasswordField firstpasswordfield = new JPasswordField();
                firstpasswordfield.setBounds(530, 180, 150, 30);
                frame.add(firstpasswordfield);

                JPasswordField repasswordfield = new JPasswordField();
                repasswordfield.setBounds(530, 230, 150, 30);
                frame.add(repasswordfield);

                // 비밀번호 조건을 표시하는 라벨
                JLabel conditionLabel = new JLabel("8자리 이상으로 비밀번호를 입력해주세요");
                conditionLabel.setBounds(530, 280, 300, 30);
                conditionLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
                conditionLabel.setForeground(new Color(255, 0, 51)); // RGB 값: FF0033
                frame.add(conditionLabel);

                // 비밀번호 일치 여부를 표시하는 라벨
                JLabel matchLabel = new JLabel("");
                matchLabel.setBounds(530, 310, 200, 30);
                matchLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
                matchLabel.setForeground(new Color(0, 204, 51)); // RGB 값: 00CC33
                frame.add(matchLabel);

                // 비밀번호 일치 여부를 체크하는 메소드
                DocumentListener documentListener = new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        checkPasswordMatch();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        checkPasswordMatch();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        checkPasswordMatch();
                    }

                    private void checkPasswordMatch() {
                        String password1 = new String(firstpasswordfield.getPassword());
                        String password2 = new String(repasswordfield.getPassword());

                        if (password1.length() < 8) {
                            conditionLabel.setForeground(new Color(255, 0, 51)); // RGB 값: FF0033
                            matchLabel.setText("");
                        } else {
                            conditionLabel.setForeground(new Color(0, 204, 51)); // RGB 값: 00CC33
                            if (password2.isEmpty()) {
                                matchLabel.setText("");
                            } else if (password1.equals(password2)) {
                                matchLabel.setText("비밀번호가 일치합니다.");
                                matchLabel.setForeground(new Color(0, 204, 51)); // RGB 값: 00CC33
                            } else {
                                matchLabel.setText("비밀번호가 일치하지 않습니다.");
                                matchLabel.setForeground(new Color(255, 0, 51)); // RGB 값: FF0033
                            }
                        }
                    }
                };

                firstpasswordfield.getDocument().addDocumentListener(documentListener);
                repasswordfield.getDocument().addDocumentListener(documentListener);



                // 보기 버튼 생성
                JToggleButton showPasswordButton1 = new JToggleButton("보기");
                showPasswordButton1.setBounds(700, 180, 70, 30);
                frame.add(showPasswordButton1);

                // 보기 버튼의 액션 리스너 설정
                showPasswordButton1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (showPasswordButton1.isSelected()) {
                            firstpasswordfield.setEchoChar((char) 0); // 텍스트가 표시되도록 설정
                        } else {
                            firstpasswordfield.setEchoChar('\u25cf'); // 텍스트를 가려서 표시되도록 설정
                        }
                    }
                });

                JToggleButton showPasswordButton2 = new JToggleButton("보기");
                showPasswordButton2.setBounds(700, 230, 70, 30);
                frame.add(showPasswordButton2);

                // 보기 버튼의 액션 리스너 설정
                showPasswordButton2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (showPasswordButton2.isSelected()) {
                            repasswordfield.setEchoChar((char) 0); // 텍스트가 표시되도록 설정
                        } else {
                            repasswordfield.setEchoChar('\u25cf'); // 텍스트를 가려서 표시되도록 설정
                        }
                    }
                });
                
                JButton correct = new JButton("확인");
                correct.setBounds(650, 450, 100, 40); // x, y, width, height 설정
                correct.setFont(nanumFont.deriveFont(Font.BOLD, 17)); // 글씨체 설정
                frame.add(correct);
                
                correct.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (matchLabel.getText().equals("비밀번호가 일치합니다.")) {
                        	char[] passwordChars = firstpasswordfield.getPassword();
                        	String enteredPassword = new String(passwordChars);

                            
                            String enteredAddress = addressTextField.getText(); // 배송지 입력란의 값
                            // 비밀번호 및 주소 업데이트
                            boolean isPasswordUpdated = updatePassword(userID, enteredPassword);
                            boolean isAddressUpdated = updateAddress(userID, enteredAddress);

                            if (isPasswordUpdated && isAddressUpdated) {
                                JOptionPane.showMessageDialog(frame, "정보가 업데이트 되었습니다");
                             // Reset password fields
                                firstpasswordfield.setText("");
                                repasswordfield.setText("");
                            }
                            else {
                                JOptionPane.showMessageDialog(frame, "예기치 못한 오류가 발생하였습니다");
                            }

                        } else {
                            // 비밀번호가 일치하지 않음
                            JOptionPane.showMessageDialog(frame, "비밀번호를 정상적으로 입력해주세요");
                        }
                    }
                });


                JButton cancel = new JButton("취소");
                cancel.setBounds(530, 450, 100, 40); // x, y, width, height 설정
                cancel.setFont(nanumFont.deriveFont(Font.BOLD, 17)); // 글씨체 설정
                frame.add(cancel);
                
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 취소 버튼 동작 구현
                        frame.dispose(); // 현재 창 닫기
                    }
                });

                // 버튼의 배경색과 글자색 설정
                correct.setForeground(new Color(51, 51, 102)); // 글씨 색 설정

                cancel.setForeground(Color.GRAY); // 글씨 색 설정


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        frame.setLocationRelativeTo(null); // 사용자 화면의 정 가운데로 위치
        frame.setVisible(true);
    }
    
    private boolean updatePassword(String id, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setString(2, id);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true; // 비밀번호 업데이트 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 비밀번호 업데이트 실패
    }

    private boolean updateAddress(String id, String newAddress) {
        String query = "UPDATE users SET address = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newAddress);
            statement.setString(2, id);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true; // 주소 업데이트 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 주소 업데이트 실패
    }
    
    private String getAddress(String id) {
        String address = null;
        String query = "SELECT address FROM users WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                address = resultSet.getString("address");
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return address;
    }

}
