import request from "@/utils/request";
export default {

    getConference(pageNum, pageSize, conferenceQuery) {
        return request({
            // es6反引号直接用模板进行参数替换
            url: `/conference/page/${pageNum}/${pageSize}`,
            method: 'post',
            data: conferenceQuery
        })
    },


}