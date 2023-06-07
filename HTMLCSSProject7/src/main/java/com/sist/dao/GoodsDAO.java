package com.sist.dao;

import java.util.*;
import java.sql.*;
import com.sist.dbconn.*;
public class GoodsDAO {
	private Connection conn;
	private PreparedStatement ps;
	private static GoodsDAO dao;
	private CreateDataBase db=new CreateDataBase();
	
	public static GoodsDAO newInstance() {
		if(dao==null){
			dao=new GoodsDAO();
		}
		return dao;
	}
	
	// 기능
	public GoodsVO goodsDetailData(int no) {
		GoodsVO vo=new GoodsVO();
		try {
			conn= db.getConnection();
			String sql="SELECT no, goods_name,goods_sub, goods_price, goods_discount, goods_first_price, goods_delivery, goods_poster "
					+ "FROM goods_all "
					+ "WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			ResultSet rs=ps.executeQuery();
			
			rs.next();
			vo.setNo(1);
			vo.setName(rs.getString(2));
			vo.setSub(rs.getNString(3));
			vo.setPrice(rs.getString(4));
			vo.setDiscount(rs.getInt(5));
			vo.setFirst_price(rs.getString(6));
			vo.setDelivery(rs.getString(7));
			vo.setPoster(rs.getString(8));
			rs.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return vo;
	}
	// 1. 목록 -> 12
	// 2. 상세 -> 디자인된 파일로 출력

}
