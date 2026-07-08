## Summary

<!-- What does this PR change, and why? -->

## Related issues

<!-- e.g. Closes #123 -->

## Type of change

- [ ] Bug fix (corrects an incorrect result or defect)
- [ ] New feature (e.g. a new calculation method or API)
- [ ] Refactor / internal change (no behaviour change)
- [ ] Documentation
- [ ] Build / CI / tooling

## Does this change any computed prayer time?

- [ ] No — results are identical; golden tests are untouched.
- [ ] Yes — I updated the affected golden tests and explained the reason above.

## Checklist

- [ ] Commits follow Conventional Commits and are small and focused.
- [ ] `./gradlew build` passes.
- [ ] `./gradlew ktlintCheck detekt` are clean.
- [ ] `explicitApi` + `allWarningsAsErrors` compile cleanly.
- [ ] Public API changes are documented (KDoc + example).
- [ ] Generated data was regenerated via `./gradlew :lib:generatePrayerData` (if the source JSON changed).
- [ ] No new runtime dependency was added to the library.
