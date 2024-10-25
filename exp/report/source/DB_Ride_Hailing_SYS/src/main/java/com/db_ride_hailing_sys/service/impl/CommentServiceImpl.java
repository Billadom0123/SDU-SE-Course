package com.db_ride_hailing_sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.db_ride_hailing_sys.dao.CommentDao;
import com.db_ride_hailing_sys.dao.IndentDao;
import com.db_ride_hailing_sys.dao.RequestDao;
import com.db_ride_hailing_sys.dto.CommentOrderDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.dto.ShowCommentDTO;
import com.db_ride_hailing_sys.entity.Comment;
import com.db_ride_hailing_sys.entity.Indent;
import com.db_ride_hailing_sys.entity.Request;
import com.db_ride_hailing_sys.service.ICommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements ICommentService {

    @Resource
    private IndentDao indentDao;
    @Resource
    private RequestDao requestDao;

    @Resource
    private CommentDao commentDao;

    @Override
    public Result commentOrder(CommentOrderDTO commentOrderDTO) {
        //伪造请求校验
        //comment是否合法
        Integer comment = commentOrderDTO.getComment();
        if(comment!=null&&(comment<0||comment>5)){
            return Result.fail("评价星级不合法,应为空或在1到5之间");
        }
        //是否匹配请求
        Long id = commentOrderDTO.getId();
        Long reqId = commentOrderDTO.getReqId();
        Request request = requestDao.queryRequestIdByReqId(reqId);
        if(!request.getCustomerId().equals(id)){
            return Result.fail("乘客与请求不匹配");
        }
        Indent indent = indentDao.queryOrderByReqId(reqId);
        if(indent==null){
            return Result.fail("不存在对应订单");
        }
        //生成评价记录
        Comment record = new Comment();
        record.setComment(comment);
        record.setOrder_id(indent.getId());
        record.setContent(commentOrderDTO.getContent());
        save(record);
        return Result.ok();
    }

    @Override
    public Result showComment(Long id, Long reqId) {
        //伪造请求校验
        //检验是否存在对应订单
        Indent indent = indentDao.queryExistingIndentByCustomerIdAndRequestId(reqId, id);
        if(indent==null){
            return Result.fail("不存在对应订单");
        }
        Double price = indent.getPrice();
        if(price==null){
            return Result.fail("该订单还未结束,不应该有评价");
        }
        //查询对应订单的评价
        Comment comment = commentDao.queryCommentByOrderId(indent.getId());
        ShowCommentDTO res = new ShowCommentDTO();
        if(comment==null){
            res.setContent("");
            res.setComment("");
            return Result.ok(res);
        }
        res.setComment(comment.getComment().toString());
        res.setContent(comment.getContent());
        return Result.ok(res);
    }
}
