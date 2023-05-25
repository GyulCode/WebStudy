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

@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath()); //??
		// JSP
		// 1. 변환 -> 전송(HTML, XML, JSON)
		// 브라우저로 미리 알려준다 -> response html을 먼저 보낼껀데 한글이 포함되어 있다!
		response.setContentType("text/html;charset=UTF-8");
		// XML -> text/xml, JSON -> text/plain
		BoardDAO dao=BoardDAO.newInstance();
		List<BoardVO> list = dao.boardListData(1);
		//사용자의 브라우저에서 읽어가는 위치를 설정 -> OutputStream
		PrintWriter out=response.getWriter();
		out.println("<html>"); //태그만 출력
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>자유 게시판</h1>");
		out.println("<table width=700 border=1 bordercolor=blue>");
		out.println("<tr>");
		out.println("<th width=10%>번호</th>");
		out.println("<th width=45%>제목</th>");
		out.println("<th width=15%>이름</th>");
		out.println("<th width=20%>작성일</th>");
		out.println("<th width=10%>조회수</th>");
		out.println("</tr>");
		for(BoardVO vo:list) {
			
			out.println("<tr>");
			out.println("<th width=10% align =center>"+vo.getNo()+"</th>");
			out.println("<th width=45%>"+vo.getSubject()+"</th>");
			out.println("<th width=15% align =center>"+vo.getName()+"</th>");
			out.println("<th width=20% align =center>"+vo.getDbday()+"</th>");
			out.println("<th width=10% align =center>"+vo.getHit()+"</th>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
		
		
		
	}

}
