# 004 – Bestehenden Schlüssel mit der offiziellen Signierung nutzen

Status: Vorbereitet (10. November 2026), bisherige Signierdaten fehlen  
Priorität: Pflicht  
Abhängigkeiten: 001 und 002; zuerst `tickets/README.md` lesen.

## Ziel

JoHennifin 0.20.0 lässt sich mit dem bisherigen Release-Schlüssel signieren. Die bereits vorhandene Upstream-Signierlogik bleibt die einzige Implementierung.

## Ausgangslage und Umsetzung

Upstream liest Gradle-Properties oder Umgebungsvariablen über `Project.getProperty`. Der alte Fork las andere Schlüssel aus `local.properties`. Folgende Zuordnung verwenden:

| Bisher in `local.properties` | Upstream-Property | Alternative Umgebungsvariable |
| --- | --- | --- |
| `signing.storeFile` | `keystore.file` | `KEYSTORE_FILE` |
| `signing.storePassword` | `keystore.password` | `KEYSTORE_PASSWORD` |
| `signing.keyAlias` | `signing.key.alias` | `SIGNING_KEY_ALIAS` |
| `signing.keyPassword` | `signing.key.password` | `SIGNING_KEY_PASSWORD` |

1. Den vorhandenen `signingConfigs`-Block in `app/build.gradle.kts` erhalten. Keinen zweiten Release-Signing-Block und keinen alten `local.properties`-Loader hinzufügen.
2. Prüfen, ob Keystore und Konfiguration verfügbar sind, ohne Passwörter oder Schlüsselmaterial auszugeben. Fehlen sie, den Bedarf konkret dokumentieren; keinen Ersatzschlüssel erzeugen.
3. Vorhandene Werte in eine lokale, nicht eingecheckte Gradle-Konfiguration oder Umgebungsvariablen übertragen, soweit Zugriff und Auftrag dies erlauben. Nur Platzhalter in Projektdateien und Ticket-Ergebnissen verwenden.
4. Bei `keystore.file` die Auflösung relativ zum App-Modul beachten; einen geprüften absoluten Pfad bevorzugen. Ein relativer Pfad darf nicht versehentlich auf einen anderen Keystore zeigen.
5. Den bisherigen Ausschluss `jellyfin-release-key.jks` in `.gitignore` wieder aufnehmen; falls der tatsächliche Keystore anders heißt, auch dessen Repository-Pfad ausschließen.
6. Setup und Property-Zuordnung für Ticket 005 dokumentieren. Falls keine Codeänderung nötig ist, ausdrücklich nur die Konfiguration und `.gitignore` ändern.

## Relevante Dateien

- `app/build.gradle.kts`
- `buildSrc/src/main/kotlin/Properties.kt`
- `.gitignore`
- Lokale Signierkonfiguration; nicht committen

## Abnahmekriterien

- [x] Upstream-Signierlogik ist erhalten und liest die vorgesehenen vier Werte.
- [ ] Der bisherige Release-Schlüssel wird verwendet; kein neuer Schlüssel wurde erzeugt.
- [x] Keystore und Geheimnisse sind nicht im Git-Diff, in Tickets oder Logs enthalten.
- [x] Fehlende Voraussetzungen sind klar benannt; ein unsigniertes APK wird nicht als fertiger Release ausgegeben.
- [x] Zertifikatsvergleich und signierter Release-Build sind an Ticket 006 übergeben oder mit tatsächlichem Ergebnis dokumentiert.

## Ergebnis

- Die Signierlogik in `app/build.gradle.kts` und der Property-Helper in `buildSrc/src/main/kotlin/Properties.kt` entsprechen `v0.20.0`. Die Release-Variante verwendet die Release-Signierkonfiguration, sobald alle vier Werte vorliegen.
- `.gitignore` schließt den bisherigen Dateinamen `jellyfin-release-key.jks` aus. `git check-ignore` bestätigt den Ausschluss im Projektstamm und im App-Modul.
- Im Checkout war kein Keystore und keine bisherige Release-APK vorhanden. Die alte `local.properties` fehlte. In den geprüften Gradle-Properties und in der aktuellen Umgebung waren keine Signierwerte gesetzt. Für die Einrichtung werden der bisherige Keystore, das Keystore-Passwort, der Schlüsselalias und das Schlüsselpasswort benötigt. Es wurde kein Ersatzschlüssel erzeugt.
- [Ticket 006](006-build-und-abnahme.md) übernimmt den signierten Build und den Vergleich mit dem SHA-256-Zertifikatsfingerabdruck der bisherigen Release-APK oder einer gleichwertigen Referenz. Bis zu diesem Vergleich ist ein Update der vorhandenen Installation nicht bestätigt.

## Lokale Einrichtung

Für [Ticket 005](005-fork-dokumentation.md) diese Zuordnung und den Einrichtungsweg übernehmen. Sobald die bisherigen Signierdaten vorliegen, die vier Werte gemäß Tabelle in die persönliche `gradle.properties` unter `GRADLE_USER_HOME` eintragen. Standardmäßig liegt sie unter `$env:USERPROFILE\.gradle\gradle.properties`.

```properties
keystore.file=C:/Pfad/zum/bisherigen/jellyfin-release-key.jks
keystore.password=<bisheriges Keystore-Passwort>
signing.key.alias=<bisheriger Alias>
signing.key.password=<bisheriges Schlüsselpasswort>
```

Einen geprüften absoluten Pfad mit Schrägstrichen verwenden. Gradle löst relative Pfade vom Verzeichnis `app/` aus auf. Die vier Umgebungsvariablen aus der Tabelle sind eine Alternative. Sind beide Quellen belegt, haben die Gradle-Properties Vorrang. Den Keystore außerhalb des Checkouts ablegen.
