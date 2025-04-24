package com.pedroporto.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pedroporto.todosimple.services.TaskService;
import com.pedroporto.todosimple.services.UserService;
import com.pedroporto.todosimple.models.Task;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    // Comentário: A responsabilidade de encontrar um usuário deveria ser movida
    // para o UserController.
    // Dica: O método `findById` para buscar um usuário existe no `UserService`,
    // então o controller de tarefas
    // não deveria estar preocupado em chamar esse método diretamente.
    // Isso ajuda a seguir o princípio de **Responsabilidade Única** (SRP - Single
    // Responsibility Principle),
    // fazendo com que cada controller se ocupe apenas das entidades com as quais
    // ele trabalha.
    // Refatore o código para chamar o `UserController` ou mover a lógica para o
    // `UserService`.
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    // Comentário: O código de verificação do usuário está sendo feito no
    // `TaskController`, mas essa responsabilidade
    // deveria ser no `UserController`.
    // Dica: Em vez de chamar diretamente `userService.findById(userId)`, considere
    // delegar a responsabilidade de buscar o usuário
    // para o `UserController`. Isso ajudará a manter os controllers mais coesos e
    // focados em suas responsabilidades.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable long userId) {
        userService.findById(userId); // Comentário: Este código deveria ser movido para o UserController.
        List<Task> objs = this.taskService.findAllByUserId(userId);
        return ResponseEntity.ok().body(objs);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) {
        try {
            this.taskService.create(obj);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(obj.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            // Comentário: O tratamento de exceção aqui é genérico.
            // Dica: Em vez de capturar uma exceção genérica, crie exceções personalizadas
            // para cada erro específico (ex: TaskCreationException).
            // Também poderia ser mais interessante usar um código de status adequado (como
            // `400 Bad Request` em vez de 500).
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Task> update(@Valid @RequestBody Task obj, @PathVariable Long id) {
        try {
            obj.setId(id);
            this.taskService.update(obj);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Comentário: O método de atualização está sendo tratado de maneira muito
            // genérica. Se a tarefa não for encontrada,
            // deveria lançar uma exceção específica e responder com o código adequado (ex:
            // 404 Not Found).
            // Dica: Use exceções específicas, como `TaskNotFoundException`, e responda com
            // um código de erro apropriado.
            return ResponseEntity.status(404).body(null); // Por exemplo, 404 se a tarefa não existir
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            this.taskService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Comentário: O tratamento de exceção deve ser mais específico para erros de
            // exclusão.
            // Dica: Criar uma exceção personalizada para falhas ao excluir tarefas, como
            // `TaskDeletionException`.
            // Essa exceção pode fornecer informações mais específicas sobre o motivo da
            // falha na exclusão, como dependências relacionadas.
            return ResponseEntity.status(400).build(); // Por exemplo, 400 Bad Request
        }
    }
}
