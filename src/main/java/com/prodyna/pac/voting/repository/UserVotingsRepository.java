package com.prodyna.pac.voting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.UserVotings;

/**
 * Spring Data JPA repository for the UserVotings entity.
 */
public interface UserVotingsRepository extends JpaRepository<UserVotings,Long> {

    List<UserVotings> findByUserId(Long userId);

    List<UserVotings> findByVoteId(Long voteId);

    List<UserVotings> findByUserIdAndVoteId(Long userId, Long voteId);

    Long countByVoteId(Long voteId);

    Long countByVoteIdAndVoteOptionsId(Long voteId, Long voteOptionsId);
}
