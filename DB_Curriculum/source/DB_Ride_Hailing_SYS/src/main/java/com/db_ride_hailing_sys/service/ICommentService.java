package com.db_ride_hailing_sys.service;

import com.db_ride_hailing_sys.dto.CommentOrderDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
public interface ICommentService extends IService<Comment> {

    Result commentOrder(CommentOrderDTO commentOrderDTO);

    Result showComment(Long id, Long reqId);
}
