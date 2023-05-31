package com.sist.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import com.sist.dao.*;

/*
 *                                   SQL문장 전송
 *    브라우저   <--------->   자바    <---------->    오라클
 *              HTML변환       |    실행된 데이터 읽기
 *                            Servlet/JSP(웹파일)
 *                            ------- ---자바+HTML
 *                            자바로만 사용
 *                            Server + let
 *                            서버에서 실행된 가벼운 프로그램
 *                            -> 수정시마다 컴파일 한다
 *                            -> --------------- 보완 : JSP
 *                            -> servlet, jsp -> 호출하는 것이 아니라 톰캣에 의해 URL주소를 호출해서 사용한다.
 *                      -> new FoodServlet() -> 톰캣
 *                      1) init() : 변수 초기화, 필요한 값을 읽는 경우
 *                                  자동로그인, 보안 ...
 *                      2) service() : 사용자가 요청한 html을 만드는 위치
 *                           = doGet() -> get
 *                           = doPost() -> post
 *                           = 브라우저로 html을 전송하는 위치
 *                      ------------------------------ JSP(메소드 영역)
 *                      3) destroy() : 메모리 해제
 *                          -> 페이지 이동 / 새로고침 / 브라우저 종료시
 *                      JSP -> 호출시에 자동으로 servlet으로 변경 (한개의 클래스를 만든다)
 *                      a.jsp -> a_jsp.java
 *                               ----------
 *                               
 *                            
 */
@WebServlet("/FoodServlet")
public class FoodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// html -> 브라우저 전송
		response.setContentType("text/html;charset=UTF-8");
		FoodDAO dao = FoodDAO.newInstance();
		List<FoodVO> list = dao.foodAllData();
		
		// 브라우저에 출력하는 내용물 -> HTML
		PrintWriter out=response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<script src=\"http://code.jquery.com/jquery.js\"></script>");
		out.println("<script>");
		out.println("$(function(){");
		out.println("$('#keyword').keyup(function(){");
		out.println("let k=$(this).val();");
		out.println("console.log(k)");
		out.println("$('#user-table > tbody > tr').hide()");
		out.println("let temp=$('#user-table > tbody > tr > td:nth-child(5n+2):contains(\"'+k+'\")');");
		out.println("$(temp).parent().show();");
		out.println("})");
		out.println("})");
		out.println("</script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>맛집 목록</h1>");
		out.println("<table border=1 bordercolor=black width=1024>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<input type=text size=30 id=keyword>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<table border=1 bordercolor=black width=1024 id=user-table>");
		out.println("<thead>");
		out.println("<tr>");
		out.println("<th></th>");
		out.println("<th>맛집명</th>");
		out.println("<th>주소</th>");
		out.println("<th>전화</th>");
		out.println("<th>음식종류</th>");
		out.println("</tr>");
		out.println("</thead>");
		out.println("<tbody>"); //Vue, React(view) -> table 출력이 안됨
		for(FoodVO vo : list){
			out.println("<tr>");
			out.println("<td><img src="+vo.getPoster()+" width=30 height=30></td>");
			out.println("<td>"+vo.getName()+"</td>");
			out.println("<td>"+vo.getAddress()+"</td>");
			out.println("<td>"+vo.getTel()+"</td>");
			out.println("<td>"+vo.getType()+"</td>");
			out.println("</tr>");
		}
		out.println("");
		out.println("</tbody>");
		out.println("</table>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

}