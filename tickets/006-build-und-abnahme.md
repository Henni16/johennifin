# 006 – Build und Abnahme von JoHennifin 0.20.0

Status: Teilweise erledigt (10. November 2026). Debug-APK geprüft, signierter Release-Build und Shield-Abnahme offen.  
Priorität: Pflicht  
Abhängigkeiten: 002–005; zuerst `tickets/README.md` lesen.

## Ziel

Ein überprüftes Debug- und signiertes Release-APK liegen vor. Die Ergebnisse unterscheiden zwischen Build-Prüfung und tatsächlicher Wiedergabeprüfung auf der Shield.

## Umsetzung

1. Diff gegen Release-Commit `45c52925661ff51427cea46ebabcd3de656276cc` prüfen: nur beauftragte Identitäts-, Branding-, Signier- und Dokumentationsänderungen. Keine alten Playback-Patches, Dependency-Downgrades oder deaktivierte R8-Optimierung.
2. Toolchain prüfen: JDK 21, Android SDK gemäß Release-Konfiguration (Compile SDK 37), Gradle-Wrapper des Releases. Fehlende Komponenten benennen und entsprechend dem ausführenden Auftrag bereitstellen.
3. Debug und Release mit expliziter Version bauen (`.\gradlew.bat '-Pjellyfin.version=0.20.0' :app:assembleDebug :app:assembleRelease`). Für den signierten Release-Build die bisherigen Signierdaten gemäß [Ticket 004](004-release-signierung.md#lokale-einrichtung) bereitstellen. Bei fehlendem Schlüssel nur Debug bauen und Release-Signierung offen lassen.
4. Erzeugte APKs tatsächlich lokalisieren. Den verifizierten Release-Pfad im Ergebnis festhalten, damit die [Shield-Anleitung](../INSTALL_TO_SHIELD.md#2-tatsächlich-erzeugtes-apk-prüfen) auf das erzeugte Artefakt verweist. Mit Android-SDK-Werkzeugen App-ID, `versionName=0.20.0`, `versionCode=200099` und Labels prüfen. Die Provider-Authorities in den erzeugten Manifesten sowie die Suchressourcen in beiden APKs mit den Sollwerten aus [Ticket 002](002-app-identitaet.md#statische-prüfung) abgleichen. Die Release-Signatur mit `apksigner verify` kontrollieren.
5. Zertifikatsfingerabdruck mit vorhandener bisheriger Release-APK oder einer anderen verlässlichen Referenz vergleichen. Ohne Referenz Update-Kompatibilität als noch nicht bestätigt ausweisen.
6. Relevante bestehende Checks ausführen, soweit die Änderungen sie betreffen. Keine neuen Tests nur für statische Branding-Texte schreiben. Dateipfade, Buildbefehle und Ergebnisse dokumentieren.
7. Für die Geräteabnahme die folgende Matrix vorbereiten und bei autorisiertem Gerätezugriff ausführen. Keine Installation, keinen Push und keine Veröffentlichung aus diesem Ticket allein ableiten.

## Geräteabnahme

Zielgerät ist eine Nvidia Shield TV Pro (2019) am Sony Bravia A80J (2021). Die weitere Audiokette (HDMI-Verkabelung, AVR oder Soundbar und Ausgabeformat) sowie die konkreten Testdateien und Tonspuren sind noch zu erfassen. Für diese Abnahme lag kein autorisierter Zugriff auf die Shield vor.

| Prüfung | Erwartung / zu protokollieren | Ergebnis |
| --- | --- | --- |
| Update der vorhandenen JoHennifin-App | Gleiche Paket-ID und Signatur. Anmeldung und Einstellungen bleiben erhalten | Nicht geprüft. Gerätezugriff und signiertes Release-APK fehlen. |
| Start, Launcher und Einstellungen | JoHennifin-Name, Icon, Logo und TV-Banner prüfen. In Serverauswahl und Über-Seite muss `johennifin-androidtv 0.20.0 <Build-Typ>` stehen. Kopierwert der Über-Seite sowie deutsche und englische Texte gemäß [Ticket 003](003-branding.md#prüfungen) prüfen | Nicht geprüft. Gerätezugriff fehlt. |
| Debug neben Release | Installation ohne Provider-Kollision und erkennbare Debug-Bezeichnung | Nicht geprüft. Gerätezugriff fehlt. |
| DTS-HD MA | Play-Methode und tatsächlich ausgegebenes Audioformat festhalten | Nicht geprüft. Gerätezugriff fehlt. Audiokette und Testdatei sind noch zu erfassen. |
| DTS:X | Tatsächliche Audioausgabe anhand der vorhandenen Audiokette prüfen | Nicht geprüft. Gerätezugriff fehlt. Audiokette und Testdatei sind noch zu erfassen. |
| Dolby TrueHD | Geeignete Datei im Direct Play prüfen. TrueHD in fMP4-HLS nicht erzwingen | Nicht geprüft. Gerätezugriff fehlt. Audiokette und Testdatei sind noch zu erfassen. |
| Dolby Vision Profil 5 | Testdatei, Bilddarstellung und Play-Methode dokumentieren | Nicht geprüft. Gerätezugriff fehlt. Testdatei ist noch zu erfassen. |
| Dolby Vision Profil 7 | Testdatei, Bilddarstellung und Play-Methode dokumentieren | Nicht geprüft. Gerätezugriff fehlt. Testdatei ist noch zu erfassen. |
| Dolby Vision Profil 8 | Testdatei, Bilddarstellung und Play-Methode dokumentieren | Nicht geprüft. Gerätezugriff fehlt. Testdatei ist noch zu erfassen. |
| HDR10 | Testdatei, HDR-Ausgabe und Play-Methode dokumentieren | Nicht geprüft. Gerätezugriff fehlt. Testdatei ist noch zu erfassen. |
| Seek, Pause/Fortsetzen, Audio-/Untertitelwechsel | Normale Bedienbarkeit ohne neue Regressionen | Nicht geprüft. Gerätezugriff fehlt. |

Die tatsächliche Audiokette und Testdateien zunächst erfassen. Ein DTS-HD-Badge in der Medienansicht allein belegt keinen DTS-HD-Passthrough. Fehlende Hardware, Testdateien oder Zugriff als „nicht geprüft“ markieren. Bei Fehlern zunächst vorhandene Diagnostik und offizielle Einstellungen nutzen; optionale Codeänderungen erfordern einen neuen Auftrag.

## Abnahmekriterien

- [x] Debug-Build, tatsächlicher APK-Pfad, Debug-Identität und Versionswerte sind dokumentiert.
- [ ] Signierter Release-Build, tatsächlicher Release-Pfad, Release-Identität und Versionswerte sind geprüft.
- [ ] Release-Signatur ist geprüft. Der Zertifikatsvergleich ist ausdrücklich offen.
- [x] Diff enthält keine zurückgestellten Wiedergabeänderungen.
- [x] Jede Geräteprüfung ist als bestanden, fehlgeschlagen oder nicht geprüft mit Begründung dokumentiert.
- [x] Debug-APK und Prüfstatus sind separat von der offenen Release- und Geräteabnahme dokumentiert.

## Ergebnis

### Diff-Prüfung

- `HEAD` auf `custom-release-0.20.0` und der lokale Tag `v0.20.0` verweisen auf `45c52925661ff51427cea46ebabcd3de656276cc`. Geprüft wurde der Arbeitsstand auf diesem Branch gegen den Release-Commit.
- Der Diff beschränkt sich auf App-Identität, Branding, den Keystore-Ausschluss und die Fork-Dokumentation. Wiedergabelogik, Abhängigkeiten, Gradle-Wrapper, R8-Minifizierung und Resource Shrinking entsprechen der Release-Basis. `git diff --check` war erfolgreich.
- Die zwölf Branding-Grafiken stimmen per Git-Blob-Hash mit dem bisherigen Fork-Commit `442603b3b` überein. XML-Schlüssel und Formatplatzhalter der drei geänderten Sprachdateien stimmen mit der Release-Basis überein.
- Eine Release-spezifische [Keep-Datei](../app/src/release/res/raw/keep_org_johennifin_androidtv.xml) markiert die unreferenzierte Ressource `@string/app_id` zum Erhalt bei der Ressourcenoptimierung. AAPT2 konnte die Datei kompilieren. Der Wert im fertigen Release-APK ist noch zu prüfen. [Android-Dokumentation zu Keep-Dateien](https://developer.android.com/topic/performance/app-optimization/customize-which-resources-to-keep).

### Build und APK-Prüfung

Temurin JDK 21.0.13, SDK-Plattform `android-37.0`, Build Tools `36.0.0` und die Android SDK Command-Line Tools wurden unter `%TEMP%\johennifin-ticket-006-0.20.0` bereitgestellt. Der unveränderte Wrapper lud Gradle 9.9.0 und startete es unter JDK 21. Die Versions-Property steht in Anführungszeichen, damit PowerShell sie als ein Argument übergibt. Die Projektvorgaben stehen in [`.tools-versions`](../.tools-versions), [`gradle/libs.versions.toml`](../gradle/libs.versions.toml) und den [Wrapper-Properties](../gradle/wrapper/gradle-wrapper.properties).

```powershell
.\gradlew.bat --console=plain '-Pjellyfin.version=0.20.0' :app:assembleDebug
```

Der Debug-Build meldete `BUILD SUCCESSFUL`. Das erzeugte APK liegt unter [`app/build/outputs/apk/debug/jellyfin-androidtv-v0.20.0-debug.apk`](../app/build/outputs/apk/debug/jellyfin-androidtv-v0.20.0-debug.apk). Seine SHA-256-Prüfsumme lautet `B23711F8BEA94883CA319AA948B70C6E08F058CA65BBA2C68942D0828DD6194D`. Für Release gibt es noch keinen APK-Pfad.

| Prüfung am Debug-APK | Ergebnis |
| --- | --- |
| `apkanalyzer apk summary` | App-ID `org.johennifin.androidtv.debug`, `versionName` `0.20.0`, `versionCode` `200099` |
| `aapt2 dump badging` | Label `JoHennifin Debug` für Standard, Deutsch und britisches Englisch |
| `apkanalyzer manifest print` | Vier eindeutige Authorities unter `org.johennifin.androidtv.debug` mit den Suffixen `.androidx-startup`, `.content`, `.integration.provider.ImageProvider` und `.acra` |
| `aapt2 dump resources` | `app_id` ist `org.johennifin.androidtv.debug`, `app_search_suggest_authority` ist `org.johennifin.androidtv.debug.content`, `app_search_suggest_intent_data` ist `content://org.johennifin.androidtv.debug.content/intent` |
| `aapt2 dump xmltree` | `res/xml/searchable.xml` verweist auf die beiden geprüften Suchressourcen und verwendet den Suchpfad `suggestions` |
| `apksigner verify --verbose --print-certs` | Debug-Signatur mit v1 und v2 bestätigt. Der Prüfer meldete Warnungen zu `META-INF`-Einträgen beim JAR-Signaturschema v1. |

### Bestehende Projektchecks

```powershell
.\gradlew.bat --console=plain --continue '-Pjellyfin.version=0.20.0' :app:lintDebug :app:testDebugUnitTest detekt
```

- **Unit-Tests:** 92 bestanden, 0 fehlgeschlagen und 0 übersprungen. [Testbericht](../app/build/reports/tests/testDebugUnitTest/index.html).
- **Android Lint:** 49 Fehler, 312 Warnungen und 418 Hinweise. Alle Fehler liegen in Dateien, die gegenüber `v0.20.0` unverändert sind. Lint meldet `app_id` als unreferenziert. Der Aufruf war wegen `abortOnError = false` im Projekt trotz der Befunde erfolgreich. [Lint-Bericht](../app/build/reports/lint-results-debug.html).
- **Detekt:** 509 Befunde. Die Befunde in bearbeiteten Kotlin- und Gradle-Dateien betreffen vorhandene Bedingungen, Ausdrücke und die Struktur des unverändert langen Geräteberichts. Die Projektkonfiguration setzt `ignoreFailures = true`. [Detekt-Bericht](../build/reports/detekt/detekt.html).

### Release-Signatur und Update-Kompatibilität

Die bisherigen Signierdaten waren im geprüften Checkout und in der lokalen Build-Umgebung nicht verfügbar. Für den signierten Build stehen der bisherige Keystore, Keystore-Passwort, Schlüsselalias und Schlüsselpasswort gemäß [Ticket 004](004-release-signierung.md#lokale-einrichtung) aus. Der signierte Build, der tatsächliche Release-Pfad sowie die APK-Prüfung der Release-Variante bleiben offen.

Der SHA-256-Zertifikatsvergleich und damit die Update-Kompatibilität sind offen. In den geprüften lokalen Verzeichnissen lag keine bisherige Release-APK oder andere verlässliche Referenz vor.
