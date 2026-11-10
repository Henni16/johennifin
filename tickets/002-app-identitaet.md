# 002 – App-ID und Provider-Adressen übernehmen

Status: Erledigt (10. November 2026)  
Priorität: Pflicht  
Abhängigkeiten: 001; zuerst `tickets/README.md` lesen.

## Ziel

Die Release-App behält `org.johennifin.androidtv`, sodass sie zusammen mit dem bisherigen Signaturschlüssel als Update der bestehenden JoHennifin-Installation funktioniert. Debug verwendet `org.johennifin.androidtv.debug`.

## Umsetzung

1. Die Identitätsänderungen aus Commit `442603b3b` gezielt mit `app/build.gradle.kts` vergleichen.
2. `defaultConfig.applicationId` auf `org.johennifin.androidtv` setzen. Den Code-Namespace `org.jellyfin.androidtv` und die Java-/Kotlin-Pakete beibehalten.
3. Generierte Ressourcen `app_id`, `app_search_suggest_authority` und `app_search_suggest_intent_data` für Release und Debug passend zur jeweiligen tatsächlichen App-ID erzeugen. Nicht mehr vom unveränderten Namespace ableiten.
4. Provider-Authorities, Manifest-Platzhalter und Suchkonfiguration auf Konsistenz prüfen. Bereits korrekt mit `${applicationId}` arbeitende Manifest-Einträge erhalten.
5. Die Upstream-Konfiguration für R8/Minifizierung und Resource Shrinking beibehalten.
6. Die Abweichung „eigene App-ID“ in `CUSTOM_DEVIATIONS.md` dokumentieren; Ticket 005 konsolidiert den Gesamttext.

## Relevante Dateien

- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- Ressourcen, die `app_id`, `app_search_suggest_authority` oder `app_search_suggest_intent_data` verwenden

## Abnahmekriterien

- [x] Release-ID: `org.johennifin.androidtv`.
- [x] Debug-ID: `org.johennifin.androidtv.debug`.
- [x] Such- und Provider-Adressen passen laut statischer Prüfung zur jeweiligen Variante und sind von den offiziellen Jellyfin-Adressen getrennt.
- [x] Namespace und Quellcode-Pakete wurden nicht umbenannt.
- [x] Kein Austausch des kompletten alten Gradle-Buildskripts; aktuelle Upstream-Funktionen bleiben erhalten.
- [x] Statische Prüfung dokumentiert; Prüfung der erzeugten Manifeste/APKs an Ticket 006 übergeben.

## Ergebnis

Der Diff von `442603b3b` gegen seinen Parent bestätigt die bisherige Release-ID und das Debug-Suffix. In `app/build.gradle.kts` setzt `defaultConfig` die Release-ID. Ein gemeinsamer `androidComponents.onVariants`-Block leitet die drei String-Ressourcen aus der finalen App-ID der jeweiligen Variante ab. Der Namespace `org.jellyfin.androidtv`, die Quellcode-Pakete, die R8-Konfiguration und das Resource Shrinking entsprechen dem Release-Tag `v0.20.0`.

### Statische Prüfung

| Variante | `app_id` | `app_search_suggest_authority` | `app_search_suggest_intent_data` |
| --- | --- | --- | --- |
| Release | `org.johennifin.androidtv` | `org.johennifin.androidtv.content` | `content://org.johennifin.androidtv.content/intent` |
| Debug | `org.johennifin.androidtv.debug` | `org.johennifin.androidtv.debug.content` | `content://org.johennifin.androidtv.debug.content/intent` |

- Per PowerShell-Assertions Gradle-Konfiguration, Manifest und `searchable.xml` abgeglichen. Die Suchressourcen referenzieren dieselbe Authority wie der Medien-Provider. Der Suchpfad `suggestions` entspricht dessen URI-Matcher.
- Die Manifest-Authorities `<App-ID>.androidx-startup`, `<App-ID>.content` und `<App-ID>.integration.provider.ImageProvider` mit beiden JoHennifin- und den Upstream-IDs ausgewertet. Die so ermittelten Authorities sind eindeutig. Medien- und Bild-Provider nutzen im Code `BuildConfig.APPLICATION_ID`.
- Der Diff gegen `v0.20.0` bestätigt die unveränderten Quellcode-Pakete, Manifest- und Suchkonfiguration. `git diff --check` war erfolgreich.

### Übergabe

[Ticket 006](006-build-und-abnahme.md) prüft die erzeugten Manifeste und APKs einschließlich der Suchressourcen und des Signaturvergleichs. Die Werte oben stammen aus der statischen Prüfung. Ein Build gehört zur Abnahme in Ticket 006.

Die App-ID ist in [`CUSTOM_DEVIATIONS.md`](../CUSTOM_DEVIATIONS.md) dokumentiert. Ticket 005 führt die Fork-Dokumentation zusammen.
