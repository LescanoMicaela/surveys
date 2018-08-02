
var datauser;

getData();

$(document).ajaxStart(function(){
    $("#loader").css("display", "flex");
});

$(document).ajaxComplete(function(){
    $("#loader").css("display", "none");
});


function login(username,password) {
        $.post("/api/login", { email: username, password: password }
        ).done(function()
        {document.location.href="index.html"}
        ).fail(function(){makeAlert("Wrong email or password")})
}
function checkPwd(str) {
     if (str.search(/[^a-zA-Z0-9\!\@\#\$\%\^\&\*\(\)\_\+]/) != -1) {
       makeAlert("Password can only contain letters and numbers");
    }else if (str.length < 6) {
        makeAlert("Password must contain at least 6 characters");
    } else if (str.length > 20) {
       makeAlert("Password must contain less than 20 characters");
    } else if (str.search(/\d/) == -1) {
        makeAlert("Password must contain at least one number");
    } else if (str.search(/[a-zA-Z]/) == -1) {
        makeAlert("Password must contain at least one letter");
    }else{
        return true;
    }
}

function makeAlert(text){
    $("#alert").text(text);
}


function signin(username,email,password){
    if (checkPwd(password) == true){
        $.post("/api/users", {  userName: username, email: email, password: password }
        ).done(function() { login(email, password)}
        ).fail(function(e){makeAlert(e.responseJSON.error)})
    }
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
        .done(function() {window.location.reload()})
}


function getData() {
    $.ajax({
        url: "/api/user_info",
        dataType: 'json',
        success: function (data) {
            datauser = data;
        }
})};

