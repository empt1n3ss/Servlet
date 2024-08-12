package org.example.servlet;

import org.example.controller.PostController;
import org.example.repository.PostRepository;
import org.example.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private static final String API_POST = "/api/posts";
  private static final String API_POSTS_ID_REGEX = "/api/posts/\\d+";
  private static final String GET_METHOD = "GET";
  private static final String POST_METHOD = "POST";
  private static final String DELETE_METHOD = "DELETE";

  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET_METHOD) && path.equals(API_POST)) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET_METHOD) && path.matches(API_POSTS_ID_REGEX)) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST_METHOD) && path.equals(API_POST)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE_METHOD) && path.matches(API_POSTS_ID_REGEX)) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

