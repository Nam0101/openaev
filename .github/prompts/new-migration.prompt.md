You are writing a Flyway migration for OpenAEV.

## Full Procedure

> **Follow [add-migration/SKILL.md](../skills/add-migration/SKILL.md)** — it has the complete template, tenant isolation patterns, ES reindex, and verification steps.

## Quick Reference

- **Location**: `openaev-api/src/main/java/io/openaev/migration/`
- **Naming**: `V4_{next_number}__Snake_case_description.java` (double underscore after version)
- **Find next number**: `ls openaev-api/src/main/java/io/openaev/migration/ | sort | tail -5`

## Rules

> Full rules: [database.instructions.md](../instructions/database.instructions.md)

- Use `statement.addBatch(...)` + `statement.executeBatch()` for multiple statements
- Tenant-scoped tables: `tenant_id VARCHAR(255) NOT NULL` + FK + index (see skill Step 3)
- Native SQL bypasses Hibernate tenant filter — always include `WHERE tenant_id = ...`
- Default tenant UUID: `2cffad3a-0001-4078-b0e2-ef74274022c3` (`Tenant.DEFAULT_TENANT_UUID`)
- If modifying an indexed entity: `DELETE FROM indexing_status` to trigger reindex (see skill Step 4)
