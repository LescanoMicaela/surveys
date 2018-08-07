setTimeout(function(){ $("body").show() }, 100);

const exitIcon = document.getElementById("exit");
const loginButton = document.getElementById("login");

loginButton.onclick = function (){
    var username= $("#username").val();
    var password = $("#password").val();
    if (validateForm(password,"hey@gmail.com",username) == true ) {
        login(username, password);

    }};
function login(username,password) {
    $.post("/api/login", { userName: username, password: password }
    ).done( function(){goToHomePage()}
    ).fail(function(){$("#alert").text("Wong username or password")})

}

exitIcon.onclick = function(){goToHomePage()};

function goToHomePage(){
    document.location.href="index.html"
};