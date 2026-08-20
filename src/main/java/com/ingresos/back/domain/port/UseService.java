package com.ingresos.back.domain.port;
import com.ingresos.back.domain.model.User;

import java.util.List;

public interface UseService {
    User createUser(User user);

    User findById(String id);

    User findByEmail(String email);

    List<User> findAllUsers();

    User updateUser(String id, User user);

    void deleteUser(String id);

    boolean existsByEmail(String email);
}
