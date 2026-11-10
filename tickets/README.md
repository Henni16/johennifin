# JoHennifin 0.20.0 – Arbeitsaufträge

Stand: 10. November 2026. Diese Tickets beschreiben die Umsetzung; erledigte Arbeiten und Prüfergebnisse sind im jeweiligen Ticket dokumentiert.

## Gemeinsamer Kontext für alle Agents

- Ziel: JoHennifin auf Basis des offiziellen Jellyfin Android TV **0.20.0**, mit eigener App-Identität und Branding.
- Geprüfte Release-Basis: `45c52925661ff51427cea46ebabcd3de656276cc` (`v0.20.0`, zum Prüfzeitpunkt auch `release-0.20.z`). Nicht einfach den jeweils neuesten `master` als Release-Basis verwenden.
- Quelle der bisherigen Anpassungen: `origin/custom-release-0.19.9`, Commit `442603b3b073e0615554e543e4ee9c248c08204a`. Der Diff dieses Commits gegen seinen Parent isoliert die eigenen Änderungen.
- Zielgerät: Nvidia Shield TV Pro (2019), angeschlossen an Sony Bravia A80J (2021). Die App läuft auf der Shield.
- Aktuelle Nutzerentscheidung: zunächst die offizielle Wiedergabelogik verwenden. Keine eigenen Stream-Fallbacks, Null-Prüfungen, Fehlerweitergabe oder zusätzlichen Fehlerlogs einbauen. Diese Arbeiten stehen ausschließlich im optionalen Ticket 007.
- Alte DTS-/HDR-Patches nicht pauschal übernehmen. Media3 1.11.1, Jellyfin-Media 1.9.0+1, API 23, getrennte HLS-Profile, Passthrough-Erkennung, HDR-Overrides und der Shield-Profil-7-Fix sind bereits Bestandteil der Basis.
- Insbesondere TrueHD nicht erneut im fMP4-HLS-Profil aktivieren. Upstream hat diese Deklaration wegen Wiedergabeproblemen entfernt.
- Der alte Custom-Commit enthält mehrere Themen. Kein vollständiger Cherry-pick; nur die im jeweiligen Ticket beschriebenen Änderungen übertragen.
- Vor Arbeit vorhandene `AGENTS.md`, Arbeitsverzeichnis und Branch prüfen. Andere Änderungen erhalten. Fehlende Zugangsdaten oder Geräte als offene Prüfung benennen; keinen erfolgreichen Test behaupten.
- Diese Tickets autorisieren keine automatische Veröffentlichung, keinen Push und keine Installation auf der Shield. Solche Schritte nur ausführen, wenn sie im ausführenden Chat zusätzlich beauftragt sind.
- Jeder Agent liest diese Übersicht und sein Ticket, aktualisiert dessen Status und liefert geänderte Dateien, Prüfergebnisse sowie offene Punkte zurück. Neue, tatsächlich umgesetzte Abweichungen gehören in `CUSTOM_DEVIATIONS.md`; Ticket 005 konsolidiert die Dokumentation.

## Tickets und Reihenfolge

| Ticket | Auftrag | Abhängigkeit | Status |
| --- | --- | --- | --- |
| [001](001-release-basis-und-version.md) | Release-Branch und Version vorbereiten | Keine | Erledigt |
| [002](002-app-identitaet.md) | Eigene App-ID und Provider-Adressen | 001 | Erledigt |
| [003](003-branding.md) | Name, Bilder, Texte und Metadaten | 001, mit 002 abstimmen | Erledigt |
| [004](004-release-signierung.md) | Vorhandenen Schlüssel mit Upstream-Signierung nutzen | 001 und 002 | Vorbereitet, Signierdaten fehlen |
| [005](005-fork-dokumentation.md) | Fork-, Upgrade- und Installationsdokumentation | 002–004 | Erledigt |
| [006](006-build-und-abnahme.md) | Build und Abnahme des neuen Releases | 002–005 | Debug-APK geprüft, Release und Shield-Abnahme offen |
| [007](007-optionale-wiedergabe-robustheit.md) | Optional: Fehlerdiagnose und Wiedergabe-Robustheit | Neuer ausdrücklicher Auftrag | Zurückgestellt |

001 zuerst erledigen. 002 und 004 ändern beide `app/build.gradle.kts` und sollten nacheinander integriert werden. 003 kann danach separat bearbeitet werden; in einem gemeinsam genutzten Checkout darf kein Agent während anderer Arbeiten den Branch wechseln. 005 konsolidiert den finalen Stand, 006 prüft das Ergebnis. 007 gehört nicht zum geplanten Release-Umfang.

## Referenzen

- [Offizieller Release 0.20.0](https://github.com/jellyfin/jellyfin-androidtv/releases/tag/v0.20.0)
- [Audio-Passthrough-Erkennung und Overrides](https://github.com/jellyfin/jellyfin-androidtv/pull/5645)
- [Shield: Dolby Vision Profile 7](https://github.com/jellyfin/jellyfin-androidtv/pull/5652)
- [TrueHD aus fMP4-HLS entfernt](https://github.com/jellyfin/jellyfin-androidtv/pull/5821)
- [Media3-Änderungsprotokoll](https://developer.android.com/jetpack/androidx/releases/media3)
