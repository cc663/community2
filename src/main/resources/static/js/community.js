function post() {
    const questionId = $("#question_id").val();
    const content = $("#comment_content").val();
    if (!content){
        alert("评论不能为空哦！");
        return;
    }
    $.ajax({
        contentType: "application/json",
        type: "POST",
        url:
            "/comment",
        data:
            JSON.stringify({
                "type": 1,
                "content": content,
                "parentId": questionId
            }),
        success:
            function (response) {
                if (response.code == 200){
                    $("#comment_create").hide();
                    parent.location.reload();
                }else{
                    if (response.code == 2003){
                        let isAccepted= confirm(response.message);
                        if (isAccepted){
                            window.open("https://github.com/login/oauth/authorize?client_id=dceb5146c5f321081f81&redirect_uri=http://localhost:8087/callback&scope=user&state=1")
                            window.localStorage.setItem("closeable","true");
                        }
                    }
                    alert(response.message);
                }
                console.log(response);//打印到浏览器控制台
            },
        dataType: "json"
    });
}