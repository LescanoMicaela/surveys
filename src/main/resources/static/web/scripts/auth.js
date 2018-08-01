// const loginForm = document.getElementById("login-form");
// const loginButton = document.getElementById("login");
// const SigninButton = document.getElementById("signin");
// const logoutButton = document.getElementById("logout");
var datauser;

getData();

$(document).ajaxStart(function(){
    $("#loader").css("display", "flex");
});

$(document).ajaxComplete(function(){
    $("#loader").css("display", "none");
});



// logoutButton.onclick = function () {
//     logout();
// }

// loginButton.onclick = function (){
//     var email= $("#username").val();
//     var password = $("#password").val();
//     if (validateForm(password,email,false) == true ){
//     login(email, password);
// }};

// SigninButton.onclick = function (){
//     var username= $("#newusername");
//     var password = $("#newpassword").val();
//     var email = $("#newemail").val();
//     if (validateForm(password,email,username) == true ){
//         signin(username.val(),email,password)
//     }};

function login(username,password) {
        $.post("/api/login", { email: username, password: password }
        ).done(function()
        { console.log("logged in!");
            document.location.href="index.html"})
            .fail(function(){console.log("Wong email or password")})
}
function checkPwd(str) {
     if (str.search(/[^a-zA-Z0-9\!\@\#\$\%\^\&\*\(\)\_\+]/) != -1) {
    $("#alert").text("Password can only contain letters and numbers");
    }else if (str.length < 6) {
        $("#alert").text("Password must contain at least 6 characters");
    } else if (str.length > 20) {
        $("#alert").text("Password must contain less than 20 characters");
    } else if (str.search(/\d/) == -1) {
        $("#alert").text("Password must contain at least one number");
    } else if (str.search(/[a-zA-Z]/) == -1) {
        $("#alert").text("Password must contain at least one letter");
    }else{
        return true;
    }
}

function signin(username,email,password){
    if (checkPwd(password) == true){

        $.post("/api/users", {  userName: username, email: email, password: password }).done(function()
        { console.log("sign in!");
            login(email, password);
        }).fail(function(e){
            $("#alert").text(e.responseJSON.error)
        })};
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
            console.log("logged out");
            window.location.reload()
        })
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

