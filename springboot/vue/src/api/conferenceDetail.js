import request from "@/utils/request";
export default {

    getConferenceDetail(pageNum, pageSize, conferenceDetailQuery) {
        return request({
            // es6反引号直接用模板进行参数替换
            url: `/conferenceDetail/page/${pageNum}/${pageSize}`,
            method: 'post',
            data: conferenceDetailQuery
        })
    },

    sign(conferenceDetailId){
        return request({
            url:`/conferenceDetail/shoudongsign/${conferenceDetailId}`,
            method:'put',
        })
    }


}