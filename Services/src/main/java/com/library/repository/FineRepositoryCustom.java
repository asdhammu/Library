package com.library.repository;

import com.library.dto.FineDTO;

import java.util.List;

public interface FineRepositoryCustom {

    List<FineDTO> findAllFinesWithSum();
}
