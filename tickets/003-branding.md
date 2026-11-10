# 003 – JoHennifin-Branding auf die neue Oberfläche übertragen

Status: Erledigt (10. November 2026)  
Priorität: Pflicht  
Abhängigkeiten: 001; mit 002 abstimmen; zuerst `tickets/README.md` lesen.

## Ziel

App-Name, Grafiken, bisher angepasste Texte und Store-Metadaten tragen wieder das JoHennifin-Branding. Die Änderungen passen zur neuen 0.20-Einstellungsoberfläche.

## Umsetzung

1. Den Branding-Anteil von `442603b3b` als Referenz verwenden. Nur die benötigten Zeichenfolgen übernehmen, nicht komplette alte Übersetzungsdateien über die neuen Dateien kopieren.
2. `app_name_release` auf `JoHennifin` und `app_name_debug` auf `JoHennifin Debug` setzen. Prüfen, dass die Build-Varianten diese Ressourcen verwenden; keine widersprüchliche Schreibweise `Johennifin` im Buildskript hinterlassen.
3. Die vorhandenen eigenen Logo-Vektoren, Launcher-Icons und TV-Banner aus dem alten Custom-Stand übernehmen. Beide Drawable-Varianten und alle fünf Mipmap-Dichten berücksichtigen. Keine neuen Grafiken generieren.
4. Die bisherigen deutschen und englischen UI-Textänderungen auf noch vorhandene beziehungsweise ersetzte String-Keys übertragen. Neue appbezogene Jellyfin-Nennungen ebenfalls prüfen. Verweise auf den tatsächlichen Jellyfin-Server, andere Jellyfin-Clients, Lizenzen und offizielle Dokumentation fachlich korrekt erhalten; keine globale Textersetzung durchführen.
5. Die frühere Versionsanzeige aus `ui/preference/category/about.kt` auf `ui/settings/screen/about/SettingsAboutScreen.kt` übertragen: sichtbare Überschrift und kopierter Versionswert verwenden JoHennifin bzw. `johennifin-androidtv`.
6. Bisherige Branding-Stellen in Serverauswahl, Login, Server-Fallbackname, Telemetrie und Gerätebericht prüfen und passend übertragen. Das Umbenennen vorhandener Berichte ist Branding; keine zusätzlichen Logs oder neue Fehlerbehandlung einbauen.
7. Bestehende Fastlane-Metadaten für `de`, `en-GB` und `en-US` gezielt anpassen. Neue Upstream-Inhalte erhalten; keine Veröffentlichung durchführen.
8. Umfang und bewusste fachliche Ausnahmen in `CUSTOM_DEVIATIONS.md` festhalten.

## Relevante Dateien

- `app/src/main/res/values/strings.xml`, `app/src/main/res/values-de/strings.xml`
- `app/src/main/res/drawable/app_logo.xml`, `app/src/main/res/drawable-v24/app_logo.xml`
- `app/src/main/res/mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/app_icon.png` und `app_banner.png`
- `app/src/main/java/org/jellyfin/androidtv/ui/settings/screen/about/SettingsAboutScreen.kt`
- `app/src/main/java/org/jellyfin/androidtv/ui/startup/fragment/SelectServerFragment.kt` und `UserLoginFragment.kt`
- `app/src/main/java/org/jellyfin/androidtv/auth/repository/ServerRepository.kt`
- `app/src/main/java/org/jellyfin/androidtv/telemetry/TelemetryService.kt`
- `app/src/main/java/org/jellyfin/androidtv/util/profile/deviceProfileReport.kt`
- `fastlane/metadata/android/{de,en-GB,en-US}/`

## Abnahmekriterien

- [x] Namen in Release/Debug, neuer Über-Seite und kopierter Versionsinformation sind konsistent.
- [x] Beide Logo-Vektoren sowie Icons/Banner aller Dichten entsprechen dem vorhandenen Custom-Branding.
- [x] Aktuelle Strings, Platzhalter und XML-Strukturen bleiben gültig; keine Übersetzungen durch alte Dateiversionen verloren.
- [x] Technische Namespaces, SDK-Bezeichner, Lizenztexte und offizielle Links bleiben korrekt.
- [x] Metadaten der drei bisher angepassten Sprachen sind aktualisiert.
- [x] Keine eigenen Wiedergabeänderungen und keine zusätzlichen Fehlerlogs.
- [x] Sichtprüfung der App an Ticket 006 übergeben; eventuell bereits durchgeführte Vorschauen dokumentiert.

## Ergebnis

- `app/src/main/res` enthält die Labels `JoHennifin` und `JoHennifin Debug`, gezielte Änderungen an deutschen und englischen Strings einschließlich der britischen Variante sowie die zwölf übernommenen Grafikdateien. Die vorhandenen Build- und Manifest-Verweise auf die Label-, Icon- und Banner-Ressourcen passen zu beiden Varianten.
- `SettingsAboutScreen.kt` zeigt „JoHennifin app version“ und kopiert den angezeigten Versionswert `johennifin-androidtv <Version> <Build-Typ>`. `SelectServerFragment.kt` zeigt denselben Wert. `UserLoginFragment.kt` zeigt den App-Namen, solange kein Server vorliegt, und verwendet sonst den tatsächlichen Servernamen. Der Fallback in `ServerRepository.kt` lautet weiterhin „Jellyfin Server“, weil er den Server bezeichnet.
- `TelemetryService.kt` und `deviceProfileReport.kt` nennen JoHennifin in den bestehenden Berichten. Titel, Kurz- und Langbeschreibung sowie Standard-Änderungsprotokoll unter `fastlane/metadata/android/{de,en-GB,en-US}/` sind angepasst. [CUSTOM_DEVIATIONS.md](../CUSTOM_DEVIATIONS.md#branding) dokumentiert die fachlichen Ausnahmen.

### Prüfungen

- Git-Blob-Hashes aller zwölf Grafiken stimmen mit `442603b3b` überein. Die beiden Vektordateien sind gültiges XML. Launcher-Icon und TV-Banner in `xhdpi` wurden als PNG-Vorschau gesichtet.
- XML-Vergleich mit `v0.20.0` bestätigt, dass nur die ausgewählten String-Werte geändert wurden. Struktur, Schlüssel und Formatplatzhalter sind erhalten. Labels, Manifest-Verweise, Versionsanzeige und Kopierwert wurden im Quellcode geprüft.
- Die drei Fastlane-Sprachen enthalten das Fork-Branding und weiterhin den Jellyfin-Serververweis. Die Funktionslisten entsprechen der Release-Basis. Diff- und Leerraumprüfung sind bestanden.
- Build, Launcher-Vorschau und Sichtprüfung der laufenden App folgen in [Ticket 006](006-build-und-abnahme.md#geräteabnahme).
