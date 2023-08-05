package 로그인화면소스코드분할;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JDialog;


public class CartTable extends JFrame {
	private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";
    
    

    private JTable table;
    private DefaultTableModel tableModel;
    private String loggedInUserID;
    private ImageIcon payBackgroundIcon;
    private ImageIcon payClickedBackgroundIcon;

    private JLabel bookcount;
    private JLabel bookpricetotal;

    public CartTable(String userID) {
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
        
        loggedInUserID = userID;
        setTitle("장바구니");
        setBounds(100, 100, 1200, 800);
        setResizable(false); // 창 크기 조정 비활성화

        tableModel = new DefaultTableModel(new Object[]{"학번", "학과", "학년", "과목", "교수", "교재", "작가","가격"}, 0);
        table = new JTable(tableModel) {
        	private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // 숨길 열의 인덱스를 가져옵니다. 여기서는 학번 열이 첫 번째 열이므로 index 0입니다.
        int columnIndexToHide = 0;

        // 해당 열의 너비를 0으로 설정하여 숨깁니다.
        TableColumn columnToHide = table.getColumnModel().getColumn(columnIndexToHide);
        columnToHide.setMinWidth(0);
        columnToHide.setMaxWidth(0);
        columnToHide.setWidth(0);
        columnToHide.setPreferredWidth(0);
        columnToHide.setResizable(false);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int studentId = (int) table.getValueAt(row, 0);
                    openDialog(studentId);
                }
            }
        });
        
        table.setRowHeight(30); // 행 높이 설정
        
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(180);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(40);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        /*기본 색상 흰색으로 바꾸는 부분*/
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.selectionBackground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 전체 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
   
        ImageIcon topBackgroundIcon = new ImageIcon("topbackgroundsmal.png");
        JLabel topBackgroundLabel = new JLabel(topBackgroundIcon);
        topBackgroundLabel.setBounds(0, 0, topBackgroundIcon.getIconWidth(), topBackgroundIcon.getIconHeight());
        mainPanel.add(topBackgroundLabel);
        
        JLabel info = new JLabel("*더블클릭하여 장바구니에서 제거 가능합니다");
        info.setFont(nanumFont.deriveFont(Font.BOLD, 14));
        info.setBounds(50,660,300,50);
        mainPanel.add(info);
        

        // 스크롤 패널 생성 및 테이블 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 150, 900, 500);
        mainPanel.add(scrollPane);
        
        // 가격 패널 생성
        JPanel pricePanel = new JPanel() {
        	private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 이미지를 패널의 배경으로 그림
                ImageIcon priceImg = new ImageIcon("pricetable.jpg");
                Image image = priceImg.getImage();
                g.drawImage(image, 0, 0, null);
            }
        };
        pricePanel.setBounds(975, 150, 200, 230);
        pricePanel.setLayout(null);
        pricePanel.setOpaque(false); // 패널을 투명하게 설정;
        
        // 결제 가격 및 수량 추가
        JLabel quantityLabel = new JLabel("총 수량");
        quantityLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 13));
        JLabel totalPriceLabel = new JLabel("결제 예상 금액");
        totalPriceLabel.setFont(nanumFont.deriveFont(Font.PLAIN, 13));
        
        quantityLabel.setBounds(30, 20, 120, 60);
        totalPriceLabel.setBounds(30, 100, 120, 60);
        pricePanel.add(quantityLabel);
        pricePanel.add(totalPriceLabel);
        
        loadCartItems();
        
        
        //총 가격, 총 수량 입력 할 곳 추가
        bookcount = new JLabel("");
        bookcount.setFont(nanumFont.deriveFont(Font.BOLD, 22));
        bookpricetotal = new JLabel("");
        bookpricetotal.setFont(nanumFont.deriveFont(Font.BOLD, 22));
        bookcount.setBounds(30, 65, 150, 30);
        bookpricetotal.setBounds(30, 145, 150, 30);
        pricePanel.add(bookcount);
        pricePanel.add(bookpricetotal);
        
        
        // "결제하기" 버튼 생성
        payBackgroundIcon = new ImageIcon("payButton.jpg");
        payClickedBackgroundIcon = new ImageIcon("presspayButton.jpg");
        JButton paymentButton = new JButton(payBackgroundIcon);
        paymentButton.setBounds(960, 375, 200, 110);
        paymentButton.setBorderPainted(false);
        paymentButton.setContentAreaFilled(false);
        paymentButton.setFocusPainted(false);

        paymentButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                paymentButton.setIcon(payClickedBackgroundIcon);
            }

            public void mouseExited(MouseEvent e) {
                paymentButton.setIcon(payBackgroundIcon);
            }
        });

        paymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "장바구니가 비어있습니다.", "장바구니", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    paymentButton.setIcon(payClickedBackgroundIcon);
                    payFrame frame = new payFrame(userID); // payFrame 인스턴스 생성
                    frame.setVisible(true); // 프레임 표시
                    
                    // Close the current frame
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(paymentButton);
                    currentFrame.dispose();
                }
            }
        });


        
        updateTotalValues();
        
        mainPanel.add(paymentButton);

        this.setLocationRelativeTo(null); // 사용자 화면의 정 가운데로 위치
        mainPanel.add(pricePanel);
        setContentPane(mainPanel);

    }

    // 장바구니 아이템 로드
    private void loadCartItems() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String selectQuery = "SELECT * FROM basket WHERE studentid = ?";
            PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
            selectStatement.setInt(1, Integer.parseInt(loggedInUserID));

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                int studentId = resultSet.getInt("studentid");
                String department = resultSet.getString("department");
                int grade = resultSet.getInt("grade");
                String subject = resultSet.getString("subject");
                String professor = resultSet.getString("professor");
                String textbookName = resultSet.getString("textbook_name");
                String writer = resultSet.getString("writer");
                int price = resultSet.getInt("price");
                tableModel.addRow(new Object[]{studentId, department, grade, subject, professor, textbookName, writer, price});
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "장바구니를 불러오는 중에 오류가 발생했습니다: " + e.getMessage(), "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    


    // 교재 삭제
    private void deleteTextbook(String textbookName) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String deleteQuery = "DELETE FROM basket WHERE textbook_name = ?";
            PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
            deleteStatement.setString(1, textbookName);
            deleteStatement.executeUpdate();
            
            // 업데이트 메서드 호출
            SwingUtilities.invokeLater(this::updateTotalValues);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "상품을 제거하는 중에 오류가 발생했습니다: " + e.getMessage(), "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // 대화상자 열기
    private void openDialog(int studentId) {
        JFrame dialogFrame = new JFrame();
        dialogFrame.setTitle("삭제 메뉴");
        
        dialogFrame.setLocationRelativeTo(null);
        dialogFrame.setSize(300,100);

 
        JButton deleteButton = new JButton("장바구니에서 제거하기");
        JButton cancelButton = new JButton("취소");


        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String textbookName = (String) table.getValueAt(row, 5);
                showConfirmationDialog(textbookName);
                dialogFrame.dispose();
                
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogFrame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(deleteButton);
        panel.add(cancelButton);

        
        
        dialogFrame.getContentPane().add(panel);
        dialogFrame.setVisible(true);
    }
    
    

    // 삭제 확인 대화상자 표시
    private void showConfirmationDialog(String textbookName) {
        JOptionPane optionPane = new JOptionPane("정말로 해당 상품을 장바구니에서 삭제하시겠습니까?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(this, "삭제 확인");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();
        if (selectedValue != null && selectedValue.equals(JOptionPane.YES_OPTION)) {
            deleteTextbook(textbookName);

            int row = table.getSelectedRow();
            if (row != -1) {
                tableModel.removeRow(row);
            }
            JOptionPane.showMessageDialog(this, "정상적으로 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
 // 업데이트 총 가격과 총 수량
    private void updateTotalValues() {
        bookcount.setText(tableModel.getRowCount() + "권");
        int totalPrice = 0;
        int priceColumnIndex = table.getColumnModel().getColumnIndex("가격");

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            int price = (int) tableModel.getValueAt(row, priceColumnIndex);
            totalPrice += price;
        }

        bookpricetotal.setText(totalPrice + "원");
    }
}