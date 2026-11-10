# 001 – Release-Basis und Version vorbereiten

Status: Erledigt (10. November 2026)  
Priorität: Pflicht  
Abhängigkeiten: Keine; zuerst `tickets/README.md` lesen.

## Ziel

Ein eigener Branch `custom-release-0.20.0` baut auf dem offiziellen Release 0.20.0 auf. Release-Builds erhalten zuverlässig Version `0.20.0`.

## Umsetzung

1. Arbeitsverzeichnis, vorhandene Branches und Remote-Refs prüfen. Uncommittete Tickets und sonstige Nutzerdateien erhalten; keinen Reset oder automatischen Stash verwenden.
2. Den offiziellen Tag `v0.20.0` bei Bedarf abrufen und seinen Commit gegen `45c52925661ff51427cea46ebabcd3de656276cc` prüfen. Bei Abweichung Ursache klären und dokumentieren, statt still eine andere Basis zu wählen.
3. `custom-release-0.20.0` von dieser Basis erstellen. Existiert der Branch bereits, seinen Stand prüfen und vorhandene Arbeit fortsetzen, statt ihn neu zu setzen. Diese explizite Branch-Bezeichnung hat Vorrang vor allgemeinen Namenskonventionen.
4. Tickets auf dem Arbeitsbranch verfügbar halten. Den bestehenden `master` und die alten Custom-Release-Branches nicht verändern.
5. Den vorhandenen Versionsmechanismus nutzen: `-Pjellyfin.version=0.20.0` oder `JELLYFIN_VERSION=0.20.0`. Keinen globalen Entwicklungs-Fallback auf eine feste Release-Version ändern.
6. Einen konkreten Windows-Buildaufruf im Ticket-Ergebnis für die nachfolgenden Agents festhalten, z. B. `.\gradlew.bat -Pjellyfin.version=0.20.0 :app:assembleRelease`. Dokumentation wird in Ticket 005 ergänzt.

## Relevante Dateien

- `buildSrc/src/main/kotlin/VersionUtils.kt`
- `buildSrc/src/main/kotlin/Properties.kt`
- `app/build.gradle.kts`
- `tickets/README.md`

## Abnahmekriterien

- [x] Der Zielbranch basiert nachweislich auf dem angegebenen Release-Commit.
- [x] Es wurden keine alten Wiedergabe-Patches oder neueren Master-Änderungen eingemischt.
- [x] Die bestehende Versionsberechnung ergibt für `0.20.0` den Code `200099`; die tatsächliche APK-Prüfung erfolgt in Ticket 006.
- [x] Arbeitsdateien und alte Branches sind erhalten.
- [x] Commit-ID, Branch und vorgesehener Buildaufruf sind im Ergebnis dokumentiert.

## Ergebnis

Bearbeitet am 10. November 2026.

### Release-Basis und Erhalt vorhandener Arbeit

- Den offiziellen Tag mit `git fetch --no-tags upstream refs/tags/v0.20.0:refs/tags/v0.20.0` von `https://github.com/jellyfin/jellyfin-androidtv.git` abgerufen.
- `git rev-parse 'refs/tags/v0.20.0^{commit}'` bestätigt exakt `45c52925661ff51427cea46ebabcd3de656276cc`; keine Abweichung von der vorgegebenen Basis.
- Den zuvor nicht vorhandenen Branch `custom-release-0.20.0` direkt auf diesem Commit erstellt und ausgecheckt. `git rev-parse HEAD` liefert denselben Commit.
- `git diff --exit-code v0.20.0 --` und `git diff --cached --exit-code` sind leer und erfolgreich: Der versionierte Quellstand entspricht exakt dem offiziellen Release. Keine alten Custom-Patches, Cherry-picks oder neueren Master-Änderungen übernommen.
- `master` bleibt unverändert auf `74d4f01175da2e9829ac8d767d0f89c2aa57da5f`. Die vorhandenen Remote-Refs sind ebenfalls unverändert, insbesondere `origin/custom-release-0.19.7` auf `d90960725c61a41233165359b86b5fb59325c394`, `origin/custom-release-0.19.8` auf `9ce90db344692ea781263542c7afac7377ee8fec` und `origin/custom-release-0.19.9` auf `442603b3b073e0615554e543e4ee9c248c08204a`.
- Alle acht zuvor unversionierten Dateien unter `tickets/` sind beim Branchwechsel erhalten geblieben; ihre SHA-256-Prüfsummen waren vor und nach dem Wechsel identisch. Anschließend ausschließlich dieses Ticket und seinen Status in `tickets/README.md` aktualisiert. Die Tickets bleiben im Arbeitsverzeichnis unversioniert verfügbar; kein Commit, Reset, Stash oder Push durchgeführt.

### Version und Buildaufruf für nachfolgende Tickets

Der vorhandene Mechanismus bleibt unverändert: `app/build.gradle.kts` bezieht `versionName` über `project.getVersionName()` und berechnet daraus `versionCode` mit `getVersionCode(...)`. `Properties.kt` bevorzugt tatsächlich die Gradle-Property `jellyfin.version` vor `JELLYFIN_VERSION` (der Kommentar in `VersionUtils.kt` beschreibt die Reihenfolge umgekehrt). Deshalb für den Release-Build die Version explizit als Property übergeben:

```powershell
.\gradlew.bat -Pjellyfin.version=0.20.0 :app:assembleRelease
```

Alternativ ist `JELLYFIN_VERSION=0.20.0` möglich, sofern keine abweichende Gradle-Property gesetzt ist. Der Entwicklungs-Fallback bleibt `0.0.0-dev.1`.

Die Formel wurde anhand der Multiplikatoren und des Stable-Suffixes aus der vorhandenen `VersionUtils.kt` statisch nachgerechnet und gegen den Sollwert geprüft:

```text
0 * 1000000 + 20 * 10000 + 0 * 100 + 99 = 200099
```

Es wurde kein Gradle-/APK-Build ausgeführt. Die Ausführung der Kotlin-Versionsberechnung im Build sowie die Prüfung der tatsächlichen APK bleiben Ticket 006 vorbehalten. Das Projekt verlangt JDK 21; im aktuellen PATH wurde nur ein Java-8-Launcher gefunden, in den geprüften Standardverzeichnissen kein JDK 21. Die Buildumgebung muss vor dem Build geprüft bzw. bereitgestellt werden.

Geänderte Dateien: `tickets/001-release-basis-und-version.md`, `tickets/README.md`. Keine Änderung an App-Code oder Buildkonfiguration und damit keine neue funktionale Abweichung für `CUSTOM_DEVIATIONS.md`.
