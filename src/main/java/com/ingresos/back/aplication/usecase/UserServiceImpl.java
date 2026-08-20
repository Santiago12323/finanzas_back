package com.ingresos.back.aplication.usecase;
import com.ingresos.back.domain.exception.UserException;
import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UseService {

    @Autowired
    private UserRepository userRepositoryPort;

    @Override
    public User createUser(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public User findById(String id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuario no encontrado con email: " + email));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User updateUser(String id, User user) {
        findById(id);
        user.setId(id);
        return userRepositoryPort.save(user);
    }

    @Override
    public void deleteUser(String id) {
        findById(id);
        userRepositoryPort.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryPort.findByEmail(email).isPresent();
    }
}