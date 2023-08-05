package 로그인화면소스코드분할;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class BookInformation extends JFrame {
	private static final long serialVersionUID = 1L;

private CartTable cartTableFrame;
private JComboBox<String> collegeComboBox;
private JComboBox<String> departmentComboBox;
private JComboBox<Integer> gradeComboBox;
private JTable textbookTable;
private String loggedInUserID;
private DefaultTableModel tableModel;
private DefaultTableModel cartTableModel; // 추가된 장바구니 테이블 모델



public BookInformation(String userID) {
	
	/*기본 색상 흰색으로 바꾸는 부분*/
	try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", Color.WHITE);
    } catch (Exception e) {
        e.printStackTrace();
    }

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
    /**/
    
    /*로고 이미지 삽입*/
    ImageIcon logoIcon = new ImageIcon("main_icon.png");
    this.setIconImage(logoIcon.getImage());

    /*상단 파란색 이미지 삽입*/
    ImageIcon topBackgroundIcon = new ImageIcon("topbackgroundsmal.png");
    JLabel topBackgroundLabel = new JLabel(topBackgroundIcon);
    topBackgroundLabel.setBounds(0, 0, topBackgroundIcon.getIconWidth(), topBackgroundIcon.getIconHeight());
    this.getContentPane().add(topBackgroundLabel);
    
    /*전북대학교 로고 삽입*/
    ImageIcon dandaeLogoIcon = new ImageIcon("JBNU_main2.png");
    Image dandaeLogoImage = dandaeLogoIcon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
    ImageIcon scaledDandaeLogoIcon = new ImageIcon(dandaeLogoImage);
    JLabel dandaeLogoLabel = new JLabel(scaledDandaeLogoIcon);
    dandaeLogoLabel.setBounds(0, 110, 400, 100);
    this.getContentPane().add(dandaeLogoLabel);
    
    JLabel infoLabel = new JLabel("*책 정보 열람 또는 장바구니에 담기는 더블클릭 해주세요");
    infoLabel.setBounds(100,700, 400, 30);
    infoLabel.setFont(nanumFont.deriveFont(Font.BOLD, 12)); // Set the font for the label
    this.add(infoLabel);
    
    /*로그인된 유저 아이디 + 프레임 타이틀 설정*/
    loggedInUserID = userID;
    setTitle("도서 목록");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);
    setResizable(false);
    setLayout(null); // 레이아웃 매니저 제거

    // 단과대학 선택 콤보박스
    collegeComboBox = new JComboBox<>();
    collegeComboBox.addItem("단과대학");
    collegeComboBox.setBounds(470, 145, 100, 30);
    collegeComboBox.setEditor(new CustomComboBoxEditor());
    collegeComboBox.setRenderer(new CustomComboBoxRenderer());


    // 학과 선택 콤보박스
    departmentComboBox = new JComboBox<>();
    departmentComboBox.setEnabled(false); // 초기에 비활성화 상태
    departmentComboBox.setBounds(590, 145, 150, 30);
    departmentComboBox.setEditor(new CustomComboBoxEditor());
    departmentComboBox.setRenderer(new CustomComboBoxRenderer());


    // 학년 선택 콤보박스
    gradeComboBox = new JComboBox<>();
    for (int i = 1; i <= 4; i++) {
        gradeComboBox.addItem(i);
    }
    gradeComboBox.setBounds(760, 145, 50, 30);
    gradeComboBox.setEditor(new CustomComboBoxEditor());
    gradeComboBox.setRenderer(new CustomComboBoxRenderer());

    // 검색 버튼
    JButton searchButton = new JButton("검색");
    searchButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedDepartment = (String) departmentComboBox.getSelectedItem();
            int selectedGrade = ((Integer) gradeComboBox.getSelectedItem()).intValue();
            fetchTextbookData(selectedDepartment, selectedGrade);
        }
    });
    searchButton.setBounds(820, 145, 60, 30);
    
    // 메뉴 버튼 생성
    JButton menuButton = new JButton("메뉴");
    menuButton.setBounds(995, 145, 105, 30);
    add(menuButton);

    // 팝업 메뉴 생성
    JPopupMenu popupMenu = new JPopupMenu();
    
    // 메뉴 버튼 폰트 설정
    menuButton.setFont(nanumFont.deriveFont(Font.PLAIN, 12));

    // 팝업 메뉴 폰트 및 가운데 정렬 설정
    Component[] menuComponents = popupMenu.getComponents();
    for (Component component : menuComponents) {
        if (component instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) component;
            menuItem.setFont(nanumFont.deriveFont(Font.PLAIN, 12));
            menuItem.setHorizontalAlignment(SwingConstants.CENTER); // 아이템 가운데 정렬
        }
    }

    // 메뉴 아이템 생성
    JMenuItem menuItem1 = new JMenuItem("내 정보");
    JMenuItem menuItem2 = new JMenuItem("포인트");
    JMenuItem menuItem3 = new JMenuItem("장바구니");
    JMenuItem menuItem4 = new JMenuItem("결제내역");

    /*내 정보 메뉴*/
    menuItem1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            correctFrame userInfo = new correctFrame(userID);
            userInfo.displayUserInfo();
        }
    });

    /*포인트 메뉴*/
    menuItem2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	CouponFrame coupon = new CouponFrame(userID);
            coupon.setVisible(true);
        }
    });
    
    /*장바구니 메뉴*/
    menuItem3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            fetchCartData();
        }
    });
    
    /*결제내역 메뉴*/
    menuItem4.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            PurchaseRecordFrame frame = new PurchaseRecordFrame(userID);
            frame.setVisible(true);
        }
    });


    // 팝업 메뉴에 메뉴 아이템 추가
    popupMenu.add(menuItem1);
    popupMenu.add(menuItem2);
    popupMenu.add(menuItem3);
    popupMenu.add(menuItem4);

    // 메뉴 버튼에 ActionListener 추가
    menuButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // 메뉴 버튼 클릭 시 팝업 메뉴 표시
        	popupMenu.show(menuButton, 0, menuButton.getHeight());
        }
    });

    
    // 콤보박스 폰트 설정
    Font comboBoxFont = new Font(nanumFont.getName(), Font.PLAIN, 12);
    
    collegeComboBox.setFont(comboBoxFont);
    departmentComboBox.setFont(comboBoxFont);
    gradeComboBox.setFont(comboBoxFont);
    Font searchButtonFont = new Font(nanumFont.getName(), Font.PLAIN, 12);
    searchButton.setFont(searchButtonFont);
    
    add(collegeComboBox);
    add(departmentComboBox);
    add(gradeComboBox);
    add(searchButton);

    textbookTable = new JTable() {
    	private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // 테이블에서 셀 편집 비활성화
        }
    };


    textbookTable.setRowHeight(30); // 행 높이 설정

    textbookTable.addMouseListener(new java.awt.event.MouseAdapter() {
        // 이하 코드 생략
    });


    textbookTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) { // 더블 클릭 이벤트 처리
                int selectedRow = textbookTable.getSelectedRow();
                if (selectedRow != -1) {
                    String subjectName = (String) tableModel.getValueAt(selectedRow, 0);
                    String professorName = (String) tableModel.getValueAt(selectedRow, 1);
                    String textbookName = (String) tableModel.getValueAt(selectedRow, 2);
                    String writerName = (String) tableModel.getValueAt(selectedRow, 3);
                    String price = (String) tableModel.getValueAt(selectedRow, 4);
                    
                    // 선택된 학과와 학년 가져오기
                    String selectedDepartment = (String) departmentComboBox.getSelectedItem();
                    int selectedGrade = (int) gradeComboBox.getSelectedItem();

                    Object[] options = {"책 정보 열람", "장바구니에 담기", "취소"};
                    int confirmResult = JOptionPane.showOptionDialog(BookInformation.this, "원하는 작업을 선택하세요:", "작업 선택",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (confirmResult == 0) { // "책 정보 열람" 선택
                        if (textbookName.equals("(강의중 자료)")) {
                            JOptionPane.showMessageDialog(null, "해당 과목은 교재를 사용하지 않습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            BookSearchFrame bookSearchFrame = new BookSearchFrame(textbookName, writerName); // BookSearchFrame에 책 이름과 저자 이름 전달
                            bookSearchFrame.setVisible(true); // BookSearchFrame 표시
                        }
                    } else if (confirmResult == 1) {
                        // 교재 이름이 "(강의중 자료)"인 경우 메시지 출력
                        if (textbookName.equals("(강의중 자료)")) {
                            JOptionPane.showMessageDialog(BookInformation.this, "해당 과목은 교재를 사용하지 않습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            TextbookDataStorage.saveTextbook(Integer.parseInt(loggedInUserID), selectedDepartment,
                                    selectedGrade, subjectName, professorName, textbookName, writerName, price);
                        }
                    }
                }
            }
        }
    });

    JButton viewButton = new JButton("책 정보 열람하기");

    viewButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = textbookTable.getSelectedRow();
            if (selectedRow != -1) {
                String textbookName = (String) tableModel.getValueAt(selectedRow, 2);
                String writer = (String) tableModel.getValueAt(selectedRow, 3);
                BookSearchFrame bookSearchFrame = new BookSearchFrame(textbookName, writer); // BookSearchFrame에 책 이름과 저자 이름 전달
                bookSearchFrame.setVisible(true); // BookSearchFrame 표시
            }
        }
    });


    JScrollPane scrollPane = new JScrollPane(textbookTable);
    scrollPane.setBounds(100, 300, 1000, 400);
    add(scrollPane);

    // 테이블 모델 생성
    tableModel = new DefaultTableModel();
    tableModel.addColumn("과목명");
    tableModel.addColumn("교수명");
    tableModel.addColumn("교재명");
    tableModel.addColumn("작가");
    tableModel.addColumn("가격");
    
    // 테이블에 모델 설정
    textbookTable.setModel(tableModel);

    // 각 열의 너비 설정
    textbookTable.getColumnModel().getColumn(0).setPreferredWidth(200);
    textbookTable.getColumnModel().getColumn(1).setPreferredWidth(50);
    textbookTable.getColumnModel().getColumn(2).setPreferredWidth(250);
    textbookTable.getColumnModel().getColumn(3).setPreferredWidth(30);
    textbookTable.getColumnModel().getColumn(4).setPreferredWidth(50);
    
    cartTableModel = new DefaultTableModel();
    cartTableModel.addColumn("과목명");
    cartTableModel.addColumn("교수명");
    cartTableModel.addColumn("교재명");

    // 테이블에 모델 설정
    textbookTable.setModel(tableModel);

    // 데이터 로드
    fetchColleges(); // 단과대학 데이터 로드


    


    getContentPane().setBackground(Color.WHITE);
    textbookTable.setBackground(Color.WHITE);

    scrollPane.setBackground(Color.WHITE);
    textbookTable.setGridColor(Color.LIGHT_GRAY); // 또는 다른 원하는 색상으로 변경


    this.setLocationRelativeTo(null); // 사용자 화면의 정 가운데로 위치
    setVisible(true);
}

