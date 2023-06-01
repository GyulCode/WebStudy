package com.sist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NatureDAO {
	
	// 연결 객체 
			private Connection conn;
			// 송수신 
			private PreparedStatement ps;
			// 오라클 URL주소 설정 
			private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
			// 싱글턴 
			private static NatureDAO dao;
			// 1. 드라이버 등록 => 한번 수행 => 시작과 동시에 한번 수행 , 멤버변수 초기화 : 생성자 
			public NatureDAO()
			{
				try
				{
					Class.forName("oracle.jdbc.driver.OracleDriver");
					// ClassNotFoundException => 체크예외처리 => 반드시 예외처리 한다 
					// java.io,java.net,java.sql  => 체크예외처리
				}catch(Exception ex) {}
			}
			// 2. 오라클 연결 
			public void getConnection()
			{
				try
				{
					conn=DriverManager.getConnection(URL,"hr","happy");
					// conn hr/happy => 명령어를 오라클 전송 
				}catch(Exception ex) {}
			}
			// 3. 오리클 해제
			public void disConnection()
			{
				try
				{
					if(ps!=null) ps.close(); // 통신이 열려있으면 닫는다
					if(conn!=null) conn.close();
					// exit => 오라클 닫기 
				}catch(Exception ex) {}
			}
			// 4. 싱글턴 설정 => static은 메모리 공간이 1개만 가지고 있다 
			// 메모리누수 현상을 방지 ...
			// DAO => new를 이용해서 생성 => 사용하지 않는 DAO가 오라클을 연결하고 있다 
			// 싱글턴은 데이터베이스에서는 필수 조건 
			// 프로그래머 , 디벨로퍼 (coder) 
			
			public static NatureDAO newInstance()
			{
				if(dao==null)
					dao=new NatureDAO();
				return dao;
			}
			
			// 5.기능
			// 목록 -> SQL(인라인뷰 -> 페이징)
			public List<NatureVO> natureListData(int page){
				List<NatureVO> list = new ArrayList<NatureVO>();
				
				try {
					getConnection();
					String sql="SELECT no,title,poster,num "
							+"FROM (SELECT no,title,poster,rownum AS num "
							+"FROM (SELECT no,title,poster "
							+"FROM seoul_nature ORDER BY no ASC)) "
							+"WHERE num BETWEEN ? AND ?";
					// rownum -> 가상컬럼(오라클에서 지원)
					// 단점 -> 중간에 데이터를 추출 할 수 없다.
					// SQL문장 전송
					ps=conn.prepareStatement(sql); // ?에 값이 없을 경우 IN, OUT에러 
					// ?값을 채운다
					int rowSize=12;
					int start=(rowSize*page)-(rowSize-1);
					int end=rowSize*page;
					ps.setInt(1, start);
					ps.setInt(2, end);
					// 실행요청
					ResultSet rs=ps.executeQuery();
					while(rs.next()) {
						NatureVO vo=new NatureVO();
						vo.setNo(rs.getInt(1));
						vo.setTitle(rs.getString(2));
						vo.setPoster(rs.getString(3));
						
						list.add(vo);
					}
					rs.close();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					disConnection();
				}
				return list;
			}

}
