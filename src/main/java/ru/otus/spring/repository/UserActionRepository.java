package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.UserAction;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    Long countUserActionByUserId(Long userId);
}