// 단과대학 데이터 로드
private void fetchColleges() {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys", "admin", "song0303")) {
        // SQL 문 생성
        String sql = "SELECT DISTINCT college FROM bookinfo";

        // 문 실행
        Statement statement = conn.createStatement();

        // 쿼리 실행 및 결과 가져오기
        ResultSet resultSet = statement.executeQuery(sql);

        // 결과 순회
        while (resultSet.next()) {
            String college = resultSet.getString("college");
            collegeComboBox.addItem(college);
        }

        // 리소스 해제
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // 단과대학 선택에 대한 아이템 리스너 등록
    collegeComboBox.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedCollege = (String) collegeComboBox.getSelectedItem();
                fetchDepartments(selectedCollege); // 선택된 단과대학에 대한 학과 로드
            }
        }
    });
}

// 학과 데이터 로드
private void fetchDepartments(String college) {
    // 학과 콤보 박스 초기화
    departmentComboBox.removeAllItems();

    // 데이터베이스 연결
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys", "admin", "song0303")) {
        // SQL 문 생성
        String sql = "SELECT DISTINCT department FROM bookinfo WHERE college = ?";

        // 준비된 문 생성
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, college); // 선택된 단과대학

        // 쿼리 실행 및 결과 가져오기
        ResultSet resultSet = statement.executeQuery();

        // 결과 순회
        while (resultSet.next()) {
            String department = resultSet.getString("department");
            departmentComboBox.addItem(department);
        }

        // 학과 콤보 박스 활성화
        departmentComboBox.setEnabled(true);

        // 리소스 해제
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//교재 데이터 로드
private void fetchTextbookData(String department, int grade) {
 // 테이블 모델 초기화
 tableModel.setRowCount(0);

 // 데이터베이스 연결
 try (Connection conn = DriverManager.getConnection("jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys", "admin", "song0303")) {
     // SQL 문 생성
     String sql = "SELECT subject, professor, textbook_name, writer, price FROM bookinfo WHERE department = ? AND grade = ?";

     // 준비된 문 생성
     PreparedStatement statement = conn.prepareStatement(sql);
     statement.setString(1, department); // 선택된 학과
     statement.setInt(2, grade); // 선택된 학년

     // 쿼리 실행 및 결과 가져오기
     ResultSet resultSet = statement.executeQuery();

     // 결과 순회
     while (resultSet.next()) {
         String subjectName = resultSet.getString("subject");
         String professorName = resultSet.getString("professor");
         String textbookName = resultSet.getString("textbook_name");
         String writerName = resultSet.getString("writer");
         String price = resultSet.getString("price");

         // 테이블 모델에 행 추가
         tableModel.addRow(new Object[]{subjectName, professorName, textbookName, writerName, price});
     }

     // 리소스 해제
     resultSet.close();
     statement.close();
 } catch (SQLException e) {
     e.printStackTrace();
 }
}


// 장바구니 데이터 로드
private void fetchCartData() {
    cartTableFrame = new CartTable(loggedInUserID);
    cartTableFrame.setVisible(true);
}
}
