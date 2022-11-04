package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Conference;
import com.example.springboot.entity.Vo.ConferenceQuery;
import com.example.springboot.mapper.ConferenceMapper;
import com.example.springboot.service.IConferenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-10
 */
@Service
public class ConferenceServiceImpl extends ServiceImpl<ConferenceMapper, Conference> implements IConferenceService {

    @Override
    public void pageQuery(Page<Conference> conferencePage, ConferenceQuery conferenceQuery) {
        QueryWrapper<Conference> queryWrapper = new QueryWrapper<>();
//        根据gmt_create字段降序排序
        queryWrapper.orderByDesc("begin_time");
        //如果数据非空，则根据查询条件进行分页数据查询
        Integer id = conferenceQuery.getId();
        String conferenceName = conferenceQuery.getConferenceName();
        String initiator = conferenceQuery.getInitiator();
        String beginTime = conferenceQuery.getBeginTime();
        String room = conferenceQuery.getRoom();

        if( !StringUtils.isEmpty(room)){
            queryWrapper.eq("room",room);
        }
        if(id !=null &&id>0){
            queryWrapper.eq("id",id);
        }
        if( !StringUtils.isEmpty(conferenceName)){
            queryWrapper.like("conference_name",conferenceName);
        }
        if( !StringUtils.isEmpty(initiator)){
            queryWrapper.eq("initiator",initiator);
        }
        if( !StringUtils.isEmpty(beginTime)){
            queryWrapper.ge("begin_time",beginTime);
        }
        baseMapper.selectPage(conferencePage,queryWrapper);


    }



}
