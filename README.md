# Immutable Tickets - IncidentTicket Refactoring

## Implemented

Refactored `IncidentTicket` from a mutable class with public setters into a fully **immutable** class with a **fluent Builder**.

- All fields `private final`, no setters, private constructor (only accessible via `Builder.build()`)
- `IncidentTicket.Builder` static inner class with fluent API for all 10 fields (3 required, 7 optional)
- All validation centralized in `build()` using `Validation` helpers - no scattered checks anywhere else
- `List.copyOf()` for defensive copying of tags - null elements rejected, external mutation throws `UnsupportedOperationException`
- `toBuilder()` method for "update by copy" - creates a pre-populated Builder from existing ticket so you can make changes and get a new instance
- Refactored `TicketService` so `escalateToCritical()` and `assign()` return new tickets instead of mutating
- Fixed regex escape in `Validation.java` (`\s` to `\\s`)

`TryIt` demo shows: builder creation, update-by-copy (original unchanged), tags mutation blocked, and full builder with all options.

---

## Original Assignment

Exercise B - Immutable Classes (Incident Tickets)

### Narrative
A small CLI tool called **HelpLite** creates and manages support/incident tickets.
Today, `IncidentTicket` is **mutable**:
- multiple constructors
- public setters
- validation scattered across the codebase
- objects can be modified after being "created", causing audit/log inconsistencies

Refactor the design so `IncidentTicket` becomes **immutable** and is created using a **Builder**.

### What you have (Starter)
- `IncidentTicket` has public setters + several constructors.
- `TicketService` creates a ticket, then mutates it later (bad).
- Validation is duplicated and scattered, making it easy to miss checks.
- `TryIt` demonstrates how the same object can change unexpectedly.

### Tasks
1) Refactor `IncidentTicket` to an **immutable class**
   - private final fields
   - no setters
   - defensive copying for collections
   - safe getters (no internal state leakage)

2) Introduce `IncidentTicket.Builder`
   - Required: `id`, `reporterEmail`, `title`
   - Optional: `description`, `priority`, `tags`, `assigneeEmail`, `customerVisible`, `slaMinutes`, `source`
   - Builder should be fluent (`builder().id(...).title(...).build()`)

3) Centralize validation
   - Move ALL validation to `Builder.build()`
   - Use helpers in `Validation.java` (add more if needed)
   - Examples:
     - id: non-empty, length <= 20, only [A-Z0-9-]
     - reporterEmail/assigneeEmail: must look like an email
     - title: non-empty, length <= 80
     - priority: one of LOW/MEDIUM/HIGH/CRITICAL
     - slaMinutes: if provided, must be between 5 and 7,200

4) Update `TicketService`
   - Stop mutating a ticket after creation
   - Any "updates" should create a **new** ticket instance (e.g., by Builder copy/from method)
   - Keep the API simple; you can add `toBuilder()` or `Builder.from(existing)`

### Acceptance
- `IncidentTicket` has no public setters and fields are final.
- Tickets cannot be modified after creation (including tags list).
- Validation happens only in one place (`build()`).
- `TryIt` still works, but now demonstrates immutability.
- Code compiles and runs with the starter commands below.

### Build/Run
```
cd immutable-tickets/src
javac com/example/tickets/*.java TryIt.java
java TryIt
```
