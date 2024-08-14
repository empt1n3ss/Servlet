package org.example.controller;

import com.google.gson.Gson;
import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
  private static final String APPLICATION_JSON = "application/json";
  private final Gson gson = new Gson();

  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  private void writeResponse(HttpServletResponse response, Object data) throws IOException {
    response.setContentType(APPLICATION_JSON);
    response.getWriter().print(gson.toJson(data));
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.all();
    final var gson = new Gson();
    response.getWriter().print(gson.toJson(data));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    try {
      writeResponse(response, service.getById(id));
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeResponse(response, new NotFoundException("Post not found"));
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException{
    Post post = gson.fromJson(body, Post.class);
    writeResponse(response, service.save(post));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException{
    try {
      service.removeById(id);
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeResponse(response, new NotFoundException("Post not found"));
    }
  }
}
