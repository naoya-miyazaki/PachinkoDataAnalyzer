<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">

<body>
    <h1>データ入力</h1>
    
    <form action="insertData" method="post">
    <label for="store_name">店舗名:</label>
    <select name="store_name" required>
        <c:forEach var="store" items="${storeNames}">
            <option value="${store}">${store}</option>
        </c:forEach>
    </select>
    <br>

    <label for="model_name">機種名:</label>
    <select name="model_name" required>
        <c:forEach var="model" items="${modelNames}">
            <option value="${model}">${model}</option>
        </c:forEach>
    </select>
    <br>

    <label for="data_date">日付:</label>
    <input type="date" name="data_date" id="data_date" required>
    <br>

    <label for="pachinko_data">パチンコデータ:</label>
    <textarea name="pachinko_data" rows="10" cols="50" placeholder="台番号 G数 差枚 BB RB 合成確率 BB確率 RB確率" required></textarea>
    <br>

    <input type="submit" value="データ登録">
</form>

	<!-- ホーム画面に戻るボタン -->
    <br><br>
    <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>

</body>
</html>

