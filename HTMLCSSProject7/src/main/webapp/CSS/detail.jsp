<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.dao.*"%>
    <!-- 자바코딩 부분은 꺾새%로 시작 -->
    <%
      String no=request.getParameter("no");
      if(no==null)
    	  no="1";
      GoodsDAO dao=GoodsDAO.newInstance();
      GoodsVO vo=dao.goodsDetailData(Integer.parseInt(no));
    %>
    
<!-- 
css_189 복붙
 -->
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HTML 폰트 응용</title>
<link rel="stylesheet" href="style.css">
<style type="text/css">
  .container{
    margin-top:50px;
    width:1200px;
  }
  .row{
    width:1000px;
    margin: 0px auto; /* 가운데 정렬 */
  }
  table{
    width:850px;
  }
  #image{
    width:100%;
    height:550px;
    border-radius: 10px
  }
  #title{
    font-size:20px;
    font-weight:bold;
  }
  #sub{
    color:gray;
  }
  #percent{
  font-size:25px;
  color:orange;
  font-weight:bold;
  }
  #price{
    font-size:30px;
    font-weight:bold;
  }
  #psub{
  font-size:20px;
  color:#999;
  }
  #label{
    font-size:15px;
    color:green;
    font-weight:bold;
  }
  #price2{
    font-size:30px;
    color:green;
    font-weight:bold;
  }
  #star{
    color:yellow;
    font-weight:bold;
  }
  #bold{
    font-weight:bold;
  }
  #count{
    color:gray;
  }
  img[src*="delivery3"],img[src*="point"]{
    width:20px;
    height:20px;
  }
  #price3{
    color:cyan;
  }
  #sel{
  width:100%;
  height:40px;
  }
  #cart, #buy{
    width:200px;
    height:70px;
    border:2px green solid;
    font-size:20px;
    font-weight:bold;
  }
  #cart{
  background-color:white;
  color:green;
  }
  #buy{
  background-color:green;
  color:white;
  }
</style> 
</head>
<body>
  <div class=container>
    <div class=row>
      <table class="table_content">
        <tr>
          <td width="50%" class=tdcenter rowspan="9">
          <img src="<%=vo.getPoster() %>"
          id ="image">
          </td>
          <td width="50%" class="tdcenter">
          <span id=title>
          <%=vo.getName() %>
          </span>
          </td>
        </tr>
        <tr>
          <td width="60%">
          <span id="sub"><%=vo.getSub() %>%</span>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <span id="percent"><%=vo.getDiscount() %>%</span> &nbsp;<span id="price"><%=vo.getFirst_price() %></span>
            <p id=psub>
            <del><%=vo.getPrice() %></del>
            </p>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <span id=label>첫구매할인가</span>&nbsp;<span id=price2><%=vo.getFirst_price() %></span>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <span id="star">★★★★★</span>&nbsp;
            <span id=bold;>4.9</span>
            <span id=count;>9</span>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <img src="https://recipe1.ezmember.co.kr/img/mobile/icon_delivery3.png">
            배송
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span id=delivery><%=vo.getDelivery() %></span>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <img src="https://recipe1.ezmember.co.kr/img/mobile/icon_point.png">
            적립
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span id=price3> 445원 </span><span> 적립 (모든 회원 구매액의 0.5% 적립)</span>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <select id="sel">
              <option>옵션선택</option>
            </select>
          </td>
        </tr>
        <tr>
          <td width="60%">
            <input type=button value="장바구니" id=cart>
            <input type=button value="바로구매" id=buy>
          </td>
        </tr>
      </table>
    </div>
  </div>
</body>
</html>