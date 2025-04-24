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

    // Comentário: O método findById está correto, mas poderia ser mais flexível
    // para lançar diferentes exceções
    // Dica: Adicione suporte para outras exceções como `InvalidIdException` ou
    // `DatabaseConnectionException`
    // que podem fornecer mais contexto dependendo do erro ocorrido.
    // Exemplo de modificação: Usar um bloco condicional que lance exceções
    // específicas dependendo do erro.
    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
    }

    @Transactional
    public Task create(Task obj) {
        // Comentário: A lógica de associar um usuário à tarefa pode ser extraída para
        // um método auxiliar.
        // Dica: Evite duplicação de código, criando um método para associar o usuário
        // ao objeto `Task`.
        User user = this.userService.findById(obj.getUser().getId()); // Encontra o usuário associado
        obj.setId(null); // Garante que a tarefa seja tratada como um novo objeto.
        obj.setUser(user); // Associa o usuário à tarefa.
        obj = this.taskRepository.save(obj); // Salva a tarefa no banco de dados.
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        // Comentário: A busca pela tarefa pelo ID está sendo repetida. O código pode
        // ser refatorado para
        // reduzir essa duplicação.
        // Dica: Considere reutilizar o método `findById` já existente para buscar a
        // tarefa e melhorar a legibilidade.
        Task newObj = this.findById(obj.getId()); // Busca a tarefa existente pelo ID.
        newObj.setDescription(obj.getDescription()); // Atualiza a descrição da tarefa.
        return this.taskRepository.save(newObj); // Salva a tarefa atualizada no banco.
    }

    public List<Task> findAllByUserId(Long userId) {
        // Comentário: Esse método está correto, mas poderia ser melhorado ao verificar
        // se o usuário existe
        // antes de tentar buscar suas tarefas.
        // Dica: Chame o método `userService.findById()` para garantir que o usuário
        // existe antes de buscar as tarefas.
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    public void delete(Long id) {
        // Comentário: A exclusão de tarefas pode ser melhorada com um tratamento mais
        // específico de exceções.
        // Dica: Considere criar uma exceção personalizada como `TaskDeletionException`
        // para fornecer mensagens mais específicas.
        findById(id); // Verifica se a tarefa existe antes de tentar excluí-la.
        try {
            this.taskRepository.deleteById(id); // Tenta excluir a tarefa.
        } catch (Exception e) {
            // Comentário: A exceção lançada aqui pode ser mais específica.
            // Dica: Lançar uma exceção mais informativa que relate claramente a falha no
            // processo de exclusão.
            throw new DataBindingViolationException(
                    "Não é possível excluir a tarefa pois ela está sendo referenciada por outras entidades.");
        }
    }
}
