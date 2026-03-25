---
description: Commit message style guide for the Mikansei project
---

# Mikansei Commit Style Guide

## Format

```
Scope: Imperative description of change
```

A **single line** subject with a **module scope** prefix, followed by a colon, then an **imperative-mood** description. No trailing period.

---

## Scope Prefixes

Use the Gradle module name that the change **primarily** affects. Capitalize it as a proper noun.

### Core Modules

| Prefix | Module path | When to use |
|---|---|---|
| `Build` | Gradle / build config | Dependencies, versions, AGP, SDK changes |
| `Domain` | `core/domain` | Use cases, repository interfaces, domain entities |
| `UI` | `core/ui` | Shared composables, extensions, navigation utilities |
| `Database` | `core/database` | DAOs, tables, migrations, repository implementations |
| `Danbooru` | `core/danbooru` | API client, interceptors, mappers, repository impl |
| `Network` | `core/network` | HTTP client, caching, image loading |
| `Model` | `core/model` | Shared data models, serializers |
| `Product` | `core/product` | Shared product-level UI (preference components, etc.) |
| `Resources` | `core/resources` | Strings, icons, drawables |
| `Preferences` | `core/preferences` | DataStore, settings flags |

### Feature Modules

For features, use the PascalCase version of the feature name or its primary concept. If the feature is deeply nested or has a multi-word name, combine them with a hyphen (e.g., `User-Settings`). 

**Examples:**

| Prefix | Module path |
|---|---|
| `Main` | `app` |
| `Posts` | `feature/home/posts` |
| `Viewer` | `feature/image` |
| `Search` | `feature/search` |
| `User-Settings` | `feature/user/settings` |

### Non-Module Prefixes

| Prefix | When to use |
|---|---|
| `Docs` | README, privacy policy, documentation only |
| `Agents` | Coding agent documents & guides |

---

## Multi-Part Commits

When a single logical change spans **multiple modules**, split it into separate commits per module and number them.

### Format

```
Scope: Description (N/M)
```

### Rules

1. **One commit per module** — each commit touches only files in its scope.
2. **Number sequentially** — lower layers first (Domain → Database/Danbooru → UI → Feature).
3. **Same description** across all parts, only the scope and part number differ.

### Examples

```
Domain: Cache favorite & vote into repository (1/2)
Database: Cache favorite & vote into repository (2/2)
```

```
UI: Add shared target post ID (1/3)
Posts: Add shared target post ID (2/3)
Search: Add shared target post ID (3/3)
```

```
Domain: Migrate Date to OffsetDateTime (1/4)
Danbooru: Migrate Date to OffsetDateTime (2/4)
Database: Migrate Date to OffsetDateTime (3/4)
Viewer: Migrate Date to OffsetDateTime (4/4)
```

---

## Action Verbs

Use **imperative mood** (as if giving a command). Preferred verbs and when to use them:

| Verb | Usage |
|---|---|
| `Implement` | New feature or component from scratch |
| `Introduce` | New concept, utility, or abstraction |
| `Integrate` | Wiring an existing component into another |
| `Refactor` | Restructuring without changing behavior |
| `Fix` | Bug fix |
| `Add` | Small addition to existing code |
| `Remove` | Deleting code or files |
| `Update` | Modifying existing behavior or upgrading deps |
| `Bump` | Version number increments |
| `Migrate` | Moving from one approach/type to another |
| `Simplify` | Reducing complexity |
| `Respect` | Making code honor a rule/setting it previously ignored |
| `Cache` | Adding caching for data |
| `Copy` | Vendoring external code |
| `Expose` | Making an internal API public |

---

## Good Examples (from history)

### New feature — single module
```
Danbooru: Implement ForceLoadFromCacheInterceptor
```
> 1 new file, 31 lines. Single focused addition.

### New feature — multi-module
```
Domain: Introduce TagCategoryRepository
Database: Add a new table tag_categories
Database: Implement TagCategoryRepository
```
> Each commit adds exactly one concern in one module.

### Bug fix
```
Posts: Fix scroll offset and session loading state
Domain: Fix search crash from multiple brackets query
```
> States **what** is broken, not the root cause.

### Refactoring
```
Domain: Refactor Favorite entity to sealed class with Regular and Group subtypes
Posts: Refactor hardcoded state key to constant
```
> Describes the **structural change**, not just "refactor code."

### Behavior change
```
Posts: Reflect post filter changes only when screen is in RESUMED state
Domain: Remove filtering on get posts
Domain: Respect safe mode in post rating filtering
```
> Describes the new behavior clearly.

### Build / dependencies
```
Build: Update dependencies
Build: Bump version to 1.1.0 (106)
Build: Enable split-APK building for common ABIs
Build: Add media3 decoder dependency
```

### Documentation  
```
Docs: Update README
```

### Utility / extension
```
UI: Add Context copyToClipboard extension
UI: Introduce Modifier thenIf extension
```

---

## Anti-Patterns (avoid these)

| ❌ Bad | ✅ Better | Why |
|---|---|---|
| `Update usages after dependency updates` | `Build: Update usages after dependency update` | Missing scope prefix |
| `Housekeeping` | `Build: Remove unused files` | Too vague, no scope |
| `Add temporary ExperimentalFoundationApi` | `Build: Add temporary ExperimentalFoundationApi opt-in` | Missing scope |
| `Use new ripple modifier` | `UI: Migrate to new ripple modifier` | Missing scope, unclear verb |
| `Refactor onNavigatedBackByGesture` | `UI: Refactor onNavigatedBackByGesture callback` | Missing scope |
| `fix bug` | `Posts: Fix crash on empty search query` | Too vague, missing scope |
| `Domain: Refactored stuff` | `Domain: Refactor Favorite entity to sealed class` | Past tense, vague |

---

## Staging Changes

**Commit only the lines that belong to the current change.** If a file contains both related and unrelated modifications, do **not** stage the whole file — cherry-pick the relevant hunks.

### Using interactive patch mode

```bash
git add -p <file>
```

Git will present each hunk of changes and prompt:

- **`y`** — stage this hunk
- **`n`** — skip this hunk
- **`s`** — split the hunk into smaller pieces (when a hunk contains both related and unrelated lines)
- **`e`** — manually edit the hunk (for fine-grained control when `s` isn't enough)

### Example scenario

You modified `PostsViewModel.kt` with two unrelated changes:
1. Fixed scroll offset calculation (related to the current commit)
2. Added a new logging statement (belongs to a different commit)

```bash
# Don't do this — it stages everything:
git add PostsViewModel.kt

# Do this instead — pick only the scroll offset fix:
git add -p PostsViewModel.kt
# Answer y/n per hunk, or s to split a hunk that mixes both changes
```

### Guidelines

- **One concern per commit** — if a file has changes for two different purposes, stage them in separate commits.
- **Prefer `git add -p`** over `git add <file>` when a file has mixed changes.
- **Use `git diff --staged`** to review exactly what will be committed before running `git commit`.
- **Use `git stash -k`** (keep index) to test that the staged changes compile and work independently.

---

## Checklist

Before committing, verify:

- [ ] Message starts with a recognized scope prefix
- [ ] Uses imperative mood (e.g., "Add" not "Added" or "Adds")
- [ ] Describes **what** changes, not **how** or **why**
- [ ] All files in the commit belong to the stated scope
- [ ] Only lines associated with this change are staged (use `git add -p` when needed)
- [ ] Multi-module changes are split into numbered parts
- [ ] Subject line is concise (≤72 characters preferred)
- [ ] No trailing period
