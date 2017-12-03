<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
  $(function() {
    $("#addForm").submit(function() {
      if (!checkEmpty("productName", "产品名称"))
        return false;
//          if (!checkEmpty("subTitle", "小标题"))
//              return false;
      if (!checkNumber("originalPrice", "原价格"))
        return false;
      if (!checkNumber("promotePrice", "优惠价格"))
        return false;
      if (!checkInt("stock", "库存"))
        return false;
      return true;
    });
  });
</script>

<title>产品管理</title>

<div class="workingArea">

  <ol class="breadcrumb">
    <li><a href="admin_category_list">所有分类</a></li>
    <li><a href="admin_product_list?categoryId=${category.id}">${category.categoryName}</a></li>
    <li class="active">产品管理</li>
  </ol>

  <div class="listDataTableDiv">
    <table
            class="table table-striped table-bordered table-hover  table-condensed">
      <thead>
      <tr class="success">
        <th>ID</th>
        <th>图片</th>
        <th>产品名称</th>
        <th>所属类别</th>
        <th>产品小标题</th>
        <th width="53px">原价格</th>
        <th width="80px">优惠价格</th>
        <th width="80px">库存数量</th>
        <th width="80px">图片管理</th>
        <th width="80px">设置属性</th>
        <th width="42px">编辑</th>
        <th width="42px">删除</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${productList}" var="product">
        <tr>
          <td>${product.id}</td>
          <td>
              <c:if test="${!empty product.firstProductImage}">
              <img width="40px" src="img/productSingle/${product.firstProductImage.id}.jpg">
              </c:if>
          </td>
          <td>${product.productName}</td>
          <td>${category.categoryName}</td>
          <td>${product.subTitle}</td>
          <td>${product.originalPrice}</td>
          <td>${product.promotePrice}</td>
          <td>${product.stock}</td>
          <td><a href="admin_productImage_list?productId=${product.id}"><span
                  class="glyphicon glyphicon-picture"></span></a></td>
          <td><a href="admin_propertyValue_edit?productId=${product.id}"><span
                  class="glyphicon glyphicon-th-list"></span></a></td>

          <td><a href="admin_product_edit?id=${product.id}"><span
                  class="glyphicon glyphicon-edit"></span></a></td>
          <td><a deleteLink="true"
                 href="admin_product_delete?id=${product.id}"><span
                  class="glyphicon glyphicon-trash"></span></a></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <div class="pageDiv">
    <%@include file="../include/admin/adminPage.jsp"%>
  </div>

  <div class="panel panel-warning addDiv">
    <div class="panel-heading">新增产品</div>
    <div class="panel-body">
      <form method="post" id="addForm" action="admin_product_insert">
        <table class="addTable">
          <tr>
            <td>产品名称</td>
            <td><input id="productName" name="productName" type="text"
                       class="form-control"></td>
          </tr>
          <tr>
            <td>产品小标题</td>
            <td><input id="subTitle" name="subTitle" type="text"
                       class="form-control"></td>
          </tr>
          <tr>
            <td>原价格</td>
            <td><input id="originalPrice" value="99.98" name="originalPrice" type="text"
                       class="form-control"></td>
          </tr>
          <tr>
            <td>优惠价格</td>
            <td><input id="promotePrice"  value="19.98" name="promotePrice" type="text"
                       class="form-control"></td>
          </tr>
          <tr>
            <td>库存</td>
            <td><input id="stock"  value="99" name="stock" type="text"
                       class="form-control"></td>
          </tr>
          <tr class="submitTR">
            <td colspan="2" align="center">
              <input type="hidden" name="categoryId" value="${category.id}">
              <button type="submit" class="btn btn-success">提 交</button>
            </td>
          </tr>
        </table>
      </form>
    </div>
  </div>
</div>
<%@include file="../include/admin/adminFooter.jsp"%>