You are adding a new REST API endpoint to OpenAEV.

> Follow conventions from `backend.instructions.md` and `security.instructions.md`.

## Use the NEW style (package `io.openaev.api.*`)

Old style (`io.openaev.rest.*` extending `RestBehavior`) is legacy — do not use for new features.

## Controller template

```java
@RestController
@RequestMapping("/api/{entity-plural}")
@RequiredArgsConstructor
public class {Entity}Api {
  private final {Entity}Service service;

  // -- CREATE --
  @Operation(summary = "Create ...", description = "...")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  @AccessControl(actionPerformed = Action.CREATE, resourceType = ResourceType.XXX)
  @LogExecutionTime
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public {Entity}Output create(@Valid @RequestBody {Entity}Input input) { ... }

  // -- READ --
  @AccessControl(resourceId = "#entityId", actionPerformed = Action.READ, resourceType = ResourceType.XXX)
  @LogExecutionTime
  @GetMapping("/{entityId}")
  public {Entity}Output getById(@PathVariable String entityId) { ... }

  // -- SEARCH --
  @AccessControl(actionPerformed = Action.READ, resourceType = ResourceType.XXX)
  @LogExecutionTime
  @PostMapping("/search")
  public Page<{Entity}Output> search(@RequestBody @Valid SearchPaginationInput input) { ... }

  // -- UPDATE --
  @AccessControl(resourceId = "#entityId", actionPerformed = Action.WRITE, resourceType = ResourceType.XXX)
  @LogExecutionTime
  @PutMapping("/{entityId}")
  public {Entity}Output update(@PathVariable String entityId, @Valid @RequestBody {Entity}Input input) { ... }

  // -- DELETE --
  @AccessControl(resourceId = "#entityId", actionPerformed = Action.DELETE, resourceType = ResourceType.XXX)
  @LogExecutionTime
  @DeleteMapping("/{entityId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String entityId) { ... }
}
```

## Service template

```java
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class {Entity}Service {

  private final {Entity}Repository repository;
  private final ReferenceResolver referenceResolver;

  // -- CREATE --
  public {Entity} create({Entity}Input input) {
    {Entity} entity = {Entity}Mapper.fromInput(input);
    return repository.save(entity);
  }

  // -- READ --
  @Transactional(readOnly = true)
  public {Entity} findById(String id) {
    return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("{Entity}", id));
  }

  @Transactional(readOnly = true)
  public Page<{Entity}> search(SearchPaginationInput input) {
    return PaginationUtils.buildPaginationJPA(
        repository::findAll,
        input,
        {Entity}.class
    );
  }

  // -- UPDATE --
  public {Entity} update(String id, {Entity}Input input) {
    {Entity} entity = findById(id);
    {Entity}Mapper.applyInput(entity, input);
    return repository.save(entity);
  }

  // -- DELETE --
  public void delete(String id) {
    repository.deleteById(id);
  }
}
```

> ⚠️ **Important**: Always use `org.springframework.transaction.annotation.Transactional` — never `jakarta.transaction.Transactional` (which lacks `rollbackFor`, `readOnly`, etc.)
>
> ⚠️ **Self-call trap**: `@Transactional` does NOT work on self-calls (`this.method()`). Spring proxies only intercept external calls. If you need a transactional self-call, inject the service into itself or extract to a separate service.

## DTOs — immutable Java `record`

```java
public record {Entity}Input(
    @JsonProperty("entity_name") @NotBlank String name,
    @JsonProperty("entity_description") String description) {}

public record {Entity}Output(
    @JsonProperty("entity_id") @NotBlank String id,
    @JsonProperty("entity_name") @NotBlank String name,
    @JsonProperty("entity_description") String description) {}
```

## Checklist

- [ ] `@AccessControl` + `@LogExecutionTime` + `@Operation` on every endpoint
- [ ] DTOs (records) for input/output + Mapper class
- [ ] No business logic in controller
- [ ] Service: `@Transactional(rollbackFor = Exception.class)` at class level
- [ ] Service: `@Transactional(readOnly = true)` on all read methods
- [ ] Service: `findById()` throws `EntityNotFoundException` if not found
- [ ] Associations resolved via `ReferenceResolver` — never loop `findById()`
