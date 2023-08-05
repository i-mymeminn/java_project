//회원가입
package 로그인화면소스코드분할;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class UserRegistrationFrame extends JFrame implements Serializable {
   
    private static final long serialVersionUID = 1L;
    private JPanel registerPanel=new JPanel();
    
    private JTextField idText = new JTextField("학번 9자리");
      private JPasswordField pwText = new JPasswordField();
    private JPasswordField pwCheckText = new JPasswordField();
    private JTextField nameText = new JTextField("이름");
   
    private String[] birthMonthContent = new String[13];
    private String[] birthYearContent = new String[101];
    private JComboBox<String> birthYearList;
    private JComboBox<String> birthMonthList;
    private String[] birthDayContent = new String[32];
    private JComboBox<String> birthDayList;
   
    private JTextField phonenumberText=new JTextField("핸드폰 번호 11자리");
    private JButton registerBtn=new JButton("회원가입");
    private JLabel instructionsLabel = new JLabel("<html><ul>" +
           "<li>학번은 9자리로 입력해주세요</li>" +
           "<li>비밀번호는 8자리 이상 입력해주세요</li>" +
           "<li>핸드폰번호는 11자리로 입력해주세요</li>" +
           "</ul></html>");
    private JLabel background=new JLabel();

    public UserRegistrationFrame() {
       
       
       pwText.setEchoChar('\u0000');
       pwText.setText("비밀번호");
       pwCheckText.setEchoChar('\u0000');
        pwCheckText.setText("비밀번호 확인");
        
        setTitle("회원가입");        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setContentPane(registerPanel);   //sighUpPanel 패널의 내용으로 설정-> 화면에 표시할 위치 지정
        setSize(500,800);            
        setLocationRelativeTo(null);   //창 화면을 중앙에 뜨도록

        registerPanel.setLayout(null);
        registerPanel.setVisible(true);
        setContentPane(registerPanel);
      
        setList();
        setIcon();
        addFocusListeners();
        checkValue();
        enterTOtap();
        SwingUtilities.invokeLater(() -> registerBtn.requestFocusInWindow());

    }
    private void enterTOtap() {
       phonenumberText.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             registerBtn.doClick();
          }
       });
    }
    private void setList() {
        birthYearContent[0] = "생년";
        for (int i = 1; i < 100; i++) {
            birthYearContent[i] = Integer.toString(2005 - i);
        }
        birthYearList = new JComboBox<>(birthYearContent);
        birthYearList.setEditor(new CustomComboBoxEditor());
        birthYearList.setRenderer(new CustomComboBoxRenderer());

        birthMonthContent[0] = "월";
        for(int i=1; i<13; i++) {
            String Month = (i < 10) ? "0" + i : Integer.toString(i);
             birthMonthContent[i] = Month;
        }
        birthMonthList = new JComboBox<>(birthMonthContent);
        birthMonthList.setEditor(new CustomComboBoxEditor());
        birthMonthList.setRenderer(new CustomComboBoxRenderer());

        birthDayContent[0] = "일";
        for (int i = 1; i < 32; i++) {
            String day = (i < 10) ? "0" + i : Integer.toString(i);
            birthDayContent[i] = day;
        }

        birthDayList = new JComboBox<>(birthDayContent);
        birthDayList.setEditor(new CustomComboBoxEditor());
        birthDayList.setRenderer(new CustomComboBoxRenderer());

    }

    private void setIcon() {
       Font nanumFont = null; // Initialize with a default value
       
       try {
          nanumFont = Font.createFont(Font.TRUETYPE_FONT, new File("NanumGothic.ttf"));
          GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(nanumFont);
       } catch (IOException e) {
          e.printStackTrace();
       } catch (FontFormatException e) {
          e.printStackTrace();
       }
       
       idText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       pwText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       pwCheckText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       nameText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       phonenumberText.setFont(nanumFont.deriveFont(Font.BOLD, 12));  
       registerBtn.setFont(nanumFont.deriveFont(Font.BOLD, 22));  
       instructionsLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12));

       idText.setBorder(BorderFactory.createEmptyBorder());
        idText.setBounds(140, 250,220, 40);
        idText.setBorder(BorderFactory.createTitledBorder("ID"));

        idText.setForeground(Color.GRAY);
        registerPanel.add(idText);
        idText.setOpaque(true);
        
        
        pwText.setBorder(BorderFactory.createEmptyBorder());
        pwText.setBounds(140, 300, 220, 40);
        pwText.setForeground(Color.GRAY);
        registerPanel.add(pwText);
        pwText.setOpaque(true);
        pwText.setBorder(BorderFactory.createTitledBorder("PASSWORD"));
        
        
        pwCheckText.setBorder(BorderFactory.createEmptyBorder());
        pwCheckText.setBounds(140, 350, 220, 40);
        pwCheckText.setForeground(Color.GRAY);
        registerPanel.add(pwCheckText);
        pwCheckText.setOpaque(true);
        pwCheckText.setBorder(BorderFactory.createTitledBorder("PASSWORD CHECK"));

        
        nameText.setBorder(BorderFactory.createEmptyBorder());
        nameText.setBounds(140, 400, 220, 40);
        nameText.setForeground(Color.GRAY);
        registerPanel.add(nameText);
        nameText.setOpaque(true);
        nameText.setBorder(BorderFactory.createTitledBorder("YOUR NAME"));


        birthYearList.setBorder(BorderFactory.createEmptyBorder());
        birthYearList.setBounds(140, 450, 70, 40);
        registerPanel.add(birthYearList);
        birthYearList.setOpaque(true);
        
        birthMonthList.setBorder(BorderFactory.createEmptyBorder());
        birthMonthList.setBounds(215, 450, 70, 40);
        registerPanel.add(birthMonthList);
        birthMonthList.setOpaque(true);
        
        birthDayList.setBorder(BorderFactory.createEmptyBorder());
        birthDayList.setBounds(290, 450, 70, 40);
        registerPanel.add(birthDayList);
        birthDayList.setOpaque(true);
        
        phonenumberText.setBorder(BorderFactory.createEmptyBorder());
        phonenumberText.setBounds(140, 500, 220, 40);
        phonenumberText.setForeground(Color.GRAY);
        registerPanel.add(phonenumberText);
        phonenumberText.setOpaque(true);
        phonenumberText.setBorder(BorderFactory.createTitledBorder("PHONE NUMBER"));
        
        
        registerBtn.setBackground(new Color(5,30,150));
        registerBtn.setBounds(140, 550, 220, 60);
        registerBtn.setForeground(Color.white);
        registerPanel.add(registerBtn);
        
        instructionsLabel.setBounds(100,600, 500, 100);
        registerPanel.add(instructionsLabel);
      
        background.setIcon(new ImageIcon("register.jpg"));
        background.setBounds(0, 0, 500, 800);
        registerPanel.add(background);
    }
    private void registerUser(String username, String birthday, String phoneNumber, String id, String password) {
        // 학번 중복 체크
        if (UserDataStorage.isIdExists(id)) {
            JOptionPane.showMessageDialog(UserRegistrationFrame.this,
                    "이미 가입된 학번입니다.",
                    "회원가입",
                    JOptionPane.ERROR_MESSAGE);
        }
        // 회원 데이터 저장
        UserDataStorage.saveUser(username, birthday, phoneNumber, id, password);
        
        JOptionPane.showMessageDialog(UserRegistrationFrame.this,
                "회원가입이 완료되었습니다.",
                "회원가입",
                JOptionPane.INFORMATION_MESSAGE);

        dispose(); // 현재 프레임을 닫기 위해 dispose() 호출
        
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
       pwText.addFocusListener(new FocusAdapter() {
          private boolean isPasswordSet = false;
          public void focusLost(FocusEvent e) {
             String enteredPassword = new String(pwText.getPassword());
             if (enteredPassword.isEmpty()) {
                pwText.setText("비밀번호");
                pwText.setEchoChar('\u0000');
                isPasswordSet = false; 
                pwText.setForeground(Color.GRAY);

             }
          }
          
          public void focusGained(FocusEvent e) {
             String enteredPassword = new String(pwText.getPassword());
             if (!isPasswordSet || enteredPassword.equals("비밀번호")) {
                pwText.setEchoChar('*');
                pwText.setText("");
                isPasswordSet = true;
                pwText.setForeground(Color.BLACK);
             }
          }
       });
       pwCheckText.addFocusListener(new FocusAdapter() {
          private boolean isPasswordSet = false;
          
          public void focusLost(FocusEvent e) {
             String enteredPassword = new String(pwCheckText.getPassword());
             if (enteredPassword.isEmpty()) {
                pwCheckText.setText("비밀번호 확인");
                pwCheckText.setEchoChar('\u0000');
                isPasswordSet = false; 
                pwCheckText.setForeground(Color.GRAY);

             }
          }
          public void focusGained(FocusEvent e) {
             String enteredPassword = new String(pwCheckText.getPassword());
             if (!isPasswordSet || enteredPassword.equals("비밀번호 확인")) {
                pwCheckText.setEchoChar('*');
                pwCheckText.setText("");
                isPasswordSet = true;
                pwCheckText.setForeground(Color.BLACK);
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
                phonenumberText.setText("핸드폰 번호 11자리");
                phonenumberText.setForeground(Color.GRAY);

             }
          }
          public void focusGained(FocusEvent e) {
             if(phonenumberText.getText().trim().equals("핸드폰 번호 11자리")) {
                phonenumberText.setText("");
                phonenumberText.setForeground(Color.BLACK);

             }
          }
       });
    }
    
    
    private void checkValue() {
       registerBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
        	  char[] passwordChars = pwText.getPassword();
              String password = new String(passwordChars);
              String checkpassword = new String(passwordChars);
              

             if(idText.getText().trim().length()<9||idText.getText().trim().equals("학번")) {
                JOptionPane.showMessageDialog(null, "학번 9자리를 제대로 입력해 주세요.", "학번 입력", JOptionPane.WARNING_MESSAGE);
                idText.grabFocus();
             }
             
             else if (password.trim().length() == 0) {
                 JOptionPane.showMessageDialog(null, "비밀번호를 입력해 주세요.", "비밀번호 입력", JOptionPane.WARNING_MESSAGE);
                 pwText.grabFocus();
             }

	         else if(password.trim().length()<8) {
	        	 JOptionPane.showMessageDialog(null, "비밀번호는 8자리 이상이어야 합니다.", "비밀번호 입력", JOptionPane.WARNING_MESSAGE);
	             pwText.grabFocus();
	         }
            
            else if(checkpassword.trim().length()==0) {
               JOptionPane.showMessageDialog(null, "비밀번호 확인을 입력해 주세요.", "비밀번호 확인 입력", JOptionPane.WARNING_MESSAGE);
               pwCheckText.grabFocus();
            }
            
            else if(!(password.trim().equals(checkpassword.trim()))) {
               JOptionPane.showMessageDialog(null, "비밀번호가 같지 않습니다.!!", "비밀번호 확인", JOptionPane.WARNING_MESSAGE);
            }
            
            else if(nameText.getText().trim().length()==0 || nameText.getText().trim().equals("이름")) {
               JOptionPane.showMessageDialog(null, "이름을 입력해 주세요.", "이름 입력", JOptionPane.WARNING_MESSAGE);
               nameText.grabFocus();
            }
            
            else if(birthYearList.getSelectedIndex()==0) {
               JOptionPane.showMessageDialog(null, "생년월일의 생일 년도를 선택해 주세요.", "생일 년도 입력", JOptionPane.WARNING_MESSAGE);
               birthYearList.grabFocus();
            }
                        
            else if(birthMonthList.getSelectedIndex()==0) {
               JOptionPane.showMessageDialog(null, "생년월일의 월을 선택해 주세요.", "월 입력", JOptionPane.WARNING_MESSAGE);
               birthMonthList.grabFocus();
            }
            
            else if(birthDayList.getSelectedIndex()==0) {
               JOptionPane.showMessageDialog(null, "생일 일자를 선택해 주세요.", "생일 일자 입력", JOptionPane.WARNING_MESSAGE);
               birthDayList.grabFocus();
            }
            
            else if(phonenumberText.getText().trim().length()!= 11 || phonenumberText.getText().trim().equals("핸드폰 번호 11자리")) {
               JOptionPane.showMessageDialog(null, "핸드폰 번호를 11자리를 제대로 입력해 주세요.", "핸드폰 번호 11자리 입력", JOptionPane.WARNING_MESSAGE);
               phonenumberText.grabFocus();
            }
            
            
            else {
               String username = nameText.getText();
                String phoneNumber = phonenumberText.getText();
                String id = idText.getText();
                String pwd = new String(pwCheckText.getPassword());
                
                String year = (String) birthYearList.getSelectedItem();
                String month = (String) birthMonthList.getSelectedItem();
                String day = (String) birthDayList.getSelectedItem();
                String birthday = year + month + day; 
                
                registerUser(username,birthday, phoneNumber, id, pwd);
            }
         }
      });
   }
}