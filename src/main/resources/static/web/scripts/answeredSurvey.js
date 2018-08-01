var survey;
$(document).ready(function () {

    $.ajax({
        url: makeUrl(),
        dataType: 'json',
        success: function (data) {
            survey = data;
            sortData(survey["survey-info"].QnA);
            printQuestions();


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
    return '/api/user_survey_view/' + userSurveyID;
}


// survey["survey-info"].QnA[0].question
// survey["survey-info"].QnA[0].answer

function printQuestions(){
    var divContainer = document.getElementById("answeredSurvey");
    for (var i=0; i < survey["survey-info"].QnA.length; i++){
        var div = document.createElement("div");
        var h2 = document.createElement("h2");
        var p = document.createElement("p");
        h2.textContent=survey["survey-info"].QnA[i].question;
        p.textContent=survey["survey-info"].QnA[i].answer;
        div.appendChild(h2);
        div.appendChild(p);
        divContainer.appendChild(div);
    }
}