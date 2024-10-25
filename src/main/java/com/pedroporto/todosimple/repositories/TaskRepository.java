package com.pedroporto.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.pedroporto.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> getByUser_Id(Long userId);

}
