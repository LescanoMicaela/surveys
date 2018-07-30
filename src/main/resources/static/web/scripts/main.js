
const welcomeMessage = $("#userName");
var datauser;
const logOutButton = $("#logout");
const buttonTologAndSignin = $(".loggedout");

getData();
function getData() {
    $.ajax({
        url: "/api/user_info",
        dataType: 'json',

        success: function (data) {
            datauser = data;
            console.log(datauser);
            hideLogingButtonsandWelcome();
        }
    })};

function hideLogingButtonsandWelcome(){
    if (datauser.currentUser == null){
        logOutButton.hide();
        buttonTologAndSignin.show();
    }else{
       logOutButton.show();
       welcomeMessage.text(", "+ datauser.currentUser.name);
       buttonTologAndSignin.hide();
    }
}


$("#create-UserSurvey").click(function createUserSurvey(){
    // var url = $(this).data("id");
    $.post("/api/user_survey")
    .done(function(){ console.log("hola")})
        .done(function(e){  window.location.href = "userSurvey.html?us=" +""+e["UserSurvey-id"]})
        .fail( function(e){console.log(e.responseJSON.error)})

});