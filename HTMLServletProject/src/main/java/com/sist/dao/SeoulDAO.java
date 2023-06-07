package com.sist.dao;

/*
   request : 사용자 요청값을 받는 경우 -> getParameter;
   response : 전송(브라우저) -> html,cookie
              한파일에 1번만 수행이 가능하다.
              서버에서 화면을 변경할때 사용되는 방법임(sendRedirect)
   
   cookie : 브라우저에 저장 -> 최근 방문
   session : 서버에 정보 저장 -> 예약, 장바구니
   -----------JSP / Spring / Spring-boot

*/
import java.util.*;
import java.sql.*;
import com.sist.dbconn.*;
public class SeoulDAO {
	private String[] tables= {"seoul_location", "seoul_nature", "seoul_shop", "seoul_hotel"};
	
	private Connection conn;
	private PreparedStatement ps;
	private CreateDataBase db=new CreateDataBase();
	private static SeoulDAO dao;
	
	// 1. 기능
	public List<SeoulVO> seoulListData(int page, int type){
		List<SeoulVO> list = new ArrayList<SeoulVO>();
		try {
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			
		}
		return list;
	}
	
	// 2. 총페이지 구하기
	public int seoulTotalpage(int type) {
		int total=0;
		try {
			conn=db.getConnection();
			String sql="SELECT CEIL(COUNT(*)/12.0 "
					+ "FROM "+tables[type];
			/*
			 * setString 자동으로 ''가 붙음
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return total;
	}
	
	// 3. 상세보기
	public SeoulVO seoulDetailData(int no, int type) {
		SeoulVO vo=new SeoulVO();
		try {
			
		} catch (Exception ex) {
			
		} finally {
			
		}
		return vo;
	}
	
	
			
	
	
	
	
	
	
	
	
	
	
	
	
	
}
