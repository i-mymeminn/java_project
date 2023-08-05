//resetPwd
package 로그인화면소스코드분할;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ResetPasswordFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private JTextField nameText= new JTextField("이름");
    private JTextField phonenumberText= new JTextField("핸드폰 번호");
    private JTextField idText= new JTextField("학번 9자리");
    private JTextField birthdayText= new JTextField("생년월일 8자리");
    
    private JLabel background=new JLabel();
    private JPanel resetPanel = new JPanel();
    private JButton resetBtn = new JButton("비밀번호 재설정"); 

    private boolean membershipIsValid=false;
    public ResetPasswordFrame() {
        setTitle("비밀번호 재설정"); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        resetPanel.setLayout(null);
        resetPanel.setVisible(true);
        setContentPane(resetPanel);
        
        setIcon();
        actionListener();
        addFocusListeners();   
        enterTOtap();

        setSize(500,800);
        setLocationRelativeTo(null); 
        setVisible(true); 
        SwingUtilities.invokeLater(() -> resetBtn.requestFocusInWindow());

    }
    private void setIcon() {
       
       Font nanumFont = null; 
       
       try {
          nanumFont = Font.createFont(Font.TRUETYPE_FONT, new File("NanumGothic.ttf"));
          GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(nanumFont);
       } catch (IOException e) {
          e.printStackTrace();
       } catch (FontFormatException e) {
          e.printStackTrace();
       }
       
       idText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       nameText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       phonenumberText.setFont(nanumFont.deriveFont(Font.BOLD, 12));
       birthdayText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       resetBtn.setFont(nanumFont.deriveFont(Font.BOLD, 22));  

       nameText.setBorder(BorderFactory.createEmptyBorder());
       nameText.setBounds(140, 250, 220, 40);
       nameText.setForeground(Color.GRAY);
       nameText.setBorder(BorderFactory.createTitledBorder("YOUR NAME"));
       resetPanel.add(nameText);
        nameText.setOpaque(true);
       
        phonenumberText.setBorder(BorderFactory.createEmptyBorder());
        phonenumberText.setBounds(140, 300, 220, 40);
        phonenumberText.setForeground(Color.GRAY);
        phonenumberText.setBorder(BorderFactory.createTitledBorder("PHONE NUMBER"));
        resetPanel.add(phonenumberText);
        phonenumberText.setOpaque(true);
 
        idText.setBorder(BorderFactory.createEmptyBorder());
        idText.setBounds(140, 350, 220, 40);
        idText.setForeground(Color.GRAY);
        idText.setBorder(BorderFactory.createTitledBorder("ID"));
        resetPanel.add(idText);
        idText.setOpaque(true);
   
        birthdayText.setBorder(BorderFactory.createEmptyBorder());
        birthdayText.setBounds(140, 400, 220, 40);
        birthdayText.setForeground(Color.GRAY);
        birthdayText.setBorder(BorderFactory.createTitledBorder("BIRTHDAY"));
        resetPanel.add(birthdayText);
        birthdayText.setOpaque(true);
        
        resetBtn.setBackground(new Color(5,30,150));
        resetBtn.setBounds(140, 450, 220, 60);
        resetBtn.setForeground(Color.white);
        resetPanel.add(resetBtn);
        
        background.setIcon(new ImageIcon("resetPwd.jpg"));
        background.setBounds(0, 0, 500, 800);
        resetPanel.add(background);   
        
    }
    private void addFocusListeners() {
        idText.addFocusListener(new FocusListener(){
           public void focusLost(FocusEvent e) {         //포커스를 잃으면         
              if(idText.getText().trim().length()==0) {   //입력 값의 길이==0이면
                 idText.setText("학번 9자리");               //해당텍스트 다시 출력
                 idText.setForeground(Color.GRAY);
              }
           }
           public void focusGained(FocusEvent e) {         //포커스를 얻으면
              if(idText.getText().trim().equals("학번 9자리")) {//해당텍스트가 학번인 경우
                 idText.setText("");                  //빈칸으로 표시해주기
                 idText.setForeground(Color.BLACK);

              }            
           }
        }); 

        nameText.addFocusListener(new FocusListener() {
           public void focusLost(FocusEvent e) {
              if(nameText.getText().trim().length()==0) {
                 nameText.setText("이름");
                 nameText.setForeground(Color.GRAY);

              }
           }
           
           public void focusGained(FocusEvent e) {         
              if(nameText.getText().trim().equals("이름")) {
                 nameText.setText("");
                 nameText.setForeground(Color.BLACK);
              }
           }
        });
        

        phonenumberText.addFocusListener(new FocusListener() {
           public void focusLost(FocusEvent e) {
              if(phonenumberText.getText().trim().length()==0) {
                 phonenumberText.setText("핸드폰 번호");
                 phonenumberText.setForeground(Color.GRAY);
              }
           }
           public void focusGained(FocusEvent e) {
              if(phonenumberText.getText().trim().equals("핸드폰 번호")) {
                 phonenumberText.setText("");
                 phonenumberText.setForeground(Color.BLACK);
              }
           }
        });
        birthdayText.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
               if(birthdayText.getText().trim().length()==0) {
                  birthdayText.setText("생년월일 8자리");
                  birthdayText.setForeground(Color.GRAY);
               }
            }
            public void focusGained(FocusEvent e) {
               if(birthdayText.getText().trim().equals("생년월일 8자리")) {
                  birthdayText.setText("");
                  birthdayText.setForeground(Color.BLACK);
               }
            }
         });
     }
    private void actionListener() {
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               checkValue();//멤버쉽 칸이 모두 차있는 지
               if(membershipIsValid) {
                   String username = nameText.getText(); // 텍스트 필드에서 입력한 사용자 이름 가져오기
                   String phonenumber = phonenumberText.getText(); // 텍스트 필드에서 입력한 생년월일 가져오기
                   String id = idText.getText(); // 텍스트 필드에서 입력한 학번 가져오기
                   String birthday= birthdayText.getText();
                   membershipIsValid=false;
                  
                   // 비밀번호 재설정 로직
                   boolean resetSuccessful = checkPasswordResetInfo(username, birthday, phonenumber, id); // 비밀번호 재설정 메서드 호출
                   if (resetSuccessful) {
                       String newPassword = promptNewPassword(); // 새 비밀번호를 입력 받는다
                       if (newPassword != null) {
                           boolean updateSuccessful = updatePassword(username, birthday, phonenumber, id, newPassword); // 비밀번호 업데이트 메서드 호출
                           if (updateSuccessful) {
                               JOptionPane.showMessageDialog(ResetPasswordFrame.this,
                                       "비밀번호가 재설정되었습니다.",
                                       "비밀번호 재설정",
                                       JOptionPane.INFORMATION_MESSAGE);
                           } 
                           else {
                               JOptionPane.showMessageDialog(ResetPasswordFrame.this,
                                       "비밀번호 업데이트 중 오류가 발생했습니다.",
                                       "비밀번호 재설정",
                                       JOptionPane.ERROR_MESSAGE);
                           }
                       }
                   } 
                   else {
                       JOptionPane.showMessageDialog(ResetPasswordFrame.this,
                               "계정 정보가 일치하지 않습니다.",
                               "비밀번호 재설정",
                               JOptionPane.ERROR_MESSAGE);
                   }
               }
            }
        });
        
    }
    private void enterTOtap() {
       birthdayText.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             resetBtn.doClick(); // 재설정 버튼 클릭
          }
       });
    }
    private void checkValue() {
             if(nameText.getText().trim().length()==0 || nameText.getText().trim().equals("이름")) {
                JOptionPane.showMessageDialog(null, "이름을 입력해 주세요.", "이름 입력", JOptionPane.WARNING_MESSAGE);
                nameText.grabFocus();
                return;
              }

             if(phonenumberText.getText().trim().length()!= 11 || phonenumberText.getText().trim().equals("핸드폰 번호")) {
                JOptionPane.showMessageDialog(null, "핸드폰 번호를 11자리를 제대로 입력해 주세요.", "핸드폰 번호 입력", JOptionPane.WARNING_MESSAGE);
                phonenumberText.grabFocus();
                return;
             }
             if(idText.getText().trim().length()!=9||idText.getText().trim().equals("학번")) {
                JOptionPane.showMessageDialog(null, "학번 9자리를 제대로 입력해 주세요.", "학번 입력", JOptionPane.WARNING_MESSAGE);
                idText.grabFocus();
                return;                
             }
             if(birthdayText.getText().trim().length()!=8 || birthdayText.getText().trim().equals("생년월일 8자리")) {
                JOptionPane.showMessageDialog(null, "생년월일 8자리를 제대로 입력해 주세요.", "이름 입력", JOptionPane.WARNING_MESSAGE);
                birthdayText.grabFocus();
                return;
             }
             
            membershipIsValid=true;
            return;
           
    }
    private boolean checkPasswordResetInfo(String username, String birthday, String phonenumber, String id) {
        String query = "SELECT * FROM users WHERE username = ? AND birthday = ? AND phonenumber = ? AND id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys", "admin", "song0303");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, birthday);
            statement.setString(3, phonenumber);
            statement.setString(4, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true; // 계정 정보 일치
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 계정 정보 불일치 또는 오류 발생
    }


    private String promptNewPassword() {
       
        JPanel panel = new JPanel();
        JPasswordField newPasswordField = new JPasswordField(20); // 20자를 입력할 수 있는 비밀번호 필드 생성
        JPasswordField confirmPasswordField = new JPasswordField(20); // 20자를 입력할 수 있는 비밀번호 필드 생성
        JLabel promptBackground = new JLabel();
        JLabel label = new JLabel("비밀번호는 8자리 이상 입력해주세요.");
        label.setBackground(Color.WHITE);
        label.setOpaque(true); // 배경색이 표시되도록 불투명 설정
        label.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트를 가운데 정렬
        

        newPasswordField.setBorder(BorderFactory.createTitledBorder("New Password"));
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder("Confirm Password"));
        panel.setLayout(new GridLayout(4, 1)); 
        promptBackground.setIcon(new ImageIcon("loginPictures/prompt.png"));
        panel.add(promptBackground);
        panel.add(newPasswordField); // 새 비밀번호 필드 추가
        panel.add(confirmPasswordField); // 비밀번호 확인 필드 추가
        panel.add(label);

        int option = JOptionPane.showOptionDialog(ResetPasswordFrame.this, panel,
                "New Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        
        if (option == JOptionPane.OK_OPTION) {
            char[] newPasswordChars = newPasswordField.getPassword();
            char[] confirmPasswordChars = confirmPasswordField.getPassword();
            
            String newPassword = new String(newPasswordChars);
            String confirmPassword = new String(confirmPasswordChars);
            
            if (newPassword.equals(confirmPassword)) {
                if (newPassword.length() >= 8) {
                    return newPassword;
                } else {
                    JOptionPane.showMessageDialog(ResetPasswordFrame.this,
                            "비밀번호는 최소 8자리 이상이어야 합니다.",
                            "비밀번호 재설정",
                            JOptionPane.WARNING_MESSAGE);
                    return promptNewPassword(); // 새 비밀번호를 다시 입력 받음
                }
            } else {
                JOptionPane.showMessageDialog(ResetPasswordFrame.this,
                        "비밀번호가 일치하지 않습니다. 다시 입력해주세요.",
                        "비밀번호 재설정",
                        JOptionPane.ERROR_MESSAGE);
                return promptNewPassword(); // 새 비밀번호를 다시 입력 받음
            }
        }
        
        return null;
    }



    private boolean updatePassword(String username, String birthday, String phonenumber, String id, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE username = ? AND birthday = ? AND phonenumber = ? AND id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys", "admin", "song0303");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setString(2, username);
            statement.setString(3, birthday);
            statement.setString(4, phonenumber);
            statement.setString(5, id);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true; // 비밀번호 업데이트 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 계정 정보 불일치 또는 비밀번호 업데이트 실패
    }
}