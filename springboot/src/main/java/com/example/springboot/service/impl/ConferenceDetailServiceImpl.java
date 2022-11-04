package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.ConferenceDetail;
import com.example.springboot.entity.Vo.ConferenceDetailQuery;
import com.example.springboot.mapper.ConferenceDetailMapper;
import com.example.springboot.service.IConferenceDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-12
 */
@Service
public class ConferenceDetailServiceImpl extends ServiceImpl<ConferenceDetailMapper, ConferenceDetail> implements IConferenceDetailService {


    @Override
    public void pageQuery(Page<ConferenceDetail> conferenceDetailPage, ConferenceDetailQuery conferenceDetailQuery) {

        QueryWrapper<ConferenceDetail> queryWrapper =  new QueryWrapper<>();
        queryWrapper.orderByDesc("join_time");

        Integer conferenceId = conferenceDetailQuery.getConferenceId();
        Integer userId= conferenceDetailQuery.getUserId();
        Integer sign = conferenceDetailQuery.getSign();
        String joinTime = conferenceDetailQuery.getJoinTime();


        if(conferenceId !=null &&conferenceId>0){
            queryWrapper.eq("conference_id",conferenceId);
        }
        if(userId !=null &&userId>0){
            queryWrapper.eq("user_id",userId);
        }
        if(sign !=null &&sign>=0){
            queryWrapper.eq("sign",sign);
        }
        if( !StringUtils.isEmpty(joinTime)){
            queryWrapper.ge("joinTime",joinTime);
        }

        baseMapper.selectPage(conferenceDetailPage,queryWrapper);


    }
}
