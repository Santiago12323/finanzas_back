package com.ingresos.back.infrastructure.controller.category;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.model.Category;
import com.ingresos.back.domain.port.CategoryService;
import com.ingresos.back.infrastructure.controller.dto.CategoryDto;
import com.ingresos.back.infrastructure.mapper.CategoryDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categorías", description = "Operaciones para la gestión de categorías de gastos e ingresos")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDtoMapper categoryDtoMapper;

    @Operation(
            summary = "Crea una categoría personalizada",
            description = "Registra una nueva categoría para el usuario. El nombre no puede repetirse entre las categorías disponibles del usuario.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la categoría a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Mascotas\", \"type\": \"EXPENSE\", \"icon\": \"paw\", \"color\": \"#8B5CF6\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida o nombre repetido",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Ya existe una categoría con el nombre: Mascotas\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        Category created = categoryService.createCategory(categoryDtoMapper.toDomain(categoryDto));
        return new ResponseEntity<>(categoryDtoMapper.toDto(created), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtiene una categoría por ID",
            description = "Recupera los detalles de una categoría específica.",
            parameters = @Parameter(name = "id", description = "ID de la categoría", required = true, example = "a7b8c9d0-1111-2222-3333-444455556666"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Categoría no encontrada con ID: a7b8c9d0-1111-2222-3333-444455556666\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(categoryDtoMapper.toDto(categoryService.findById(id)));
    }

    @Operation(
            summary = "Obtiene las categorías disponibles de un usuario",
            description = "Devuelve las categorías por defecto del sistema más las personalizadas del usuario. Se puede filtrar por tipo: con EXPENSE incluye también las de tipo BOTH.",
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                    @Parameter(name = "type", description = "Filtra por tipo: EXPENSE, INCOME o BOTH (opcional)", example = "EXPENSE")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de categorías",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryDto>> findByUserId(
            @PathVariable String userId,
            @RequestParam(value = "type", required = false) CategoryType type) {
        return ResponseEntity.ok(categoryService.findAvailableByUserId(userId, type)
                .stream()
                .map(categoryDtoMapper::toDto)
                .toList());
    }

    @Operation(
            summary = "Actualiza una categoría personalizada",
            description = "Actualiza nombre, tipo, ícono y color. Las categorías por defecto no se pueden modificar.",
            parameters = @Parameter(name = "id", description = "ID de la categoría a actualizar", required = true, example = "a7b8c9d0-1111-2222-3333-444455556666"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de la categoría",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida o categoría por defecto",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Las categorías por defecto no se pueden modificar\"}"))),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Categoría no encontrada con ID: a7b8c9d0-1111-2222-3333-444455556666\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
        Category updated = categoryService.updateCategory(id, categoryDtoMapper.toDomain(categoryDto));
        return ResponseEntity.ok(categoryDtoMapper.toDto(updated));
    }

    @Operation(
            summary = "Elimina una categoría personalizada",
            description = "Elimina la categoría. No se puede eliminar si es por defecto o si tiene gastos o presupuestos asociados.",
            parameters = @Parameter(name = "id", description = "ID de la categoría a eliminar", required = true, example = "a7b8c9d0-1111-2222-3333-444455556666"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Categoría en uso o por defecto",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No se puede eliminar una categoría que tiene gastos o presupuestos asociados\"}"))),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Categoría no encontrada con ID: a7b8c9d0-1111-2222-3333-444455556666\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtiene las categorías de un usuario según su tipo",
            description = "Devuelve las categorías por defecto del sistema más las personalizadas del usuario que sirven para el tipo indicado. "
                    + "Con EXPENSE trae las de gastos y con INCOME las de ingresos; en ambos casos se incluyen también las de tipo BOTH. "
                    + "Con BOTH trae únicamente las que sirven para los dos usos. Úsalo para llenar el selector de categoría al crear un gasto o un ingreso.",
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                    @Parameter(name = "type", description = "Tipo de categoría: EXPENSE, INCOME o BOTH (en mayúsculas)", required = true, example = "EXPENSE")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de categorías del tipo indicado",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)),
                                    examples = @ExampleObject(value = "[{\"id\": \"a7b8c9d0-1111-2222-3333-444455556666\", \"name\": \"Alimentación\", \"type\": \"EXPENSE\", \"icon\": \"utensils\", \"color\": \"#F97316\", \"defaultCategory\": true}, {\"id\": \"c3d4e5f6-7777-8888-9999-000011112222\", \"name\": \"Mascotas\", \"type\": \"EXPENSE\", \"icon\": \"paw\", \"color\": \"#8B5CF6\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"defaultCategory\": false}]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Tipo inválido (debe ser EXPENSE, INCOME o BOTH)",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Tipo de categoría inválido\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<CategoryDto>> findByUserIdAndType(
            @PathVariable String userId,
            @PathVariable CategoryType type) {
        return ResponseEntity.ok(categoryService.findByUserIdAndType(userId, type)
                .stream()
                .map(categoryDtoMapper::toDto)
                .toList());
    }


}