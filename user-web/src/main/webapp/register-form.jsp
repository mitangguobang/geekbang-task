<head>
<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
	<title>My Home Page</title>
    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
</head>
<body>
	<div class="container">
		<form class="form-register"  method="post" action="register">
			<h1 class="h3 mb-3 font-weight-normal">注册</h1>
			<label for="inputName" class="sr-only">姓名：</label> <!-required->
            <input type="text" id="inputName"name="inputName" class="form-control" placeholder="请输入姓名" autofocus>
            <br>
			<label for="inputTel" class="sr-only">手机：</label>
            <input type="tel" id="inputTel" name="inputTel" class="form-control" placeholder="请输入手机号">
            <br>
			<label for="inputEmail" class="sr-only">邮箱：</label>
			<input type="email" id="inputEmail" name="inputEmail" class="form-control" placeholder="请输入电子邮箱地址">
			<br>
			<label for="inputPassword" class="sr-only">密码：</label>
			<input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="请输入密码" >
			<br><br>
			<button type="submit" >注册</button>
		</form>
	</div>
</body>