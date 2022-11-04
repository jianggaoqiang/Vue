package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.ConferenceDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Vo.ConferenceDetailQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-12
 */
public interface IConferenceDetailService extends IService<ConferenceDetail> {



    void pageQuery(Page<ConferenceDetail> conferenceDetailPage, ConferenceDetailQuery conferenceDetailQuery);
}
