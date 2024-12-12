<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">

<body>
    <input type="hidden" name="store_name" value="${storeName}">
<h1>${storeName} のデータを入力</h1>
    
    
    <br>
    <input type="hidden" name="store_name" value="${storeName}">
	<form action="insertData" method="POST">
    <!-- 店舗名を選択するためのドロップダウンリスト -->
    <label for="store_name">店舗名</label>
    <select name="store_name" id="store_name" required>
        <option value="">店舗を選択</option>
        <c:forEach var="store" items="${storeName}">
            <option value="${store}" 
                <c:if test="${store == storeName}">selected</c:if>
            >${store}</option>
        </c:forEach>
    </select>

    <!-- 機種名を選択するためのドロップダウンリスト -->
    <label for="model_name">機種名</label>
    <select name="model_name" id="model_name" required>
        <option value="">機種を選択</option>
        <c:forEach var="model" items="${modelNames}">
            <option value="${model}" 
                <c:if test="${model == modelName}">selected</c:if>
            >${model}</option>
        </c:forEach>
    </select>

    <!-- データの日付を入力 -->
    <label for="data_date">データ日付</label>
    <input type="date" name="data_date" id="data_date" required>

    <!-- データ入力 -->
    <label for="pachinko_data">パチンコデータ</label>
    <textarea name="pachinko_data" id="pachinko_data" rows="10" required></textarea>

    <button type="submit">データを登録</button>
</form>




	<!-- ホーム画面に戻るボタン -->
    <br><br>
    <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>

</body>
</html>

