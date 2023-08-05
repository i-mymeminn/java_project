package 로그인화면소스코드분할;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.Dimension;
import java.sql.*;

public class PurchaseRecordFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://database-1.cgw8vg4c5xae.us-east-2.rds.amazonaws.com:3306/sys";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "song0303";
    
    private JTable table;

    public PurchaseRecordFrame(String userID) {
        setTitle("Purchase Record");
        setSize(900, 335);
        setLocationRelativeTo(null); // 프레임을 화면의 정중앙에 위치
        setResizable(false);

        // 데이터베이스에서 데이터 불러오기
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("주문 일자");
        model.addColumn("결제금액");
        model.addColumn("결제수단");
        model.addColumn("배송지");
        

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();

            // 데이터베이스에서 userID로 검색하여 결과셋 가져오기
            String query = "SELECT price, date_string, method, address FROM purchase WHERE studentid = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);

            // 결과셋에서 데이터 읽어와서 모델에 추가
            while (rs.next()) {
                int price = rs.getInt("price");
                String date = rs.getString("date_string");
                String method = rs.getString("method");
                String address = rs.getString("address");

                model.addRow(new Object[]{date, price, method, address}); // 결제금액과 주문일자의 위치 변경
            }


            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        table = new JTable(model) {
        	private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 열 너비 수정
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(90);
        columnModel.getColumn(3).setPreferredWidth(300);

        
        table.setRowHeight(30); // 모든 행의 높이를 30으로 설정
        
        // 열 내용 가운데 정렬을 위한 TableCellRenderer 설정
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // 첫 번째 열 가운데 정렬
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // 두 번째 열 가운데 정렬
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // 세 번째 열 가운데 정렬
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // 네 번째 열 가운데 정렬

        // 테이블 스크롤 패널 생성
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 885, 300);
        add(scrollPane);
        
        // 세로 스크롤바의 가로 너비 조절
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(15, verticalScrollBar.getPreferredSize().height));

        
        table.setRowHeight(30); // 행 높이 설정

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // 레이아웃 매니저를 사용하지 않음
        setVisible(true);
    }
}
