package com.pedroporto.todosimple.services;

import com.pedroporto.todosimple.dao.UserDAO;
import com.pedroporto.todosimple.models.User;
import com.pedroporto.todosimple.services.exceptions.ObjectNotFoundException;
import com.pedroporto.todosimple.services.exceptions.DataBindingViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO;

    // Injeção de dependência do UserDAO
    // Dica: Seguindo o princípio da **Inversão de Controle** (IoC), a injeção de
    // dependência via construtor é uma boa prática.
    // Isso melhora a testabilidade da classe, tornando possível injetar mocks em
    // testes e facilitando a criação do objeto sem acoplamento.
    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // **Princípio da Responsabilidade Única (SRP)**: Este método tem uma única
    // responsabilidade, que é a busca de um usuário por ID.
    // Dica: Aqui, estamos delegando para o DAO a responsabilidade de interação com
    // o banco de dados, o que está correto.
    // Manter a responsabilidade de acessar a persistência separada ajuda a manter o
    // código mais modular e coeso.
    public User findById(Long id) {
        Optional<User> user = this.userDAO.findById(id); // Usando UserDAO para buscar o usuário
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    // **Princípio da Inversão de Dependências (DIP)**: A classe `UserService`
    // depende da abstração (DAO), não de detalhes concretos (repositório).
    // Dica: Continue utilizando o padrão de injeção de dependência para garantir
    // que o serviço não tenha dependências rígidas de implementação.
    // **Princípio da Coesão**: O método está claro em sua responsabilidade. Aqui,
    // ele apenas lida com a criação de um usuário,
    // delegando a persistência ao DAO.
    public User create(User obj) {
        obj.setId(null); // Garantindo que o ID seja nulo antes de criar um novo usuário
        return this.userDAO.save(obj); // Usando UserDAO para salvar o usuário
    }

    // **Princípio da Substituição de Liskov (LSP)**: Este método segue Liskov
    // porque podemos substituir `UserService` por outra implementação
    // de `UserDAO` sem quebrar o comportamento esperado. O método deve continuar
    // funcionando corretamente, mesmo que o `UserDAO` seja substituído.
    // Dica: Verifique se a lógica de atualização está tratando corretamente as
    // alterações.
    // **Princípio da Segregação de Interface (ISP)**: Este método não viola o ISP,
    // pois `UserService` tem uma única interface bem definida.
    public User update(User obj) {
        User existingUser = findById(obj.getId()); // Busca o usuário existente
        existingUser.setPassword(obj.getPassword()); // Atualiza a senha (exemplo de atributo)
        return this.userDAO.save(existingUser); // Usando UserDAO para salvar as alterações
    }

    // **Princípio da Responsabilidade Única (SRP)**: O método de exclusão agora
    // está fazendo duas coisas: verificar a existência do usuário
    // e executar a exclusão. Essas duas responsabilidades podem ser separadas.
    // **Princípio da Inversão de Dependências (DIP)**: A classe `UserService`
    // depende de `UserDAO` para realizar a exclusão.
    public void delete(Long id) {
        User user = findById(id); // Verifica se o usuário existe
        try {
            this.userDAO.deleteById(id); // Método correto para deletar um usuário
        } catch (Exception e) {
            // **Princípio da Clareza nas Exceções**: Melhorar a clareza da exceção que é
            // gerada.
            // Dica: Criar uma exceção personalizada, como `UserDeletionException`, para ser
            // mais específico.
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
