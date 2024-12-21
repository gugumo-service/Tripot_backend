package com.junior.repository.qna;


import com.junior.domain.admin.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long>, QnaRepositoryCustom {


}
