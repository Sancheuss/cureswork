package com.suai.cureswork.crud.repo;

import com.suai.cureswork.crud.entity.Cells;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CellsRepo extends CrudRepository<Cells, Integer> {
    List<Cells> findAll();
    List<Cells> findAllByRow(Integer row);
    List<Cells> findBySubjectId(Integer subjectId);
    void deleteBySubjectId(Integer subjectId);
    Optional<Cells> findBySubjectIdAndRowAndColumn(Integer subjectId, Integer row, Integer column);

    void deleteAllBySubjectId(Integer subjectId);
}
