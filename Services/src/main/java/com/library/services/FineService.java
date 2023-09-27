package com.library.services;

import com.library.dto.FineDTO;
import com.library.dto.ResponseDTO;

import java.util.List;

public interface FineService {

    ResponseDTO calculateFines();
    ResponseDTO payFine(int cardId);
    List<FineDTO> getAllFines();
    List<FineDTO> getFineForCardId(int cardId);
}
