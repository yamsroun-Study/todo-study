package jocture.todo.repository;

import jocture.todo.entity.Todo;

import java.util.List;

public interface TodoRepository {

    List<Todo> findAll();

}
