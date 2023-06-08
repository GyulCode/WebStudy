package com.sist.dao;
/*
 * 1. 사용자
 *    1. 등록
 *    2. 정보보기
 *    3. 수정
 *    4. 삭제
 *    5. 목록출력
 *    
 * 2. 관리자 ***
 *    1. 부서별 통계
 *    2. 부서별 순위(급여순위) -> 문제임***
 * 
 */
import java.util.*;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.*;
import com.sist.dbconn.*;
public class EmpDAO {
	//연결
	private Connection conn;
	
	//송수신
	private PreparedStatement ps;
	
	//연결 -> 클래스화
	private CreateDataBase db=new CreateDataBase();
	
	//기능설정
	/*
	 * 1.등록
	 *    insert : 추가
	 *    INSERT INTO 테이블명(컬럼명...) VALUES(값...)
	 *      -> default / null값을 허용 : 필요한 데이터만 출력
	 *    ISNERT INTO 테이블명 VALUES(값..)
	 *      -> 8개를 추가(매개변수 8개를 사용하지 않고 클래스로 모아서 첨부 -> VO사용)
	 * 
	 */


	
	public void empInsert(EmpVO vo)
	{
		try
		{
			conn=db.getConnection();
			// JOIN => SELECT 
			// SUBQUERY => SELECT , INSERT , UPDATE , DELETE
			String sql="INSERT INTO myEmp VALUES("
					  +"(SELECT NVL(MAX(empno)+1,7000) FROM myEmp),"
					  +"?,?,?,SYSDATE,?,?,?)";
			ps=conn.prepareStatement(sql);
			// ? 에 값을 채운다 
			// setString() , setInt() , setDouble() , setDate()
			// -----------                            ---------
			// ''를 붙여준다 => table명 , 컬럼명에는 사용하지 않는다 
			// MyBatis => $ , #
			ps.setString(1, vo.getEname());
			ps.setString(2, vo.getJob());
			ps.setInt(3, vo.getMgr());
			ps.setInt(4, vo.getSal());
			ps.setInt(5, vo.getComm());
			ps.setInt(6, vo.getDeptno());
			
			// 실행 요청 
			ps.executeUpdate(); // commit()수행 
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.disConnection(conn, ps);
		}
	}
	
	// MGR / SAL / DEPTNO
	public List<Integer> empGetMgrData(){
		List<Integer> list = new ArrayList<Integer>();
		//제네릭은 반드시 클래스형만 사용(제네릭스:면접의 단골 제네릭스는 어떨떄 주로 사용하는가? = 자료형 통일)
		
		try {
			conn=db.getConnection();
			String sql="SELECT DISTINCT mgr FROM myEmp";
			ps=conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return list;
	}

	// 수정필요
	public List<Integer> empGetSalData()
	{
		List<Integer> list=new ArrayList<Integer>();
		// 제네릭는 반드시 클래스형만 사용 (제네릭스:면접의 단골)
		// 50:50 
		try
		{
			conn=db.getConnection();
			String sql="SELECT DISTINCT sal FROM myEmp";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				list.add(rs.getInt(1));
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.disConnection(conn, ps);
		}
		return list;
	}
	
	public List<Integer> empGetDeptnoData()
	{
		List<Integer> list=new ArrayList<Integer>();
		// 제네릭는 반드시 클래스형만 사용 (제네릭스:면접의 단골)
		// 50:50 
		try
		{
			conn=db.getConnection();
			String sql="SELECT DISTINCT deptno FROM myEmp";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				list.add(rs.getInt(1));
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.disConnection(conn, ps);
		}
		return list;
	}
	
	public List<String> empGetJobData()
	{
		List<String> list=new ArrayList<String>();
		// 제네릭는 반드시 클래스형만 사용 (제네릭스:면접의 단골)
		// 50:50 
		try
		{
			conn=db.getConnection();
			String sql="SELECT DISTINCT job FROM myEmp";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				list.add(rs.getString(1));
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.disConnection(conn, ps);
		}
		return list;
	}
	
	// 첫번째 문제 -> 오라클 조인으로 바꿔서 제출해야함 ********************
	public List<EmpVO> empListData()
	{
		List<EmpVO> list=new ArrayList<EmpVO>();
		try
		{
			conn=db.getConnection();
			
			// 첫번째 문제 => 오라클 조인 
			String sql="SELECT empno,ename,job,TO_CHAR(hiredate,'YYYY-MM-DD'),TO_CHAR(sal,'L999,999'),NVL(comm,0),dname,loc,grade "	
					  +"FROM myEmp me JOIN myDept md "
					  +"ON me.deptno=md.deptno "
					  +"JOIN myGrade ms "
					  +"ON me.sal BETWEEN ms.losal AND ms.hisal "
					  +"ORDER BY empno DESC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setDbday(rs.getString(4));
				vo.setDbsal(rs.getString(5));
				vo.setComm(rs.getInt(6));
				vo.getDvo().setDname(rs.getString(7));
				vo.getDvo().setLoc(rs.getString(8));
				vo.getSvo().setGrade(rs.getInt(9));
				list.add(vo);
			}
			rs.close();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.disConnection(conn, ps);
		}
		return list;
	}
	
	//상세보기
	public EmpVO empDetailData(int empno)
	{
		EmpVO vo=new EmpVO();
		try
		{
			conn=db.getConnection();
			// 2번째 문제 => 스칼라 서브쿼리로 변경**************
			String sql="SELECT empno,ename,job,NVL(mgr,0),TO_CHAR(hiredate,'YYYY-MM-DD'),TO_CHAR(sal,'L999,999'),NVL(comm,0),dname,loc,grade "
					  +"FROM myEmp me,myDept md,myGrade mg "
					  +"WHERE me.deptno=md.deptno "
					  +"AND sal BETWEEN losal AND hisal "
					  +"AND empno=?";
			ps=conn.prepareStatement(sql);
			//?에 값을 채운다 
			ps.setInt(1, empno);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setEmpno(rs.getInt(1));
			vo.setEname(rs.getString(2));
			vo.setJob(rs.getString(3));
			vo.setMgr(rs.getInt(4));
			vo.setDbday(rs.getString(5));
			vo.setDbsal(rs.getString(6));
			vo.setComm(rs.getInt(7));
			vo.getDvo().setDname(rs.getString(8));
			vo.getDvo().setLoc(rs.getString(9));
			vo.getSvo().setGrade(rs.getInt(10));
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.disConnection(conn, ps);
		}
		return vo;
	}
	
	//코드 필요
	public EmpVO empUpdateData() {
		EmpVO vo=new EmpVO();
		try {
			//쿼리문
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
		}
		return vo;
	}
	
	
	
	
	
	
	
	

}
