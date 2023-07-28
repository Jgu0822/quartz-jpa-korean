import java.io.*;
import java.sql.*;

public class BlobTest {

    public static void main(String[] args) throws IOException, SQLException {
        // 이미지를 BLOB 형식으로 데이터베이스에 저장
        // storePicBlob();

        // 데이터베이스에서 BLOB 형식의 이미지 데이터 읽어오기
        getPicBlob();
    }

    public static void storePicBlob() throws FileNotFoundException, SQLException, IOException {
        String m_dbDriver = "com.mysql.jdbc.Driver";
        String m_dbUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        Connection conn = DriverManager.getConnection(m_dbUrl, "root", "root");

        File f = new File("C:\\Users\\admin\\Desktop\\timg.jpg");
        FileInputStream fis = new FileInputStream(f);

        String sql = "insert into image_save(image_name,image_in) values(?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, "임시 이미지");
        ps.setBinaryStream(2, fis, (int) f.length());

        ps.executeUpdate();

        conn.close();
        ps.close();
    }

    public static void getPicBlob() throws FileNotFoundException, SQLException, IOException {
        String m_dbDriver = "com.mysql.jdbc.Driver";
        String m_dbUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        Connection conn = DriverManager.getConnection(m_dbUrl, "root", "root");

        String sql = "select image_in from image_save where image_name='临时图片'";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Blob imageIn = null;
        while (rs.next()) {
            imageIn = rs.getBlob("image_in");
        }
        if (imageIn != null) {
            InputStream in = imageIn.getBinaryStream();

            OutputStream out = new FileOutputStream("C:\\Users\\admin\\Desktop\\cat.jpg");
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } else {
            System.out.println("이미지를 찾을 수 없습니다.");
        }

        conn.close();
        ps.close();
    }
}
