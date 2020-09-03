/**
 * 点赞
 * @param btn
 * @param entityType
 * @param entityId
 * @param entityUserId
 * @param postId
 */
function like(btn,entityType,entityId,entityUserId,postId) {
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){
                // 成功
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            }else{
                // 失败
                alert(data.msg);
            }
        }
    );
}