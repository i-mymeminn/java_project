package 로그인화면소스코드분할;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;

class CustomComboBoxEditor extends BasicComboBoxEditor {

    @Override
    public Component getEditorComponent() {
        JTextField editorComponent = (JTextField) super.getEditorComponent();

        // 커스텀 디자인을 적용합니다.
        editorComponent.setForeground(Color.WHITE);
        editorComponent.setBackground(Color.WHITE);  // 배경 색상을 흰색으로 설정
        editorComponent.setFont(new Font("Arial", Font.PLAIN, 12));

        return editorComponent;
    }
}



class CustomComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = 1L;


    private Border hoverBorder = BorderFactory.createLineBorder(Color.WHITE);
    private Border defaultBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);

    public CustomComboBoxRenderer() {
        super();
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Color navyColor = new Color(51, 51, 102);
        // 커스텀 디자인을 설정합니다.
        setForeground(Color.WHITE);
        setBackground(navyColor);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // 내부 여백 설정

        if (isSelected) {
            setBorder(hoverBorder);
        } else if (cellHasFocus) {
            setBorder(hoverBorder);
        } else {
            setBorder(defaultBorder);
        }

        return this;
    }
}

class CustomTableHeaderRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.BLACK));
        return label;
    }
}

class Customrenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = 1L;

    private Color backgroundColor;
    private Color foregroundColor;
    private Font boldFont;

    public Customrenderer(Color backgroundColor, Color foregroundColor) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        boldFont = super.getFont().deriveFont(Font.BOLD);
    }

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        renderer.setBackground(backgroundColor);
        renderer.setForeground(foregroundColor);
        
        if (isSelected) {
            renderer.setFont(boldFont);
        } else {
            renderer.setFont(super.getFont());
        }
        
        return renderer;
    }
}
