package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galeev.springcourse.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
