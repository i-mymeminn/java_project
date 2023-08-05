package 로그인화면소스코드분할;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BookSearchFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private JTextArea bookInfoTextArea;
    private JLabel bookCoverLabel;

    public BookSearchFrame(String bookTitle, String author) {

        setTitle("도서 검색");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(600, 400, 700, 250);
        getContentPane().setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        bookCoverLabel = new JLabel();
        bookCoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookCoverLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bookInfoTextArea = new JTextArea();
        bookInfoTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookInfoTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bookCoverLabel, scrollPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(150);

        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        searchBooks(bookTitle, author);
    }

    public void searchBooks(String bookTitle, String author) {
        try {
            String encodedBookTitle = URLEncoder.encode(bookTitle, "UTF-8");

            // 알라딘 API를 이용하여 도서 검색
            String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbsong46411323001"
                    + "&Query=" + encodedBookTitle + "&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=xml&Version=20070901";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // API 응답을 XML 형식으로 받아와서 처리
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(connection.getInputStream());

                // XML에서 도서 정보 추출
                NodeList itemList = document.getElementsByTagName("item");
                List<JSONObject> bookList = new ArrayList<>();

                for (int i = 0; i < itemList.getLength(); i++) {
                    Node itemNode = itemList.item(i);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element itemElement = (Element) itemNode;
                        String itemBookTitle = itemElement.getElementsByTagName("title").item(0).getTextContent();
                        String itemAuthor = itemElement.getElementsByTagName("author").item(0).getTextContent();
                        String itemPublisher = itemElement.getElementsByTagName("publisher").item(0).getTextContent();
                        String bookPrice = itemElement.getElementsByTagName("priceSales").item(0).getTextContent();
                        String bookCoverUrl = itemElement.getElementsByTagName("cover").item(0).getTextContent();

                        if (itemAuthor.contains(author)) {
                            // 검색 결과 중 저자명에 입력한 저자가 포함된 경우에만 도서 정보 저장
                            JSONObject bookObject = new JSONObject();
                            bookObject.put("책 이름", itemBookTitle);
                            bookObject.put("저자 이름", itemAuthor);
                            bookObject.put("출판사", itemPublisher);
                            bookObject.put("가격", bookPrice);
                            bookObject.put("표지 이미지 URL", bookCoverUrl);

                            bookList.add(bookObject);
                        }
                    }
                }

                // 도서 정보를 화면에 표시
                displayBookInfo(bookList);
            } else {
                System.out.println("HTTP 요청 실패: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayBookInfo(List<JSONObject> bookList) {
    	
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
        
        if (bookList.isEmpty()) {
            // 검색 결과가 없을 경우 메시지 표시
            bookInfoTextArea.setText("검색 결과가 없습니다.");
            bookCoverLabel.setIcon(null);
        } else {
            // 검색 결과가 있을 경우 첫 번째 도서 정보 표시
            JSONObject bookObject = bookList.get(0);

            StringBuilder sb = new StringBuilder();
            sb.append("책 이름 - ").append(bookObject.getString("책 이름")).append("\n\n");
            sb.append("저자 - ").append(bookObject.getString("저자 이름")).append("\n\n");
            sb.append("출판사 - ").append(bookObject.getString("출판사")).append("\n\n");
            sb.append("가격 - ").append(bookObject.getString("가격")).append("원");
            bookInfoTextArea.setText(sb.toString());

            bookInfoTextArea.setFont(nanumFont.deriveFont(Font.BOLD, 18));

            String bookCoverUrl = bookObject.getString("표지 이미지 URL");
            try {
                // 도서 표지 이미지 로드 및 크기 조정
                URL imageUrl = new URL(bookCoverUrl);
                BufferedImage originalImage = ImageIO.read(imageUrl);

                int resizedWidth = 150; // 조정할 가로 크기
                int resizedHeight = 200; // 조정할 세로 크기

                BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = resizedImage.createGraphics();

                // 렌더링 품질 설정
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics.drawImage(originalImage, 0, 0, resizedWidth, resizedHeight, null);
                graphics.dispose();

                ImageIcon icon = new ImageIcon(resizedImage);
                bookCoverLabel.setIcon(icon);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
