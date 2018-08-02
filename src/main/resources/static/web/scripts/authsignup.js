setTimeout(function(){ $("body").show() }, 100);

const SigninButton = document.getElementById("signin");
const exitIcon = document.getElementById("exit");

exitIcon.onclick = function(){document.location.href="index.html"};

SigninButton.onclick = function (){
    var username= $("#newusername").val();
    var password = $("#newpassword").val();
    var email = $("#newemail").val();

    if (validateForm(password,email,username) == true && checkPwd(password) == true){
        signin(username,email,password)
    }
};


