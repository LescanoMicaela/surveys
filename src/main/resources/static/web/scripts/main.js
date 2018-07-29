
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
       welcomeMessage.text(", "+ datauser.currentUser.userName);
       buttonTologAndSignin.hide();
    }
}