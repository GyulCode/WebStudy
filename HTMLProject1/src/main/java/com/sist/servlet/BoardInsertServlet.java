package com.sist.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;

/**
 * Servlet implementation class BoardInsertServlet
 */
@WebServlet("/BoardInsertServlet")
public class BoardInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardInsertServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //입력폼 전송 -> HTML(
		response.setContentType("text/html;charset=UTF-8");
		
		//메모리 HTML을 저장 -> 브라우저에서 읽어서 출력
		PrintWriter out=response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=html/table.css>");
		out.println("</head>");
		out.println("<body>");
		out.println("<cneter>");
		out.println("<h1>글쓰기</h1>");
		out.println("<form method=post action=BoardInsertServlet>");
		// 입력된 데이터를 한번에 -> action에 등록된 클래스로 전송
		// 메소드 => method=post => doPost()
		out.println("<table class=talbe_content width=700>");
		out.println("<tr>");
		out.println("<th width=15%>이름</th>");
		out.println("<td width=85%><input type=text name=name size=15 required></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<th width=15%>제목</th>");
		out.println("<td width=85%><input type=text name=subject size=50 required></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<th width=15%>내용</th>");
		out.println("<td width=85%><textarea rows=10 cols=50 name=content required></textarea></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<th width=15%>비밀번호</th>");
		out.println("<td width=85%><input type=password name=pwd size=10 required></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td colspan=2 align=center>");
		out.println("<input type=submit value=글쓰기>");
		out.println("<input type=button value=취소 onclick=\"javascript:history.back()\">");
		out.println("");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

	/*
	 * http://localhost:8080/HTMLProject1/BoardInsertServlet?
	 * name=%ED%99%8D%EA%B8%B8%EB%8F%99&subject=%E3%85%81%3B%E3%85%A3%E3%84%B4%EC%95%84&
	 * content=%E3%85%81%3B%E3%85%A3%E3%84%B4%EC%95%8C&pwd=123
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			request.setCharacterEncoding("UTF-8"); //한글 변환
			// 디코딩 -> byte[]를 한글로 변환
			// 자바 -> 2byte => 브라우저 1byte로 받는다 -> 2byte
		} catch(Exception ex) {}
		// name=%ED%99%8D%EA%B8%B8%EB%8F%99& 인코딩 상태
		// 홍길동 -> 디코딩 상태
		// 값 받아오기
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		
		System.out.println("이름:"+name);
		System.out.println("제목:"+subject);
		System.out.println("내용 :"+content);
		System.out.println("비번 :"+pwd);
		
		BoardVO vo=new BoardVO();
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		
		// 오라클로 INSERT 요청
		BoardDAO dao=BoardDAO.newInstance();
		dao.boardInsert(vo);
		
		//화면이동
		response.sendRedirect("BoardListServlet");
		
		//doGet(request, response);
	}

}
