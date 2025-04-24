package com.pedroporto.todosimple.dao;

import com.pedroporto.todosimple.models.User;
import com.pedroporto.todosimple.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAO {

    private final UserRepository userRepository;

    // Injeção de dependência do UserRepository
    @Autowired
    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método para encontrar um usuário por ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Método para salvar um usuário
    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    // Você pode adicionar outros métodos, como update, delete, etc.
}
