package com.db_ride_hailing_sys.controller;


import com.db_ride_hailing_sys.dto.CommentOrderDTO;
import com.db_ride_hailing_sys.dto.Result;
import com.db_ride_hailing_sys.service.ICommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gilfoyle
 * @since 2023-07-22
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @PostMapping("commentOrder")
    public Result commentOrder(@RequestBody CommentOrderDTO commentOrderDTO){
        return commentService.commentOrder(commentOrderDTO);
    }

    @GetMapping("showComment")
    public Result showComment(@RequestParam("id")Long id,@RequestParam("reqId")Long reqId){
        return commentService.showComment(id,reqId);
    }
}

