<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body  style="background: url(${rootPath}/images/m-91.jpg);">
	<center>
	<form action="${rootPath}manage/Restaurant_Update?rest.id=${rest.id}" method="post">
		<label>店名：</label><input type="text" name="rest.name" value="${rest.name}">
		<label>电话：</label><input type="text" name="rest.phone" value="${rest.phone}"><br>
		<label>地址：</label><input type="text" name="rest.address" value="${rest.address}">
		<label>简介：</label><input type="text" name="rest.introduce" value="${rest.introduce}"><br>
		<button type="submit">修改</button>
	</form>
</center>
</body>
</html>