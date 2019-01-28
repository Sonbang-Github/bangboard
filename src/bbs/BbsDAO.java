package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

public class BbsDAO {

	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {
		try {
			// "jdbc:mysql://localhost:3306/jsptest",
			//?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory
//			String dbURL = "jdbc:postgres://ec2-54-225-227-125.compute-1.amazonaws.com:5432/d85eti39ptt1g6";
//			String dbID = "vivpchcphtjnfz";
//			String dbPassword = "8c0d6b20b2fc03b609938e6f733f12e00bea431a0a1b69e39790a2923feb8432";
//			Class.forName("org.postgresql.Driver");
//			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
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
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; //DB 오류
	}
	
	public int getNext() {
		String SQL = "SELECT bbsid FROM bbs ORDER BY bbsid DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; //첫 번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //DB 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO bbs VALUES (?, ?, ?, now(), ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
//			pstmt.setString(4, getDate());
			pstmt.setString(4, bbsContent);
			pstmt.setInt(5, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //DB 오류
	}
	
	public ArrayList<Bbs> getlist(int pageNumber) {
		String SQL = "SELECT * FROM bbs WHERE bbsid < ? AND bbsavailable = 1 ORDER BY bbsid DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM bbs WHERE bbsid < ? AND bbsavailable = 1 ORDER BY bbsid DESC LIMIT 10";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
		public Bbs getBbs(int bbsID) {
			String SQL = "SELECT * FROM bbs WHERE bbsid = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, bbsID);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					return bbs;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE bbs SET bbstitle = ?, bbscontent = ? WHERE bbsid = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //DB 오류
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE bbs SET bbsavailable = 0 WHERE bbsid = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //DB 오류
	}
	
}
