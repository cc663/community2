function comment(parentId, content, type ){
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
                "type": type,
                "content": content,
                "parentId": parentId
            }),
        success:
            function (response) {
                if (response.code == 200){
                    if (type == 1){
                        $("#comment_create").hide();
                        parent.location.reload();
                    }else{
                        //局部刷新二级评论
                        $("#comment-"+parentId).addClass("in");
                        get_second_comments(parentId);
                    }

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

function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    comment(questionId, content, 1)
}

function comment_reply(e){
    let commentId = e.getAttribute("id");
    let content = $("#comment_reply-"+commentId).val();
    comment(commentId, content, 2);
    //提交评论后 清空输入框
    $("#comment_reply-"+commentId).val("");
}

function get_second_comments(id){
    $.ajax({
        url: '/comment/'+id,
        type: 'GET',
        dataType: 'json',
        timeout: 1000,
        cache: false,
        beforeSend: LoadFunction, //加载执行方法
        error: erryFunction,  //错误执行方法
        success: succFunction //成功执行方法
    })
    function LoadFunction() {
        $("#list").html('加载中...');
    }
    function erryFunction() {
        alert("error");
    }

    function succFunction(tt) {
        //eval将字符串转成对象数组
        //var json = { "id": "10086", "uname": "zhangsan", "email": "zhangsan@qq.com" };
        //json = eval(json);
        //alert("===json:id=" + json.id + ",uname=" + json.uname + ",email=" + json.email);
        const json = eval(tt).data; //数组
        var items = [];

        $.each(json, function (index) {
            //循环获取数据
            var avatar = json[index].user.avatarUrl;
            var commenator = json[index].commenator;
            var content = json[index].content;
            var s_gmtCreate = json[index].gmtCreate;
            var gmtCreate = myformatter(new Date(s_gmtCreate));

            items += `<div class="media">
                                <div class="media-left">
                                    <a th:href="@{#}">
                                        <img class="media-object img-rounded" src="${avatar}">
                                    </a>
                                </div>
                                <div class="media-body">
                                    <h5 class="media-heading">${commenator}</h5>
                                    <div>
                                        ${content}
                                    </div>
                                    <div class="float-gly">
                                        <span class="pull-right">${gmtCreate}</span>
                                    </div>
                                </div>
                                <hr>
                            </div>`
        });

        console.log(items);
        let c = $("#js-html");
        c.html(items);
    }

}

function collapseIn(e) {
    let ifView = e.getAttribute("collapse-status");
    let v = e.getAttribute("id");
    let c = $("#comment-"+v);

    if (ifView){
        //若显示 就折叠
        c.removeClass("in");
        e.removeAttribute("collapse-status");
        e.classList.remove("comment-active");
    }else{
        //未显示 就显示且标记
        c.addClass("in");
        get_second_comments(v);
        e.setAttribute("collapse-status", "in");
        e.classList.add("comment-active");
    }
}

function myformatter(date) {

    var strDate = date.getFullYear() + "-";

    if (date.getMonth() < 10) {

        var s = date.getMonth() + 1 + "-";

        strDate += "0" + s;

    } else {

        strDate += date.getMonth() + 1 + "-";

    }

    if (date.getDate() < 10) {

        strDate += "0" + date.getDate();

    } else {

        strDate += date.getDate();

    }

    return strDate;

}
