package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Conference;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Vo.ConferenceQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-10
 */
public interface IConferenceService extends IService<Conference> {

    void pageQuery(Page<Conference> conferencePage, ConferenceQuery conferenceQuery);
}
