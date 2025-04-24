package com.pedroporto.todosimple.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = User.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {

    public interface CreateUser {
    }

    public interface UpdateUser {
    }

    public static final String TABLE_NAME = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    // Comentário: A validação de username está bem configurada, garantindo que seja
    // não-nulo, não vazio e tenha um tamanho adequado.
    // **Princípio da Validação**: É importante garantir que o nome de usuário seja
    // único e atenda aos critérios de tamanho.
    @Column(name = "username", nullable = false, length = 100, unique = true)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    // Comentário: A senha está sendo protegida corretamente usando o anotação
    // `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)`, para garantir que
    // não seja exposta em respostas JSON.
    // **Princípio de Segurança**: Nunca envie a senha em respostas JSON.
    // Dica: Use `@JsonProperty` apenas para a senha, garantindo que ela seja
    // excluída de
    // respostas e acessível apenas na criação e atualização.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 60)
    @NotNull(groups = { CreateUser.class, UpdateUser.class })
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class })
    @Size(groups = { CreateUser.class, UpdateUser.class }, min = 8, max = 60)
    private String password;

    // Comentário: O relacionamento `OneToMany` com as tarefas está bem estruturado.
    // **Princípio da Coesão**: A classe `User` tem um relacionamento com `Task`, o
    // que
    // indica que um usuário pode ter várias tarefas.
    // Dica: Garantir que o relacionamento de `OneToMany` não cause problemas de
    // carregamento excessivo de dados (ex.: problema de "Lazy Loading").
    // Adicionar o `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` para que
    // as tarefas não sejam incluídas nas respostas JSON automaticamente.
    @OneToMany(mappedBy = "user")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<>();

}
