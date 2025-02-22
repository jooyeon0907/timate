package com.zerobase.timate.repository;

import com.zerobase.timate.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAuthKey(String emailAuthKey);


}
