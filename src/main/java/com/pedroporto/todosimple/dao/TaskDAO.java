package com.pedroporto.todosimple.dao;

import com.pedroporto.todosimple.models.Task;
import com.pedroporto.todosimple.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskDAO {

    private final TaskRepository taskRepository;

    // Injeção de dependência do TaskRepository
    @Autowired
    public TaskDAO(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Método para encontrar uma tarefa por ID
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    // Método para salvar uma tarefa
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    // Método para buscar tarefas por ID do usuário
    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUser_Id(userId);
    }

    // Método para excluir uma tarefa
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
