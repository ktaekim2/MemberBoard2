package com.its.memberboard.repository;

import com.its.memberboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits + 1 where b.id = :id")
    void boardHits(@Param("id") Long id);

//    @Query(value = "select b from BoardEntity b where :searchType like concat('%', :q, '%')")
//    Page<BoardEntity> search(String searchType, String q, Pageable pageable);

    @Query(value = "select b from BoardEntity b where b.boardTitle like %:q%")
    Page<BoardEntity> searchTitle(String q, Pageable pageable);

    @Query(value = "select b from BoardEntity b where b.boardWriter like %:q%")
    Page<BoardEntity> searchWriter(String q, Pageable pageable);
}
