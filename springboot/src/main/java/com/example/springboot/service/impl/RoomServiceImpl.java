package com.example.springboot.service.impl;

import com.example.springboot.entity.Room;
import com.example.springboot.mapper.RoomMapper;
import com.example.springboot.service.IRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

}
