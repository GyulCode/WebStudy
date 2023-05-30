package com.sist.dao;
// -> 카테고리 출력 -> 카테고리별 맛집 -> 맛집에 대한 상세보기 -> 지도출력, 검색
import java.util.*;
import java.util.Locale.Category;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;

import java.sql.*;
public class FoodDAO {
	
	// 연결객체
	private Connection conn;
	//송수신
	private PreparedStatement ps;
	//오라클 URL주소 설정
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	//싱글턴
	private static FoodDAO dao;
	// 1. 드라이버 등록 -> 한번 수행 -> 시작과 동시에 한번 수행, 멤버변수 초기화
	public FoodDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// ClassNotFoundException -> 체크예외처리 반드시 예외 처리한다;
			// java. io, net, sql은 무조건 예외처리 해줘야함
		} catch (Exception ex) {}
	}
	// 2. 오라클 연결 
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
			//conn hr/happy 명령어 오라클 전송
		} catch (Exception ex) {}
	}
	
	// 3. 오라클 해제
	public void disConnection() {
		try {
			if(ps!=null) ps.close(); //통신이 열려있으면 닫는다
			if(conn!=null) conn.close();
		} catch (Exception ex) {}
	}
	// 4. 싱글턴 설정 -> static은 메모리 저장 공간이 1개만 가지고 있다.
	// 메모리누수 현상을 방지..
	// DAO-> new를 이용해서 생성 -> 사용하지 않는 dAO가 오라클을 연결하고 있다.
	// DB에서 필수적으로 사용함 ***
	// 프로그래머, 디벨로퍼 (coder)
	public static FoodDAO newInstance() {
		if(dao==null)
			dao=new FoodDAO();
		return dao;
	}
	// 5. 기능
	// 5-1. 카테고리 출력
	public List<CategoryVO> food_category_list(){
		
		//카테고리 1개의 정보(번호, 이미지,제목, 부제목) -> categoryVO
		List<CategoryVO> list = new ArrayList<CategoryVO>();
		try {
			getConnection();
			String sql="SELECT cno, title, subject,poster "
					+"FROM food_category "
					+"ORDER BY cno ASC";
			//String sql="SELECT /*+ INDEX_ASC(food_category fc_cno_pk)*/cno, title, subject, poster"
			//		+"FROM food_category ";
			// -> 자동 (primary ,unique)
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				CategoryVO vo=new CategoryVO();
				vo.setCno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setSubject(rs.getString(3));
				vo.setPoster(rs.getString(4));
				list.add(vo);
			}
			rs.close();
			// list -> 받아서 브라우저로 전송 실행
			//              ------- Servlet, JSP
		    //              Spring 안에는 웹으로 전송할 수 있는 Servlet이 따로 있음 spring이 직접 하는건아님 -> dispatcherServlet
			// 사용가능한 언어
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	//5-1-1. 카테고리 정보
	public CategoryVO food_category_info(int cno) {
		CategoryVO vo= new CategoryVO();
		try {
			
			getConnection();
			String sql="SELECT title,subject FROM food_category "
					+"WHERE cno="+cno;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setTitle(rs.getString(1));
			vo.setSubject(rs.getString(2));
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return vo;
	}
	
	// 5-2. 카테고리별  맛집 출력
	// List<FoodVO>
	public List<FoodVO> food_category_data(int cno){
		List<FoodVO> list=new ArrayList<FoodVO>();
		try {
			getConnection();
			String sql="SELECT fno,name,poster,address,phone,type "
					+"FROM food_house "
					+"WHERE cno="+cno;
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				FoodVO vo=new FoodVO();
				vo.setFno(rs.getInt(1));
				vo.setName(rs.getString(2));
				String poster=rs.getString(3);
				// 내부에 이미지 여러개중 한개 잘라오기
				poster=poster.substring(0,poster.indexOf("^"));
				poster=poster.replace('#', '&');
				vo.setPoster(poster);
				String address =rs.getString(4);
				address.substring(0,address.lastIndexOf("지")); //지번앞에서 주소를 잘라올라고 사용함
				vo.setAddress(address.trim()); //지번앞에 공백을 제거할려고 갖다둠
				vo.setPhone(rs.getString(5));
				vo.setType(rs.getString(6));
				
				list.add(vo);
			}
			/*
			 * https:에서 값을 가져올때 &를 #으로 바꿔서 가져왔음 이걸다시 &로 바꿔줘야함
			 * 
			 */
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	// List<FoodVO>
	// 5-3. 맛집 상세보기
	// FoodVO
	
	public FoodVO foodDetailData(int fno) {
		FoodVO vo = new FoodVO();
		try {
			getConnection();
			String sql="SELECT fno,cno name, poster, phone, type, address, "
					+ "time,parking, menu, price, score "
					+ "FROM food_house "
					+ "WHERE fno=?";
			ps=conn.prepareStatement(sql);
			// ?차 값을 채운다 -> JSP/프로젝트
			// 2차 -> Mybatis, 보안(비밀번호 암호화), 실시간 -> betch  
			// 3차 -> 오라클(MysQl), jpa  
			// 기반 ->MSA 기반  CI/ CD -> 설치 젠킨스
			ps.setInt(1, fno);
			// 실행 요청 -> 결과값 받기
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setFno(rs.getInt(1));
			vo.setCno(rs.getInt(2));
			vo.setName(rs.getString(3));
			vo.setPoster(rs.getString(4));
			vo.setPhone(rs.getString(5));
			vo.setTime(rs.getString(6));
			vo.setAddress(rs.getString(7));
			vo.setType(rs.getString(8));
			vo.setParking(rs.getString(9));
			vo.setMenu(rs.getString(10));
			vo.setPrice(rs.getString(11));
			vo.setScore(rs.getDouble(12));
					
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		 return vo;
	}
	
	// FoddVO
	// 5-4. 맛집 검색 -> AJax 화면검색
	// List<FoodVO>
	public List<FoodVO> foodFindData(String addr, int page){
		List<FoodVO> list =new ArrayList<FoodVO>();
		try {
			getConnection();
			/*
			String sql="SELECT fno, name, poster, score "
					+"FROM food_location "
					+"WHERE address LIKE '%'||?||'%'";
			//mysql = like concat('%',?,'%')
			*/
			String sql="SELECT fno,name, poster, score, num "
			            +"FROM (SELECT fno, name, psoter,score, orwnum as num "
					    +"FROM (SELECT fno,name, poseter, score "
			            +"FROM food_location "
					    +"WHERE address LIKE '%'||?||'%')) "
			            +"WHERE num BETWEEN ? AND ?";
			ps=conn.prepareStatement(sql);
			int rowSize=12;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			ps.setString(1, addr);
			ps.setInt(2, start);
			ps.setInt(3, end);
			
			// 결과값 읽기
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				FoodVO vo=new FoodVO();
				vo.setFno(rs.getInt(1));
				vo.setName(rs.getString(2));
				String poster=rs.getString(3);
				poster=poster.substring(0,poster.indexOf("^"));
				poster=poster.replace("#", "&");
				vo.setPoster(poster);
				vo.setScore(rs.getDouble(4));
				list
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
		
	}
	// 5-4-1 총페이지 -> 데이터(오라클) -> DAO(80%(
	public int foodRowCount(String addr) {
		int count=0;
		try
		{
			getConnection();
			String sql="SELECT COUNT(*) FROM food_location "
					+"WHERE address LIKE '%' ||?||'%'";
			ps=conn.prepareStatement(sql);
			ps.setString(1, addr);
			ResultSet rs=ps.executeQuery();
			rs.next();
			count=rs.getInt(1);
			rs.close();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	return count;
		
	}
	// 5-5. 댓글(CURD) -> 로그인
	
	
	
	

}
