package com.jupiterspring.springblog.controllers;
import com.jupiterspring.springblog.model.Post;
import com.jupiterspring.springblog.model.User;
import com.jupiterspring.springblog.repositories.PostRepository;
import com.jupiterspring.springblog.repositories.UserRepository;
import com.jupiterspring.springblog.util.Methods;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final UserRepository userDao;
    Methods m = new Methods();

    public PostController(PostRepository postDao, UserRepository userDao){
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @GetMapping("/")
    public String topPosts(Model model){
        List<Post> all = postDao.findAll();
        List<Post> top3 = new ArrayList<>();
        List<Post> bottom3 = new ArrayList<>();
        List<String> homeImages = new ArrayList<>();
        homeImages.add("/img/grassPuppy.jpeg");
        homeImages.add("/img/snowDog.jpeg");
        homeImages.add("/img/puppy.jpg");
        homeImages.add("/img/snowHusky.jpeg");
        homeImages.add("/img/wildHusky.jpeg");
        homeImages.add("/img/wildPuppy.jpeg");
        for(int i = 0; i < 3; i++){
            top3.add(all.get(i));
        }
        for(int i = 3; i < 6; i++){
            bottom3.add(all.get(i));
        }
        model.addAttribute("postsTop", top3);
        model.addAttribute("postsBottom", bottom3);
        model.addAttribute("imgList", homeImages);
        model.addAttribute("title", "Home Page");
        return "home";
    }

    @GetMapping("/post")
    public String allPosts(Model model){
        List<Post> posts = postDao.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("title", "All Posts");
        return "posts/pages";
    }


    @GetMapping("/post/{id}")
    public String getOne(@PathVariable long id, Model model){
        Post post = postDao.getOne(id);
        User temp = userDao.getOne(1L);
        post.setUser(temp);
        model.addAttribute("post", post);
        model.addAttribute("title", post.getTitle());
        return "posts/show";
    }

    @GetMapping("/post/create")
    public String createForm(Model model){
        model.addAttribute("title", "Create Your Post");
        model.addAttribute("post", new Post());
        return "posts/createPost";
    }

    // duplicate title entry causes error page unique constraint
    @PostMapping("/post/create")
    public String createPost(@ModelAttribute Post post, Model model){
        User temp = userDao.getOne(1L);
        post.setDate(new Date());
        post.setUser(temp);
        post.setAuthor(temp.getUsername()); // i believe i only need this for now while my posts are from made up users
        Post savedPost = postDao.save(post); // to use to then send an email or something
        model.addAttribute("post", post);
        model.addAttribute("title", post.getTitle());
        return "posts/show";
    }

    @GetMapping("/post/search")
    public String searchPost(@RequestParam String searchValue, Model model){
        Post post = postDao.findPostByTitle(searchValue);
        model.addAttribute("post", post);
        model.addAttribute("title", post.getTitle());
        return "posts/show";
    }

    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable long id){
        postDao.deleteById(id);
        return "redirect:/postPage";
    }

    @PostMapping("/post/edit")
    public String editThePost(Model model, @ModelAttribute Post post){
        User temp = userDao.getOne(1L);
        post.setUser(temp);
        post.setAuthor(temp.getUsername());
        post.setDate(new Date());
        postDao.save(post);
        return getOne(post.getId(), model);
    }

    @GetMapping("/postPage")
    public String findIt(Model model, @PageableDefault(value = 5, sort = "title", direction = Sort.Direction.ASC) Pageable pageable){
        model.addAttribute("page", postDao.findAll(pageable));
        model.addAttribute("title", "Viewing Post Page " + (Integer.parseInt(String.valueOf(pageable.getPageNumber())) + 1));
        return "posts/pages";
    }

}
