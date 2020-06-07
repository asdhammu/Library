package com.library.repository;

import com.library.dto.Fine;

import java.util.List;

public interface FineRepositoryCustom {

    List<Fine> findAllFinesWithSum();
}
