package com.example.springboot.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.*;
import com.example.springboot.service.IBlogService;
import com.example.springboot.service.ICollectService;
import com.example.springboot.service.IFollowService;
import com.example.springboot.service.IFriendService;
import com.example.springboot.service.ITypeService;
import com.example.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;
    @Resource
    private ICollectService collectService;
    @Resource
    private ITypeService typeService;
    @Resource
    private IFriendService friendService;
    @Resource
    private IFollowService followService;

    /**
     * 检查博客可见性权限
     * @param blog 博客对象
     * @param currentUserId 当前用户ID
     * @return 是否有权限查看
     */
    private boolean hasVisibilityPermission(Blog blog, Integer currentUserId) {
        if (blog.getVisibility() == null || "public".equals(blog.getVisibility())) {
            return true; // 公开可见
        }
        
        if (blog.getUserId().equals(currentUserId)) {
            return true; // 作者本人可以看到
        }
        
        switch (blog.getVisibility()) {
            case "private":
                return false; // 仅作者可见
            case "friends":
                return friendService.isFriend(currentUserId, blog.getUserId()); // 好友可见
            case "followers":
                // 检查当前用户是否关注了博客作者
                return isFollowing(currentUserId, blog.getUserId());
            default:
                return true; // 默认公开
        }
    }
    
    /**
     * 检查用户是否关注了目标用户
     */
    private boolean isFollowing(Integer userId, Integer targetUserId) {
        try {
            // 这里需要调用Follow相关的服务来检查关注关系
            // 暂时简化为检查好友关系，你可以根据需要调整
            return friendService.isFriend(userId, targetUserId) || 
                   checkFollowRelation(userId, targetUserId);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查关注关系（需要Follow服务）
     */
    private boolean checkFollowRelation(Integer userId, Integer targetUserId) {
        try {
            return followService.isFollowing(userId, targetUserId);
        } catch (Exception e) {
            System.err.println("检查关注关系时出错: " + e.getMessage());
            return false;
        }
    }


    @GetMapping("/count")
    public Result count(){

        List<Type> list = typeService.list();

        Map<Integer, Long> map = blogService.list().stream().collect(Collectors.groupingBy(Blog::getTypeId,Collectors.counting()));

        JSONArray array = new JSONArray();

        for (Type type : list) {
            JSONObject object = new JSONObject();
            object.set("name",type.getName());
            object.set("value",map.getOrDefault(type.getId(),0L));
            array.add(object);
        }

        return Result.success(array);
    }

    @PostMapping
    public Result save(@RequestBody Blog blog) {
        System.out.println("接收到的博客数据:");
        System.out.println("标题: " + blog.getName());
        System.out.println("可见性: " + blog.getVisibility());
        System.out.println("内容长度: " + (blog.getContent() != null ? blog.getContent().length() : 0));
        
        if (blog.getId()==null){
            blog.setUserId(TokenUtils.getCurrentUser().getId());
            blog.setTime(DateUtil.now());
        }
        
        boolean result = blogService.saveOrUpdate(blog);
        System.out.println("保存结果: " + result + ", 博客ID: " + blog.getId());
        
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(blogService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(blogService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        Account account = TokenUtils.getCurrentUser();
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.equals(account.getRole(),"ROLE_ADMIN")){
            wrapper.eq(Blog::getUserId,account.getId());
        }
        return Result.success(blogService.list(wrapper));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        Blog blog = blogService.getById(id);
        System.out.println("查询博客ID " + id + " 的可见性: " + (blog != null ? blog.getVisibility() : "null"));
        
        if (blog == null) {
            return Result.error("404", "博客不存在");
        }
        
        // 检查可见性权限
        Account currentUser = TokenUtils.getCurrentUser();
        Integer currentUserId = currentUser != null ? currentUser.getId() : null;
        
        if (!hasVisibilityPermission(blog, currentUserId)) {
            System.out.println("博客 ID " + id + " 访问被拒绝，可见性: " + blog.getVisibility() + "，当前用户: " + currentUserId);
            return Result.error("403", "没有权限访问此内容");
        }
        
        System.out.println("博客 ID " + id + " 访问通过，可见性: " + blog.getVisibility() + "，当前用户: " + currentUserId);
        return Result.success(blog);
    }

    @GetMapping("/front/page")
    public Result findFrontPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam Integer typeId,
                           @RequestParam(defaultValue = "") String keyword) {

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Blog::getId);

        if (typeId!=0){
            queryWrapper.eq(Blog::getTypeId,typeId);
        }

        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.like(Blog::getName, keyword);
        }

        Page<Blog> page = blogService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 调试日志：检查查询结果中的visibility字段
        System.out.println("前端页面查询结果数量: " + page.getRecords().size());
        if (!page.getRecords().isEmpty()) {
            Blog firstBlog = page.getRecords().get(0);
            System.out.println("第一条博客的可见性: " + firstBlog.getVisibility());
            System.out.println("第一条博客的标题: " + firstBlog.getName());
        }

        // 获取当前用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        Integer currentUserId = currentUser != null ? currentUser.getId() : null;
        
        // 应用可见性过滤
        List<Blog> filteredBlogs = new ArrayList<>();
        for (Blog blog : page.getRecords()) {
            if (hasVisibilityPermission(blog, currentUserId)) {
                filteredBlogs.add(blog);
                System.out.println("博客 ID " + blog.getId() + " 通过权限检查，可见性: " + blog.getVisibility());
            } else {
                System.out.println("博客 ID " + blog.getId() + " 未通过权限检查，可见性: " + blog.getVisibility() + "，当前用户: " + currentUserId);
            }
        }
        
        // 更新页面记录
        page.setRecords(filteredBlogs);
        page.setTotal(filteredBlogs.size());

        Map<Integer, Long> map = collectService.list().stream().collect(Collectors.groupingBy(Collect::getItemId, Collectors.counting()));
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, currentUserId != null ? currentUserId : -1);
        List<Integer> collectList = collectService.list(wrapper).stream().map(Collect::getItemId).collect(Collectors.toList());

        for (Blog blog : page.getRecords()) {
            blog.setIsCollected(collectList.contains(blog.getId()));
            blog.setCount(Math.toIntExact(map.getOrDefault(blog.getId(), 0L)));
        }

        return Result.success(page);
    }

    @GetMapping("/user/page")
    public Result findUserPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam Integer userId,
                                @RequestParam(defaultValue = "") String keyword) {

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Blog::getId);

        queryWrapper.eq(Blog::getUserId,userId);

        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.like(Blog::getName, keyword);
        }

        Page<Blog> page = blogService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 获取当前用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        Integer currentUserId = currentUser != null ? currentUser.getId() : null;
        
        // 应用可见性过滤
        List<Blog> filteredBlogs = new ArrayList<>();
        for (Blog blog : page.getRecords()) {
            if (hasVisibilityPermission(blog, currentUserId)) {
                filteredBlogs.add(blog);
                System.out.println("用户页面 - 博客 ID " + blog.getId() + " 通过权限检查，可见性: " + blog.getVisibility());
            } else {
                System.out.println("用户页面 - 博客 ID " + blog.getId() + " 未通过权限检查，可见性: " + blog.getVisibility() + "，当前用户: " + currentUserId);
            }
        }
        
        // 更新页面记录
        page.setRecords(filteredBlogs);
        page.setTotal(filteredBlogs.size());

        Map<Integer, Long> map = collectService.list().stream().collect(Collectors.groupingBy(Collect::getItemId, Collectors.counting()));
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, currentUserId != null ? currentUserId : -1);
        List<Integer> collectList = collectService.list(wrapper).stream().map(Collect::getItemId).collect(Collectors.toList());

        for (Blog blog : page.getRecords()) {
            blog.setIsCollected(collectList.contains(blog.getId()));
            blog.setCount(Math.toIntExact(map.getOrDefault(blog.getId(), 0L)));
        }

        return Result.success(page);
    }

    @GetMapping("/collect/page")
    public Result findCollectPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam Integer userId,
                               @RequestParam(defaultValue = "") String keyword) {


        LambdaQueryWrapper<Collect> collectWrapper = new LambdaQueryWrapper<>();
        collectWrapper.eq(Collect::getUserId, userId);
        List<Integer> ids = collectService.list(collectWrapper).stream().map(Collect::getItemId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(ids)) return Result.success(collectService.page(new Page<>(),collectWrapper));

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Blog::getId);

        queryWrapper.in(Blog::getId,ids);

        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.like(Blog::getName, keyword);
        }

        Page<Blog> page = blogService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 获取当前用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        Integer currentUserId = currentUser != null ? currentUser.getId() : null;
        
        // 应用可见性过滤
        List<Blog> filteredBlogs = new ArrayList<>();
        for (Blog blog : page.getRecords()) {
            if (hasVisibilityPermission(blog, currentUserId)) {
                filteredBlogs.add(blog);
                System.out.println("收藏页面 - 博客 ID " + blog.getId() + " 通过权限检查，可见性: " + blog.getVisibility());
            } else {
                System.out.println("收藏页面 - 博客 ID " + blog.getId() + " 未通过权限检查，可见性: " + blog.getVisibility() + "，当前用户: " + currentUserId);
            }
        }
        
        // 更新页面记录
        page.setRecords(filteredBlogs);
        page.setTotal(filteredBlogs.size());

        Map<Integer, Long> map = collectService.list().stream().collect(Collectors.groupingBy(Collect::getItemId, Collectors.counting()));
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, currentUserId != null ? currentUserId : -1);
        List<Integer> collectList = collectService.list(wrapper).stream().map(Collect::getItemId).collect(Collectors.toList());

        for (Blog blog : page.getRecords()) {
            blog.setIsCollected(collectList.contains(blog.getId()));
            blog.setCount(Math.toIntExact(map.getOrDefault(blog.getId(), 0L)));
        }

        return Result.success(page);
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String keyword) {

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Blog::getId);

        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.like(Blog::getName, keyword);
        }

        Account account = TokenUtils.getCurrentUser();
        if (!StrUtil.equals(account.getRole(),"ROLE_ADMIN")){
            queryWrapper.eq(Blog::getUserId,account.getId());
        }

        return Result.success(blogService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

