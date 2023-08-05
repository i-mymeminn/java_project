package 로그인화면소스코드분할;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private BookInformation bookInformationFrame;
    private String loggedInUserID;
    
    private JPanel loginPanel = new JPanel();

    private JLabel idLabel = new JLabel("학번 ");
    private JLabel pwLabel = new JLabel("비밀번호 ");
    private JTextField idText = new JTextField();
    private JPasswordField pwText = new JPasswordField();

    private JButton loginBtn = new JButton("로그인");
    private JButton idSearchBtn = new JButton("");
    private JButton pwReBtn =new JButton("비밀번호 재설정");
    private JButton registBtn = new JButton("회원 가입");

    private JLabel background=new JLabel();
    
    

    public LoginFrame() {
    	
        ImageIcon logoIcon = new ImageIcon("main_icon.png");
        this.setIconImage(logoIcon.getImage());

        setTitle("로그인");
        setIcon();
        check();
        cursor();
        actionlistener();
        setSize(1200,800);
        setLocationRelativeTo(null);
        setResizable(false);

        loginPanel.setLayout(null);
        loginPanel.setVisible(true);
        setContentPane(loginPanel);
    }
    
    private void actionlistener() {
       idText.addFocusListener(new FocusAdapter() {
           @Override
           public void focusGained(FocusEvent e) {
               idText.setFocusTraversalKeysEnabled(false);
           }
       });
       idText.addKeyListener(new KeyAdapter() {
           @Override
           public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_TAB) {
                   e.consume();  // 기본 탭 행동을 막음
                   pwText.requestFocus();  // 비밀번호 입력 필드로 포커스 이동
               }
           }
       });
       pwText.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             loginBtn.doClick();
          }
       });
    }
    private void check() {

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idText.getText();
                String password = new String(pwText.getPassword());

                if (checkUserCredentials(id, password)) {
                    loggedInUserID = id;

                    JOptionPane.showMessageDialog(LoginFrame.this,
                            id + "님, 로그인 성공!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    openBookInformationFrame();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "ID/비밀번호 오류입니다.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        idSearchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFindIdFrame();
            }
        });

        pwReBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openResetPasswordFrame();
            }
        });

        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationFrame();
            }
        });
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
       idLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
       pwLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
       idText.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
       pwText.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
       
       loginBtn.setFont(nanumFont.deriveFont(Font.BOLD, 18)); 
       idSearchBtn.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
       pwReBtn.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
       registBtn.setFont(nanumFont.deriveFont(Font.BOLD, 12)); 
        idLabel.setBounds(410,300,100,20);
        loginPanel.add(idLabel);

        idText.setBorder(BorderFactory.createEmptyBorder());
        idText.setBounds(465, 290,220, 40);
        loginPanel.add(idText);
        idText.setOpaque(true);

        pwLabel.setBounds(400,350,100,20);
        loginPanel.add(pwLabel);

        pwText.setBorder(BorderFactory.createEmptyBorder());
        pwText.setBounds(465, 340, 220, 40);
        loginPanel.add(pwText);
        pwText.setOpaque(true);

        loginBtn.setBackground(new Color(5,30,150));
        loginBtn.setBounds(700, 290, 90, 90);
        loginBtn.setForeground(Color.white);
        loginPanel.add(loginBtn);

        registBtn.setBackground(new Color(5,30,150));
        registBtn.setBounds(580, 390, 100, 30);
        registBtn.setOpaque(false);
        registBtn.setBorderPainted(false);
        registBtn.setForeground(Color.BLACK);
        loginPanel.add(registBtn);

        pwReBtn.setBackground(new Color(5,30,150));
        pwReBtn.setBounds(670, 390, 140, 30);
        pwReBtn.setOpaque(false);
        pwReBtn.setBorderPainted(false);
        pwReBtn.setForeground(Color.BLACK);
        loginPanel.add(pwReBtn);


        background.setIcon(new ImageIcon("login.jpg"));
        background.setBounds(0, 0, 1200, 800);
        loginPanel.add(background);
    }

    private void cursor() {
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        pwReBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pwReBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pwReBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        registBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private boolean checkUserCredentials(String id, String password) {
        return UserDataStorage.checkUserCredentials(id, password);
    }

    private void openBookInformationFrame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bookInformationFrame = new BookInformation(loggedInUserID);
                bookInformationFrame.setVisible(true);
                setVisible(false);
                dispose();
            }
        });
    }

    private void openFindIdFrame() {
        FindIdFrame findIdFrame = new FindIdFrame();
        findIdFrame.setVisible(true);
    }

    private void openResetPasswordFrame() {
        ResetPasswordFrame resetPasswordFrame = new ResetPasswordFrame();
        resetPasswordFrame.setVisible(true);
    }

    private void openRegistrationFrame() {
       UserRegistrationFrame registrationFrame = new UserRegistrationFrame();
        registrationFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }
        });
    }
}