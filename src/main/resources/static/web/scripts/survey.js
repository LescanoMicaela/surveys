var survey;
var counter = 0;
var answers = [];
var surveyQuestionIDs= [];


$(document).ready(function () {

    $.ajax({
        url: makeUrl(),
        dataType: 'json',
        success: function (data) {
            survey = data;
            sortData(survey["survey-info"].QnA);
            showQuestions();
            nameSurvey();

        },
        error: function () {
            console.log("This is not your survey")
        }
    })
});

function sortData(data) {
    if (data != null) {
        data.sort(function (a, b) {
            var a1 = a.id, b1 = b.id;
            if (a1 == b1) return 0;
            return a1 > b1 ? 1 : -1;
        });
    }
}

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function makeUrl() {
    var userSurveyID = getParameterByName("us");
    console.log(userSurveyID);
    return '/api/user_survey_view/' + userSurveyID;
}





$("#startSurvey").click(function(){
    $(".progressdiv").toggle();
    $("#surveyPresentation").hide();
//	$("#surveyPresentation").slideToggle();
    $("#surveyLayout").show(900);

})

function showQuestions(){
    $("#question").text(survey["survey-info"].QnA[0].question);
    $("#next").click(function changeQuestionDisplay(){
        // postUserSurveyQuestion();
        if($("#answer").val() != "" ){
            counter +=1;
            widthProgressBar();
            answers.push($("#answer").val());
            surveyQuestionIDs.push(""+ survey["survey-info"].QnA[counter-1].id);
            $("#answer").val('');
            if ( survey["survey-info"].QnA[counter] == undefined){
                $("#surveyLayout").hide();
                $("#question").text("Thank you");
                $("#surveyLayout").slideToggle(500);
                $("#next").hide();
                $("#answer").hide();
                postUserSurveyQuestion();
            }else{
                console.log(counter);
                $("#surveyLayout").hide();
                $("#question").text(survey["survey-info"].QnA[counter].question);
                $("#surveyLayout").slideToggle(500);

            }
        }else{
            $("#alert").text("Answer can't be blank");
        }

    })

}

$('#answer').keypress(function(e){
    if(e.which == 13){//Enter key pressed
        $('#next').click();//Trigger search button click event
    }
});

$("#answer").keyup(function(){
    $("#alert").text("");
});

function widthProgressBar(){
    var progress = (counter*100) / survey["survey-info"].QnA.length;
    $("#progressBar").css("width", progress +"%");
}

function getAnswers(){
    var answer = $("#answer").val();
}

function postUserSurveyQuestion() {
    var userSurveyid = survey["u-survey-id"];
    $.post({
        url: "/api/userSurveys/" + userSurveyid + "/userSurveyAnswer",
        data: JSON.stringify({answer: answers , id: surveyQuestionIDs}),
        dataType: "text",
        contentType: "application/json"
    }).done(function () {
        console.log("answer saved");
        window.location.href = "index.html"

    }).fail(
        function (e) {
            $("#alert").text(e.responseText)
        });

}

function nameSurvey(){
    var h1 = $("#surveyTitle").text(survey["survey-info"].description);
}