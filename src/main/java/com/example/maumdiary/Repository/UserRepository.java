package com.example.maumdiary.Repository;

import com.example.maumdiary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findByNickname(String name);

    public List<User> findBySocialId(String socialId);


}
