# 007 – Optional: Wiedergabe-Robustheit und zusätzliche Fehlerdiagnose

Status: Zurückgestellt – nicht Teil des geplanten Releases  
Priorität: Optional  
Abhängigkeiten: Neuer ausdrücklicher Nutzerauftrag; zuerst `tickets/README.md` lesen.

## Nutzerentscheidung

Für JoHennifin 0.20.0 wird zunächst angenommen, dass die offizielle Wiedergabelogik funktioniert. Dieses Ticket ist ein Merkposten und darf nicht automatisch mit den Pflicht-Tickets umgesetzt werden.

## Möglicher Auftrag nach Aktivierung

Die noch nicht übernommenen Robustheits- und Diagnoseänderungen aus `442603b3b` anhand eines konkreten Fehlerbilds prüfen. Nur den dafür nötigen Teil auf die aktuelle Architektur portieren.

| Bereich | Früherer Custom-Patch | Relevante Dateien |
| --- | --- | --- |
| Fehlende Stream-URLs | Direct Play, Direct Stream und Transcoding nur mit verwendbarer URL wählen; bei fehlender URL nächste erlaubte Möglichkeit versuchen | `app/src/main/java/org/jellyfin/androidtv/ui/playback/PlaybackManager.kt` |
| Null-Prüfungen | `playMethod` null-sicher behandeln und null-sicheren Enum-Vergleich verwenden | `app/src/main/java/org/jellyfin/androidtv/ui/playback/PlaybackController.java` |
| Fehlercodes | Ursprüngliche Exception erhalten, statt sie immer in `Exception` einzupacken; `PlaybackException` mit Fehlercode und Meldung erzeugen | `PlaybackManager.kt`, `app/src/main/java/org/jellyfin/androidtv/data/compat/PlaybackException.kt` |
| Fehlerweitergabe | Fehlgeschlagene Stream-Abfrage in die zentrale Fehlerbehandlung leiten; Player-Exception durch den Callback weitergeben | `PlaybackController.java`, `PlaybackControllerNotifiable.kt`, `VideoManager.java` im selben Playback-Verzeichnis |
| Zusätzliche Diagnose | Player-Exception mit Ursache loggen, fehlende Medien-URL mit Kontext melden; Fehler im neuen Backend loggen | Genannte Dateien und `playback/media3/exoplayer/src/main/kotlin/ExoPlayerBackend.kt` |

## Vorgehen nach Aktivierung

1. Reproduktion, verwendeten Player, Datei/Codec, Serverantwort und vorhandene Logs erfassen. Keine unnötigen Änderungen ohne belegtes Problem.
2. Gegen den dann aktuellen Release prüfen, ob der Fehler inzwischen upstream behoben ist.
3. Kleine, gezielte Änderungen schreiben. Vorhandene neue Architektur und korrekte Coroutine-Abbruchbehandlung erhalten; alte Dateien nicht komplett zurückkopieren.
4. Fehlercodes und fehlende URLs mit sinnvollen Tests abdecken, soweit der betroffene Pfad geändert wird. Playback-Retry/Fallback darf keine Schleife erzeugen und keine serverseitigen Berechtigungen umgehen.
5. Logs ohne Tokens, Zugangsdaten oder vollständige authentifizierte Stream-URLs halten. Technische Diagnosedetails nicht ungefragt als neue Benutzer-Toasts ausgeben.
6. Aktivierte Teilbereiche und Prüfergebnisse in `CUSTOM_DEVIATIONS.md` dokumentieren.

## Abnahmekriterien nach Aktivierung

- [ ] Ein konkretes Fehlerbild und der freigegebene Teilumfang sind benannt.
- [ ] Nur erforderliche Robustheits-/Diagnoseänderungen wurden umgesetzt.
- [ ] Geänderte Fehler- und Erfolgswege sind geprüft; normale Wiedergabe bleibt funktionsfähig.
- [ ] Keine Geheimnisse in Logs; keine Endlosschleifen oder unkontrollierten Retries.
- [ ] Dokumentation beschreibt nur tatsächlich aktivierte Teile dieses Tickets.

## Ergebnis

Bewusst zurückgestellt. Keine Umsetzung beauftragt.
