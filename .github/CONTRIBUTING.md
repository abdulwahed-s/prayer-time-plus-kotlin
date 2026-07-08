# Contributing to prayer-time-plus

Thanks for your interest in improving **prayer-time-plus** — the Kotlin / JVM
port of an offline Islamic prayer-times engine. This guide covers how to set the
project up, the standards a change is held to, and how to get it merged.

The same engine also ships for
[Dart / Flutter](https://github.com/abdulwahed-s/prayer_time_plus) and
[Swift](https://github.com/abdulwahed-s/prayer-time-plus-swift). A change that
affects computed times should ideally be raised for all three ports so they stay
in lock-step.

## Ways to contribute

- **Report a bug** or an incorrect prayer time.
- **Request a feature** or a new calculation method.
- **Improve the docs** — the README, KDoc comments, or the example.
- **Send a pull request** — see the workflow below.

Open issues from the [templates](https://github.com/abdulwahed-s/prayer-time-plus-kotlin/issues/new/choose).
For anything security-related, **do not open a public issue** — follow
[SECURITY.md](SECURITY.md).

## Development setup

Requires a JDK 17+ toolchain (the library targets JVM 11 bytecode).

```bash
./gradlew build            # compile + test + lint
./gradlew test             # test suite — must be green
./gradlew ktlintCheck detekt   # formatting + static analysis — must be clean
./gradlew dokkaHtml        # API documentation
./gradlew run              # CLI demo
```

The build uses **explicit API mode** and treats **all warnings as errors**, so
every public declaration needs an explicit visibility and the tree must be
warning-free.

## Standards every change is held to

1. **Zero runtime dependencies.** The Kotlin standard library and the JDK only.
   Do not add a runtime dependency to `lib`; test dependencies are fine.
2. **Numeric parity is sacred.** Prayer times are validated to the minute against
   a fixed set of golden vectors. A refactor must not change any computed time.
   If a change *should* alter output (a genuine fix), update the affected golden
   tests in the same PR and explain why in the description.
3. **Everything stays green.** `./gradlew ktlintCheck detekt test` all pass;
   `explicitApi` and `allWarningsAsErrors` compile cleanly. New behaviour comes
   with tests.
4. **Public API is documented.** Every public declaration carries a `/** KDoc */`,
   and the main types carry a runnable example. Everything else is `internal`.

## Commit & PR conventions

- **[Conventional Commits](https://www.conventionalcommits.org/):**
  `type(scope): subject` in the imperative mood — e.g.
  `feat: add Singapore calculation method`, `fix: correct Isha rounding near DST`,
  `docs: clarify utcOffset handling`. Types: `feat`, `fix`, `test`, `docs`,
  `refactor`, `perf`, `chore`, `build`, `ci`.
- **Small, atomic commits** — one logical change each; the message describes only
  that change.
- Bundled data (method parameters and Auto resolution) is **generated** — edit
  the JSON under `tools/data/`, run `./gradlew :lib:generatePrayerData`, and
  commit the regenerated source; never hand-edit the generated files.
- Keep pull requests focused, fill in the template, and link the issue they
  close.

## Code of conduct

Be respectful and constructive. Harassment or abuse of any kind is not welcome in
issues, pull requests, or discussions.
