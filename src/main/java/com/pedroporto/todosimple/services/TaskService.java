package com.pedroporto.todosimple.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedroporto.todosimple.models.Task;
import com.pedroporto.todosimple.models.User;
import com.pedroporto.todosimple.repositories.TaskRepository;
import com.pedroporto.todosimple.services.exceptions.DataBindingViolationException;
import com.pedroporto.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = this.findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);

    }

    public List<Task> findAllByUserId(Long userId) {

        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);

        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }

    }
}
