const SigninButton = document.getElementById("signin");
const exitIcon = document.getElementById("exit");

exitIcon.onclick = function(){
    document.location.href="index.html"};

SigninButton.onclick = function (){
    var username= $("#newusername");
    var password = $("#newpassword").val();
    var email = $("#newemail").val();
    if (validateForm(password,email,username) == true && checkPwd(password) == true){
        signin(username.val(),email,password)
    }};


