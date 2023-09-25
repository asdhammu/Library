package com.library.dto;

import java.util.List;

public record BookPaginatedDTO(PaginatedDTO pagination, List<BookDTO> books) {
}
