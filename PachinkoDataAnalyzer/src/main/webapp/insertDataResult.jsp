<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>データ登録結果</title>
</head>
<body>
    <h1>データ登録結果</h1>
    
    <%
        String result = request.getParameter("result");
        if ("success".equals(result)) {
    %>
        <p>データの登録が正常に完了しました！</p>
        <a href="insertData.jsp">新しいデータを登録</a>
        <a href="index.jsp">メイン画面に戻る</a>
    <% 
        } else if ("duplicate".equals(result)) {
    %>
        <p>この店舗と日付のデータはすでに登録されています。重複を避けて再度登録してください。</p>
        <a href="insertData.jsp">戻る</a>
    <% 
        } else {
    %>
        <p>データの登録に失敗しました。再度お試しください。</p>
        <a href="insertData.jsp">戻る</a>
    <% 
        }
    %>
    
</body>
</html>
