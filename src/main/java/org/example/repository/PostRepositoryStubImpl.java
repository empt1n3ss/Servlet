package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepositoryStubImpl implements PostRepository {
  private static final String POST_NOT_FOUND_MESSAGE = "Post not found";

  private final Map<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong idCounter = new AtomicLong();

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long id = idCounter.incrementAndGet();
      post.setId(id);
      posts.put(id, post);
      return post;
    } else {
      if (posts.containsKey(post.getId())) {
        posts.put(post.getId(), post);
        return post;
      } else {
        throw new NotFoundException(POST_NOT_FOUND_MESSAGE);
      }
    }
  }
  public void removeById(long id) {
    if (posts.remove(id) == null) {
      throw new NotFoundException(POST_NOT_FOUND_MESSAGE);
    }
  }
}
