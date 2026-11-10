# 005 – Fork-, Upgrade- und Installationsdokumentation aktualisieren

Status: Erledigt (10. November 2026)  
Priorität: Pflicht  
Abhängigkeiten: 002–004; zuerst `tickets/README.md` lesen.

## Ziel

Die Dokumentation beschreibt den tatsächlich umgesetzten 0.20.0-Fork und verhindert, dass spätere Agents erledigte oder bewusst zurückgestellte Wiedergabe-Patches erneut einbauen.

## Umsetzung

1. Die bisherigen Dokumente aus `origin/custom-release-0.19.9` als Ausgangspunkt lesen. Neu angelegte Abschnitte aus anderen Tickets erhalten und zusammenführen.
2. `AGENTS.md` aktualisieren: Release-Basis, Zielgerät, eigene App-ID und Pflicht zur Pflege der Abweichungsdokumentation festhalten. Die aktuelle Nutzerentscheidung zu unveränderter offizieller Wiedergabelogik ausdrücklich aufnehmen.
3. `CUSTOM_DEVIATIONS.md` auf den finalen Stand bringen: aktive Anpassungen von entfallenen Workarounds und optionalen Vorhaben unterscheiden. Aktive Themen sind App-Identität, Branding und lokale Signierkonfiguration. Keine schon erledigten Wiedergabetests behaupten.
4. Als durch Upstream abgedeckt beziehungsweise zunächst entfallen dokumentieren: alte Abhängigkeits-Pins/API-Anhebung, eigene HLS-Profilaufteilung, DTS-Profilmapping sowie pauschale Dolby-Vision-/HDR-Annahmen. DTS-Mapping fehlt zwar als identischer Patch, liegt aber im untersuchten Stand in einem Support-Prüfpfad ohne gefundene Aufrufstelle; tatsächliche Wiedergabe wird in Ticket 006 geprüft.
5. TrueHD im fMP4-HLS-Profil ausdrücklich nicht wieder aktivieren. Alte HEVC-Level-Absenkung, Main10-zu-HDR10-Annahme und 1080p-Fallback nur bei neu belegtem Bedarf erneut diskutieren. Keine automatische Profil-5-/Profil-8-Trennung behaupten: Der alte Custom-Code leitete beide aus derselben allgemeinen Dolby-Vision-Prüfung ab.
6. `NEW_RELEASE.md` für künftige Updates korrigieren: neuen Branch vom verifizierten Release-Tag erstellen, eigene Änderungen anhand der Dokumentation selektiv portieren und prüfen. Den alten Rebase-Befehl nicht ungeprüft übernehmen; er kann den alten Custom-Branch umschreiben.
7. `INSTALL_TO_SHIELD.md` auf PowerShell, Version `0.20.0`, App-ID `org.johennifin.androidtv`, neuen Signiermechanismus und tatsächlich erzeugten APK-Pfad umstellen. ADB-Ziel als Platzhalter, keine erfundene IP. Update-Installation mit `adb install -r` beschreiben; keinen Datenverlust durch Deinstallation empfehlen.
8. Zusätzliche Fehlerlogs, Stream-Fallbacks, Null-Prüfungen und Fehlerweitergabe als zurückgestellt mit Verweis auf Ticket 007 nennen.

## Abnahmekriterien

- [x] `AGENTS.md`, `CUSTOM_DEVIATIONS.md`, `NEW_RELEASE.md` und `INSTALL_TO_SHIELD.md` entsprechen dem neuen Stand.
- [x] Beispiele verwenden PowerShell-kompatible Syntax und die richtige App-ID/Version.
- [x] Upstream-Signier-Properties sind dokumentiert, Geheimnisse ausschließlich als Platzhalter.
- [x] Keine veralteten Anweisungen erzwingen die Wiedereinführung alter Wiedergabe-Patches.
- [x] Offene Gerätetests und optionale Arbeiten sind klar von fertigen Funktionen getrennt.

## Ergebnis

- [AGENTS.md](../AGENTS.md) hält Release-Basis, App-IDs, Zielgerät und die Entscheidung für die offizielle Wiedergabelogik fest. [CUSTOM_DEVIATIONS.md](../CUSTOM_DEVIATIONS.md) führt die Ergebnisse aus 002 bis 004 fort und trennt aktive Anpassungen von Upstream-Funktionen, entfallenen Patches und den optionalen Arbeiten aus 007.
- [NEW_RELEASE.md](../NEW_RELEASE.md) beschreibt den neuen Branch vom geprüften Tag und die gezielte Übernahme der aktiven Änderungen. [INSTALL_TO_SHIELD.md](../INSTALL_TO_SHIELD.md) beschreibt die lokale Signierung, die Ermittlung des erzeugten APK-Pfads, die Prüfung von Version, App-ID und Zertifikat sowie das Update per `adb install -r`.
- Alle neun PowerShell-Beispiele wurden syntaktisch geprüft. Lokale Links und Zielüberschriften, Release-Tag, App-ID, Versionswerte und die Signier-Properties wurden abgeglichen. Der Diff gegen `v0.20.0` zeigt keine Änderungen an Wiedergabelogik oder Abhängigkeiten.
- Der tatsächliche Pfad eines signierten APKs, der Zertifikatsvergleich und die Geräteabnahme werden in [Ticket 006](006-build-und-abnahme.md) eingetragen. Die Anleitung ermittelt den Pfad nach erfolgreichem Build. Für dieses Dokumentationsticket wurden Build und Installation nicht ausgeführt.
