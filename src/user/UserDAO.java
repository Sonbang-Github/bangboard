package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class UserDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			// "jdbc:mysql://localhost:3306/jsptest",
//			String dbURL = "jdbc:postgres://ec2-54-225-227-125.compute-1.amazonaws.com:5432/d85eti39ptt1g6";
//			String dbID = "vivpchcphtjnfz";
//			String dbPassword = "8c0d6b20b2fc03b609938e6f733f12e00bea431a0a1b69e39790a2923feb8432";
//			Class.forName("org.postgresql.Driver");
			// org.postgresql.Driver
			
			Class.forName("org.postgresql.Driver");
			Properties props = new Properties();
			props.setProperty("user", "vivpchcphtjnfz");
			props.setProperty("password", "8c0d6b20b2fc03b609938e6f733f12e00bea431a0a1b69e39790a2923feb8432");
			props.setProperty("ssl", "true");
			props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
			
//			String url = "jdbc:postgresql://ec2-54-225-227-125.compute-1.amazonaws.com:5432/d85eti39ptt1g6";
//			conn = DriverManager.getConnection(url, props);
			String url = "jdbc:postgresql://ec2-54-225-227-125.compute-1.amazonaws.com:5432/d85eti39ptt1g6?user=vivpchcphtjnfz&password=8c0d6b20b2fc03b609938e6f733f12e00bea431a0a1b69e39790a2923feb8432&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userpassword FROM public.bbsuser WHERE userid = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; //로그인 성공
				}
				else {
					return 0; //비밀번호 불일치
				}
				} else {
				return -1; // 아이디가 없음
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; //데이터베이스 오류
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO public.bbsuser VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
}
