package com.pedroporto.todosimple.controllers;

import java.net.URI;

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

import com.pedroporto.todosimple.models.User;
import com.pedroporto.todosimple.models.User.CreateUser;
import com.pedroporto.todosimple.models.User.UpdateUser;
import com.pedroporto.todosimple.services.UserService;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = this.userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    // Comentário: A responsabilidade de encontrar um usuário deve ser delegada para
    // o
    // UserService.
    // Dica: O método `findById` já está presente no `UserService`, então o
    // controlador
    // não deve se preocupar com a lógica de buscar um usuário.
    // Seguir o princípio de **Responsabilidade Única (SRP)** ajuda a manter a
    // coesão do código,
    // permitindo que cada classe ou método tenha uma única responsabilidade.
    // Refatore para usar o `UserService` diretamente no `TaskController` ou outros
    // lugares
    // onde a lógica de encontrar o usuário for necessária.
    @PostMapping
    @Validated(CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody User user) {
        this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // Comentário: No método `create`, a validação personalizada para o
    // **CreateUser** já é
    // aplicada, o que está correto.
    // Dica: Certifique-se de que os grupos de validação, como `CreateUser`, estejam
    // bem
    // definidos nas classes de modelo.
    // **Princípio da Validação**: A validação dos dados antes de salvar no banco é
    // uma boa prática
    // para garantir que os dados atendem aos requisitos de negócios.
    // Se você tem lógica de negócios adicional, pode separar a validação na camada
    // de serviço.
    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody User user) {
        user.setId(id);
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    // Comentário: O método `update` está tratando o ID corretamente, mas o controle
    // de erros
    // poderia ser melhorado.
    // Dica: Se o usuário não for encontrado, é interessante lançar uma exceção
    // específica
    // como `UserNotFoundException` e retornar um código de erro apropriado, como
    // `404 Not Found`.
    // Isso melhora a **clareza do erro** e ajuda na **experiência do usuário**.
    // Refatore para capturar a exceção e retornar o código de status correto.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            this.userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Comentário: O tratamento de exceção aqui é genérico.
            // Dica: Use exceções mais específicas para falhas de exclusão. Por exemplo,
            // criar uma
            // `UserDeletionException` que forneça mais contexto sobre a falha.
            // Além disso, considere utilizar **códigos de status adequados**, como `400 Bad
            // Request`
            // ou `404 Not Found`, dependendo do erro específico.
            return ResponseEntity.status(500).body(null); // Exemplo de 500, mas deve ser ajustado conforme o erro.
        }
    }
}
