You are adding a new JPA entity to OpenAEV.

## Quick Checklist

Before writing code, gather these inputs:
- Entity name (singular PascalCase, e.g. `PlatformGroup`)
- Table name (plural snake_case, e.g. `platform_groups`)
- Tenant-scoped (`TenantBase`) or platform-level (`Base`)?
- Fields with types and constraints

## Full Procedure

> **Follow [create-feature-module/SKILL.md](../skills/create-feature-module/SKILL.md)** — it covers entity creation end-to-end (Steps 1–9).

If you only need the entity + repository (no API, no frontend), follow Steps 1–3 of the skill.

## Conventions Reference

| Concern | Source of truth |
|---|---|
| Entity annotations, column naming, collections | [backend.instructions.md](../instructions/backend.instructions.md) |
| Table naming, FK constraints, tenant isolation | [database.instructions.md](../instructions/database.instructions.md) |
| Migration class structure | [new-migration.prompt.md](new-migration.prompt.md) |
| Tests (Fixture, Composer, integration test) | [testing.instructions.md](../instructions/testing.instructions.md) |

## Key Patterns (quick reference)

- `@ControlledUuidGeneration` for ID generation
- `@Queryable` on filterable fields
- Implement `isUserHasAccess(User user)` + `equals` / `hashCode`
- Collections: mutable (`new ArrayList<>()`) + `@Fetch(FetchMode.SUBSELECT)`
- Follow `Group.java` (tenant-scoped) or `Tenant.java` (platform-level) as reference
