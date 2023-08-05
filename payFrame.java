package 로그인화면소스코드분할;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.Color;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;


class CustomTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

    public CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        // 값 변경을 방지하고 아무 작업도 수행하지 않음
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 모든 셀을 편집 불가능으로 설정
        return false;
    }
}



public class payFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";
    int totalPrice = 0; 
    int finalp = 0;
    int afterPoint = 0;
    int giving = 0;
    String howtopurchase = "";
    
    


    public payFrame(String userID) {
    	
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
        
        /*상단 파란색 이미지 삽입*/
        ImageIcon topBackgroundIcon = new ImageIcon("topbackgroundsmal.png");
        JLabel topBackgroundLabel = new JLabel(topBackgroundIcon);
        topBackgroundLabel.setBounds(0, -70, topBackgroundIcon.getIconWidth(), topBackgroundIcon.getIconHeight());
        this.getContentPane().add(topBackgroundLabel);
        
        ImageIcon JBNU = new ImageIcon("purchasepagelogo.jpg");
        
        /*로고 이미지 삽입*/
        ImageIcon logoIcon = new ImageIcon("main_icon.png");
        this.setIconImage(logoIcon.getImage());

         JLabel JBNULabel = new JLabel(JBNU);
         JBNULabel.setBounds(900, 30, 210, 210);
         this.getContentPane().add(JBNULabel);
        
        setSize(1200, 800); // 프레임 크기 설정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 닫기 버튼 동작 설정
        setLayout(null); // 레이아웃 매니저를 null로 설정
        
        String[] columnNames = {"과목", "책 이름", "작가", "가격"};
        Object[][] data = {}; // 데이터는 비어있는 상태로 초기화
        

        JLabel deliverpriceValue = new JLabel("0원");
        deliverpriceValue.setBounds(1050, 280, 150, 30);
        deliverpriceValue.setFont(nanumFont.deriveFont(Font.BOLD, 16)); // Set the font for the label
        add(deliverpriceValue);

        JLabel usedpointValue = new JLabel("0원");
        usedpointValue.setBounds(1050, 330, 150, 30);
        usedpointValue.setFont(nanumFont.deriveFont(Font.BOLD, 16)); // Set the font for the label
        add(usedpointValue);

        

        
        
        JLabel pricebefore = new JLabel("상품 금액");
        pricebefore.setBounds(900, 230, 150, 30);
        pricebefore.setFont(nanumFont.deriveFont(Font.BOLD, 16)); // Set the font for the label
        add(pricebefore);
        
        JLabel deliverprice = new JLabel("배송비");
        deliverprice.setBounds(900, 280, 150, 30);
        deliverprice.setFont(nanumFont.deriveFont(Font.BOLD, 16)); // Set the font for the label
        add(deliverprice);
        
        JLabel usedpoint = new JLabel("포인트 사용");
        usedpoint.setBounds(900, 330, 150, 30);
        usedpoint.setFont(nanumFont.deriveFont(Font.BOLD, 16)); // Set the font for the label
        add(usedpoint);
        
        JLabel purchasemethod = new JLabel ("결제수단 : 미선택");
        purchasemethod.setBounds(900,480,230,30);
        purchasemethod.setFont(nanumFont.deriveFont(Font.BOLD, 20)); // Set the font for the label
        add(purchasemethod);
        
        JLabel finalprice  = new JLabel("최종결제금액");
        finalprice.setBounds(900, 530, 150, 30);
        finalprice.setFont(nanumFont.deriveFont(Font.BOLD, 20)); // Set the font for the label
        add(finalprice);
        
        JLabel givingpoint  = new JLabel("적립예정포인트");
        givingpoint.setBounds(900, 580, 150, 30);
        givingpoint.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
        add(givingpoint);

        
        

        CustomTableModel tableModel = new CustomTableModel(data, columnNames);
        JTable dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(50, 50, 800, 250);
        add(scrollPane);

        dataTable.setRowHeight(30); // 행 높이 설정

        // Fetch data from the database and populate the table
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String query = "SELECT subject, textbook_name, writer, price FROM basket WHERE studentid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String textbookName = resultSet.getString("textbook_name");
                String writer = resultSet.getString("writer");
                int price = resultSet.getInt("price");

                Object[] rowData = { subject, textbookName, writer, price };
                tableModel.addRow(rowData);
                
                totalPrice += price; // Add the price to the total
                finalp = totalPrice;
                giving = totalPrice / 10;
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JLabel givingpointValue  = new JLabel( giving + "P");
        givingpointValue.setBounds(1050, 580, 150, 30);
        givingpointValue.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
        add(givingpointValue);
        
        JLabel pricebeforeValue = new JLabel(totalPrice + "원");
        pricebeforeValue.setBounds(1050, 230, 150, 30);
        pricebeforeValue.setFont(nanumFont.deriveFont(Font.BOLD, 16)); // Set the font for the label
        add(pricebeforeValue);

        JLabel finalpriceValue  = new JLabel(totalPrice + "원");
        finalpriceValue.setBounds(1050, 530, 150, 30);
        finalpriceValue.setFont(nanumFont.deriveFont(Font.BOLD, 20)); // Set the font for the label
        add(finalpriceValue);


        getContentPane().setBackground(Color.WHITE);
        setVisible(true);

        JButton inputButton = new JButton("기본 배송지 불러오기");
        inputButton.setBounds(50, 330, 170, 30);
        inputButton.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
        add(inputButton);
        
        // 기본 배송지 입력 필드
        JTextField addressField = new JTextField();
        addressField.setBounds(50, 370, 450, 30);
        
        addressField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (addressField.getText().equals("(배송지 입력)")) {
                    addressField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (addressField.getText().isEmpty()) {
                    addressField.setText("(기본 배송지 입력)");
                }
            }
        });
        
        add(addressField);
        
        // 기본 배송지 불러오기 버튼 클릭 이벤트 처리
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String address = getAddress(userID);

                // 주소 값을 addressField에 설정
                if (address != null) {
                    addressField.setText(address);
                }
            }
        });
        
        // 상세 배송지 입력 필드
        JTextField detailressField = new JTextField();
        detailressField.setBounds(50, 410, 450, 30);
        detailressField.setText("(상세주소)");
        detailressField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (detailressField.getText().equals("(상세주소)")) {
                    detailressField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (detailressField.getText().isEmpty()) {
                    detailressField.setText("(상세주소)");
                }
            }
        });
        add(detailressField);
        
        JTextField userPointField = new JTextField();
        userPointField.setBounds(700, 330, 150, 30);
        userPointField.setEditable(false);
        add(userPointField);

        JTextField afterPointField = new JTextField();
        afterPointField.setBounds(700, 410, 150, 30);
        afterPointField.setEditable(false);
        add(afterPointField);


        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String query = "SELECT point FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userPoint = resultSet.getInt("point");
                
                userPointField.setText(Integer.toString(userPoint));
                afterPointField.setText(Integer.toString(userPoint));
                giving = finalp/10;
                afterPoint = userPoint;
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JComboBox<Integer> usingPointComboBox = new JComboBox<>();
                usingPointComboBox.setBounds(700, 370, 150, 30);
                usingPointComboBox.setRenderer(new Customrenderer(Color.WHITE, Color.BLACK));

                add(usingPointComboBox);

                // Populate the usingPointComboBox with values up to userPoint in increments of 1000
                for (int i = 0; i <= userPoint; i += 1000) {
                    usingPointComboBox.addItem(i);
                }

                usingPointComboBox.addActionListener(e -> {
                	 int usingPoint = (int) usingPointComboBox.getSelectedItem();
                     afterPoint = Math.max(userPoint - usingPoint, 0);
                     afterPointField.setText(Integer.toString(afterPoint));
                     
                     
                    String usedPointText = usingPoint + "원";
                    usedpointValue.setText(usedPointText);

                    int total = totalPrice - usingPoint; // 선택한 값을 기존 totalprice에서 뺍니다.
                    finalp = totalPrice - usingPoint;
                    giving = finalp/10;
                    
                    givingpointValue.setText(giving + "P");
                    String totalPriceText = total + "원";
                    finalpriceValue.setText(totalPriceText);
                });
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel userPointLabel = new JLabel("보유중인 JB포인트");
        userPointLabel.setBounds(580, 330, 150, 30);
        userPointLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
        add(userPointLabel);

        JLabel usingPointLabel = new JLabel("사용할 JB포인트");
        usingPointLabel.setBounds(580, 370, 150, 30);
        usingPointLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
        add(usingPointLabel);
        
        JLabel afterPointLabel = new JLabel("사용 후 JB포인트");
        afterPointLabel.setBounds(580, 410, 150, 30);
        afterPointLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
        add(afterPointLabel);
        
        int desiredWidth = 180;  // 사용자가 원하는 가로 크기
        int desiredHeight = 90; // 사용자가 원하는 세로 크기
        String imagePath = "";
        
        
        JButton kakaopay = new JButton("");
        kakaopay.setBounds(50, 480, 200, 100);
        imagePath = "kakaopay.jpg";


        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        kakaopay.setIcon(resizedIcon);
        add(kakaopay);
        kakaopay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "Kakao Pay";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });
        
        JButton naverpay = new JButton("");
        naverpay.setBounds(250, 480, 200, 100);
        imagePath = "npay.jpg";

        ImageIcon originalIconNaverPay = new ImageIcon(imagePath);
        Image originalImageNaverPay = originalIconNaverPay.getImage();
        Image resizedImageNaverPay = originalImageNaverPay.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIconNaverPay = new ImageIcon(resizedImageNaverPay);

        naverpay.setIcon(resizedIconNaverPay);
        add(naverpay);
        naverpay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "Naver Pay";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });

        JButton samsungpay = new JButton("");
        samsungpay.setBounds(450, 480, 200, 100);
        imagePath = "samsungpay.jpg";

        ImageIcon originalIconSamsungPay = new ImageIcon(imagePath);
        Image originalImageSamsungPay = originalIconSamsungPay.getImage();
        Image resizedImageSamsungPay = originalImageSamsungPay.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIconSamsungPay = new ImageIcon(resizedImageSamsungPay);

        samsungpay.setIcon(resizedIconSamsungPay);
        add(samsungpay);
        samsungpay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "Samsung Pay";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });

        JButton toss = new JButton("");
        toss.setBounds(50, 580, 200, 100);
        imagePath = "toss.jpg";

        ImageIcon originalIconToss = new ImageIcon(imagePath);
        Image originalImageToss = originalIconToss.getImage();
        Image resizedImageToss = originalImageToss.getScaledInstance(desiredWidth-30, desiredHeight-20, Image.SCALE_SMOOTH);
        ImageIcon resizedIconToss = new ImageIcon(resizedImageToss);

        toss.setIcon(resizedIconToss);
        add(toss);
        toss.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "Toss";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });

        JButton payco = new JButton("");
        payco.setBounds(650, 480, 200, 100);
        imagePath = "payco.jpg";

        ImageIcon originalIconPayco = new ImageIcon(imagePath);
        Image originalImagePayco = originalIconPayco.getImage();
        Image resizedImagePayco = originalImagePayco.getScaledInstance(150, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIconPayco = new ImageIcon(resizedImagePayco);

        payco.setIcon(resizedIconPayco);
        add(payco);
        payco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "Payco";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });


        JButton nobank = new JButton("무통장 입금");
        nobank.setBounds(250, 580, 200, 100);
        nobank.setFont(nanumFont.deriveFont(Font.BOLD, 24)); // Set the font for the label
        add(nobank);
        nobank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "무통장 입금";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });

        JButton phonepurchase = new JButton("휴대폰 결제");
        phonepurchase.setBounds(450, 580, 200, 100);
        phonepurchase.setFont(nanumFont.deriveFont(Font.BOLD, 24)); // Set the font for the label
        add(phonepurchase);
        phonepurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "휴대폰 결제";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });

        JButton creditcard = new JButton("신용카드결제");
        creditcard.setBounds(650, 580, 200, 100);
        creditcard.setFont(nanumFont.deriveFont(Font.BOLD, 24)); // Set the font for the label
        add(creditcard);
        creditcard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howtopurchase = "신용카드결제";
                purchasemethod.setText("결제수단 : " + howtopurchase);
            }
        });


        JButton accept = new JButton("결제하기");
        accept.setBounds(900, 630, 230, 50);
        accept.setFont(nanumFont.deriveFont(Font.BOLD, 20)); // Set the font for the label
        add(accept);
        
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((howtopurchase.isEmpty() || addressField.getText().isEmpty()) || addressField.getText().equals("(배송지 입력)")) {
                	JOptionPane.showMessageDialog(null, "결제 수단을 선택해주시고, 주소를 입력해주세요.", "결제 수단 및 주소", JOptionPane.WARNING_MESSAGE);

                } else {
                    int option = JOptionPane.showConfirmDialog(null, "결제를 완료하시겠습니까?", "결제 확인", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // 결제 완료 버튼을 눌렀을 때의 동작
                        try {
                            // 1. users 테이블 업데이트
                            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                            String query = "UPDATE users SET point = ? WHERE id = ?";
                            PreparedStatement stmt = conn.prepareStatement(query);
                            stmt.setInt(1, giving + afterPoint);
                            stmt.setString(2, userID);
                            stmt.executeUpdate();
                            stmt.close();

                            // 2. basket 테이블 데이터 삭제
                            query = "DELETE FROM basket WHERE studentid = ?";
                            stmt = conn.prepareStatement(query);
                            stmt.setString(1, userID);
                            stmt.executeUpdate();
                            stmt.close();

                            // 3. 현재 시간 및 날짜 가져오기
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date date = new Date();
                            String formattedDate = dateFormat.format(date);

                            // 4. purchase 테이블에 데이터 추가
                            query = "INSERT INTO purchase (date_string, price, studentid, method, address) VALUES (?, ?, ?, ?, ?)";
                            stmt = conn.prepareStatement(query);
                            stmt.setString(1, formattedDate);
                            stmt.setDouble(2, finalp);
                            stmt.setString(3, userID);
                            stmt.setString(4, howtopurchase); // method 열에 howtopurchase 변수 값 설정

                            // addressField에서 값을 가져와서 address 열에 저장
                            String addressValue = addressField.getText();

                            // detailAddressField에서 값을 가져와서 address 열에 이어서 저장
                            String detailAddressValue = detailressField.getText();
                            addressValue += " " + detailAddressValue;

                            stmt.setString(5, addressValue);

                            stmt.executeUpdate();
                            stmt.close();
                            
                            JOptionPane.showMessageDialog(null, "주문이 완료되었습니다.", "주문 완료", JOptionPane.INFORMATION_MESSAGE);
                            
                            dispose();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else if (option == JOptionPane.NO_OPTION) {
                        JOptionPane.getRootFrame().dispose();
                    }
                }
            }
        });

        

        this.setLocationRelativeTo(null); // 사용자 화면의 정 가운데로 위치
        getContentPane().setBackground(Color.WHITE);
        setVisible(true); // 프레임을 표시
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
