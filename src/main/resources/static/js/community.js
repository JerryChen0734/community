function retrun() {
    var jumpUrl=window.location.href;
    window.localStorage.setItem("jumpUrl",jumpUrl);
}
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                       var url='https://github.com/login/oauth/authorize?client_id=5e6caff25e933fb47159&redirect_uri=http://localhost:8080/callback&scope=user&state=1';
                        var loginJumpWindow = window.open(url,"_self");
                        retrun();

                    }
                } else {

                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}