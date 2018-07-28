const loginForm = document.getElementById("login-form");
const loginButton = document.getElementById("login");
const SigninButton = document.getElementById("signin");
const logoutButton = document.getElementById("logout");
var datauser;

getData();


logoutButton.onclick = function () {
    logout();
}

loginButton.onclick = function (){
    var email= $("#username").val();
    var password = $("#password").val();
    if (validateForm(password,email,false) == true ){
    login(email, password);
}};

SigninButton.onclick = function (){
    var username= $("#newusername");
    var password = $("#newpassword").val();
    var email = $("#newemail").val();
    if (validateForm(password,email,username) == true ){
        signin(username.val(),email,password)
    }};

function login(username,password) {
    console.log(username,password);
        $.post("/api/login", { email: username, password: password }
        ).done(function()
        { console.log("logged in!"); })
            .fail(function(){console.log("Wong email or password")})
}

function signin(username,email,password){
    console.log(username,email,password);
    $.post("/api/users", {  userName: username, email: email, password: password }).done(function()
    { console.log("sign in!")
        login(email, password);
    });

}

function validateForm(password, email,username) {

    var emailFilter = /^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$/;

    if (!emailFilter.test(email)) {
        $("#alert").html('Please enter a valid e-mail address');
        return false;
    }
    if ((password.trim().length === 0)){
        $("#alert").html('Password can not be blank');
        return false;

    } if (username){
        if (username.val().trim().length === 0){
            $("#alert").html('Username can not be blank');
            return false;
        }else{
            return true;
        }
    } else{
        return true;
    }
}


function logout() {
    $.post("/api/logout")
        .done(function() {
            console.log("logged out"); })
}


function getData() {
    $.ajax({
        url: "/api/user_info",
        dataType: 'json',

        success: function (data) {
            datauser = data;
            console.log(datauser);
        }
})};

