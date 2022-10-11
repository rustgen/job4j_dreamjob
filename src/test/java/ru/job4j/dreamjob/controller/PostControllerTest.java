package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;
import ru.job4j.dreamjob.util.UserSession;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post"),
                new Post(2, "New post")
        );

        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);

        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenCreatePost() {
        Post post = new Post(1, "New post");
        post.setCity(new City(1, "London"));

        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);

        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.createPost(post, model, session);
        verify(postService).add(post);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(1, "New post");
        post.setCity(new City(1, "London"));

        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);

        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.updatePost(post, model, session);
        verify(postService).update(post);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        Post post = new Post(1, "New post");
        int id = post.getId();

        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findById(id)).thenReturn(post);
        CityService cityService = mock(CityService.class);

        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.formUpdatePost(model, id, session);
        verify(model).addAttribute("post", post);
        assertThat(page, is("updatePost"));
    }

    @Test
    public void whenAddPosts() {
        User user = new User(1, null, null);
        /*Post post = new Post(1, "New post");*/
        Post post = new Post(0, "Fill in the field",
                "Fill in the field", LocalDateTime.now(), new City(0, "Choose the city"));
        List<City> cities = Arrays.asList(
                new City(1, "New York"),
                new City(2, "Tokyo")
        );
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        UserSession userSession = mock(UserSession.class);
        when(postService.findAll()).thenReturn(posts);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.formAddPost(model, session);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("post", posts);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("addPost"));
    }
}