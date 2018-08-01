setTimeout(function(){ $("body").show() }, 100);

const loginButton = document.getElementById("login");

loginButton.onclick = function (){
    var email= $("#username").val();
    var password = $("#password").val();
    if (validateForm(password,email,false) == true ){
        login(email, password);
    }};

function login(username,password) {
    console.log(username,password);
    $.post("/api/login", { email: username, password: password }
    ).done(function()
    { console.log("logged in!");
        document.location.href="index.html"})
        .fail(function(){$("#alert").text("Wong email or password")})
}

const exitIcon = document.getElementById("exit");

exitIcon.onclick = function(){
    console.log("hola")
    document.location.href="index.html"};