package com.library.repository;

import com.library.entity.Fine;

import java.util.List;

public interface FineRepositoryCustom {

    List<Fine> findAllFinesWithSum();
}
