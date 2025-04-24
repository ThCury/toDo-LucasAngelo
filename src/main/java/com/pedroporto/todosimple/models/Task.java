package com.pedroporto.todosimple.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = Task.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Task {

    public static final String TABLE_NAME = "task";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    // Comentário: O relacionamento ManyToOne é adequado, pois uma tarefa pertence a
    // um usuário.
    // **Princípio da Coesão**: Este relacionamento mantém a coesão entre a tarefa e
    // o usuário.
    // Dica: Garantir que a chave estrangeira seja não-nula e não seja atualizável é
    // uma boa prática,
    // pois a tarefa não deve alterar a relação com o usuário depois que foi criada.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    // Comentário: O campo description está sendo corretamente validado como
    // não-nulo.
    // **Princípio de Validação**: A anotação `@NotNull` garante que a descrição da
    // tarefa nunca será nula.
    // Dica: Certifique-se de que a descrição tenha uma validação mais robusta, caso
    // necessário, para garantir que ela
    // atenda aos requisitos de formato ou tamanho. Pode ser interessante utilizar
    // outras anotações de validação,
    // como `@Size(min = 5, max = 255)` para garantir que o tamanho da descrição
    // seja adequado.
    @Column(name = "description", nullable = false, length = 255)
    @NotNull
    private String description;

}
