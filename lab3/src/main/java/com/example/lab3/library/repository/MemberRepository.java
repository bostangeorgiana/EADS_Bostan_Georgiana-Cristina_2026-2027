package com.example.lab3.library.repository;

import com.example.lab3.library.model.Member;
import com.example.lab3.library.model.MemberRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Repository in memorie (fara baza de date): datele stau intr-un Map
@Repository
public class MemberRepository {

    private final Map<Long, Member> members = new ConcurrentHashMap<>();

    public MemberRepository() {
        members.put(1L, new Member(1L, "Ana", MemberRole.STUDENT));
        members.put(2L, new Member(2L, "Mihai", MemberRole.REGULAR));
        members.put(3L, new Member(3L, "Elena", MemberRole.PREMIUM));
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }

    public List<Member> findAll() {
        return List.copyOf(members.values());
    }
}
