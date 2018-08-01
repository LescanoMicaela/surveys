setTimeout(function(){ $("body").show() }, 100);
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
            displayLoggedinAndLoggedOut();
        }
    })};

function displayLoggedinAndLoggedOut(){
    if (datauser.currentUser == null){
        $("#loggedOutView").show();
        $("#loggedInview").hide();
        logOutButton.hide();
        buttonTologAndSignin.show();

    }else{
        $("#loggedOutView").hide();
        $("#loggedInview").show();
       logOutButton.show();
       welcomeMessage.text(", "+ datauser.currentUser.name);
       buttonTologAndSignin.hide();
       showButtonToAnsweredSurvey();
    }
}


$("#create-UserSurvey").click(function createUserSurvey(){
    // var url = $(this).data("id");
    $.post("/api/user_survey")
    .done(function(){ console.log("hola")})
        .done(function(e){  window.location.href = "userSurvey.html?us=" +""+e["UserSurvey-id"]})
        .fail( function(e){console.log(e.responseJSON.error)})

});

$("#viewSurvey").click(function viewUserSurvey(){
    window.location.href = "answeredSurvey.html?us=" +""+datauser.currentUser.UserSurveyID;



});

function showButtonToAnsweredSurvey(){
    console.log(datauser.currentUser.anseredSurvey)
    if(datauser.currentUser.anseredSurvey === true ){
        console.log("hola")
        $("#answered").show();
        $("#notAnswered").hide();
    }else{
        $("#answered").hide();
        $("#notAnswered").show();

    }
}