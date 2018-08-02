setTimeout(function(){ $("body").show() }, 100);

const loginButton = document.getElementById("login");

loginButton.onclick = function (){
    var email= $("#username").val();
    var password = $("#password").val();
    if (validateForm(password,email,false) == true ){
        login(email, password);
    }};

function login(username,password) {
    $.post("/api/login", { email: username, password: password }
    ).done( function(){goToHomePage()}
    ).fail(function(){$("#alert").text("Wong email or password")})
}

const exitIcon = document.getElementById("exit");

exitIcon.onclick = function(){goToHomePage()};

function goToHomePage(){
    document.location.href="index.html"
};