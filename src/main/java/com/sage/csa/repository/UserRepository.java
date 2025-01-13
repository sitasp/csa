package com.sage.csa.repository;

import com.sage.csa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(String userName);

    @Query("SELECT u.userId FROM User u WHERE u.userName = userName")
    Long findUserIdxByUserName(String userName);

    @Query("SELECT count(u) > 0 FROM User u WHERE u.userName = :userName")
    boolean existsByUserName(String userName);
}
