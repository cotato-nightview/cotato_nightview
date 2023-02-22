function getPaginationHTML(data, placeId) {
    let pagiantionHTML = '';
    let curPage = data.pageable.pageNumber;
    let startPage = Math.max(curPage - 4, 1);
    let endPage = Math.min(curPage + 9, data.totalPages);
    for (let i = startPage; i < endPage + 1; i++) {
        let j = i - 1;
        if (j == curPage) {
            pagiantionHTML += '<li onclick="getComment(' + placeId + ',' + j + ')" class="page-item"><a href="#" style="color: red" class="page-link">' + i + '</a></li>'
        } else {
            pagiantionHTML += '<li onclick="getComment(' + placeId + ',' + j + ')" class="page-item"><a href="#" class="page-link">' + i + '</a></li>'
        }
    }
    return pagiantionHTML;
}

function getCommentListHTML(data, authorizedUsername) {
    let commentList = data.content;
    let commentListHTML = "";
    console.log(authorizedUsername);
    for (let i = 0; i < commentList.length; i++) {
        let comment = '<div style="border-bottom: gray 1px solid; border-top: gray 1px solid" th:with="username=${#authentication.name}" id="commentwrapper' + commentList[i].id + '" class="list-group-item list-group-item-action py-3 lh-sm comment-list">\n' +
            '               <form>\n' +
            '                   <input type="hidden" name="currentPage" value="' + data.pageable.pageNumber + '">\n' +
            '                   <input type="hidden" name="username" value="' + commentList[i].username + '">\n' +
            '                   <input type="hidden" name="id" value="' + commentList[i].id + '">\n' +
            '                   <input type="hidden" name="placeId" value="' + commentList[i].placeId + '">\n' +
            '                   <input type="hidden" name="content" value="' + commentList[i].content + '">\n' +
            '               </form>\n' +
            '               <div class="d-flex w-100 align-items-center justify-content-between">\n' +
            '                   <strong class="mb-1">' + commentList[i].username + '</strong>\n' +
            '                   <small class="text-muted">' + elapsedTime(commentList[i].createdAt) + '</small>\n' +
            '               </div>\n' +
            '               <div style="text-align: start;padding-left: 30px" class="col-10 mb-1 small">' + commentList[i].content + '</div>';
        if (authorizedUsername == commentList[i].username) {
            comment += '<div style="padding-bottom: 5px; text-align: end" onclick="deleteComment(this)"><a href="#">삭제</a></div>\n' +
                '<div style="text-align: end" onclick="updateComment(this)"><a href="#">수정</a></div>';
        }
        comment += '</div>';
        commentListHTML += comment;
    }
    return commentListHTML;
}