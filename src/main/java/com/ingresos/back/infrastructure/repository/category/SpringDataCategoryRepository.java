package com.ingresos.back.infrastructure.repository.category;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.infrastructure.entity.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, String> {

    @Query("SELECT c FROM CategoryEntity c WHERE c.user IS NULL OR c.user.id = :userId ORDER BY c.name")
    List<CategoryEntity> findAvailableByUserId(@Param("userId") String userId);

    @Query("""
            SELECT c FROM CategoryEntity c
            WHERE (c.user IS NULL OR c.user.id = :userId) AND c.type IN :types
            ORDER BY c.name
            """)
    List<CategoryEntity> findAvailableByUserIdAndTypes(@Param("userId") String userId,
                                                       @Param("types") List<CategoryType> types);

    List<CategoryEntity> findByUserIsNullOrderByName();

    boolean existsByNameAndUserIsNull(String name);
}