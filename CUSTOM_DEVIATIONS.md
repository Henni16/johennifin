# Abweichungen von Jellyfin Android TV 0.20.0

Die aktiven Fork-Anpassungen basieren auf dem offiziellen Tag `v0.20.0` (`45c52925661ff51427cea46ebabcd3de656276cc`). Die Wiedergabe verwendet den Stand dieses Releases.

## Eigene App-ID

JoHennifin verwendet für Release `org.johennifin.androidtv` und für Debug `org.johennifin.androidtv.debug`. Die Release-ID entspricht der bisherigen JoHennifin-App. Für ein Update der vorhandenen Installation ist zusätzlich derselbe Signaturschlüssel erforderlich.

[`app/build.gradle.kts`](app/build.gradle.kts) erzeugt `app_id`, `app_search_suggest_authority` und `app_search_suggest_intent_data` aus der finalen App-ID der Variante. Die Such-Authority lautet `<App-ID>.content`, die Intent-URI `content://<App-ID>.content/intent`. Die Provider im Manifest verwenden `${applicationId}`. Der Code-Namespace bleibt `org.jellyfin.androidtv`.

[`keep_org_johennifin_androidtv.xml`](app/src/release/res/raw/keep_org_johennifin_androidtv.xml) erhält `@string/app_id` bei der Ressourcenoptimierung im Release. Die App referenziert die Ressource zur Laufzeit nicht. Die APK-Abnahme prüft ihren Wert nach dem Packaging.

## Branding

- Release und Debug beziehen ihre Labels `JoHennifin` und `JoHennifin Debug` aus den String-Ressourcen. Beide Logo-Vektoren sowie Launcher-Icons und TV-Banner aller fünf Dichten stammen aus `442603b3b`.
- Die deutschen und englischen UI-Texte einschließlich `en-GB` nennen die TV-App JoHennifin. Serverauswahl und Über-Seite zeigen `johennifin-androidtv <Version> <Build-Typ>`. Die Über-Seite kopiert diesen Wert. Im Login erscheint der App-Name, solange kein Server vorliegt.
- Absturz- und Geräteberichte tragen den Client-Namen `JoHennifin for Android TV`. Die Fastlane-Texte für `de`, `en-GB` und `en-US` beschreiben die App als Fork und nennen Jellyfin als benötigten Server.
- Jellyfin bleibt die Bezeichnung für den Server, dessen Fallbackname `Jellyfin Server` und andere Clients, etwa beim Quick Connect. Die technische SDK-Clientkennung für normale API-Aufrufe, der Schlüssel für serverseitige Anzeigeeinstellungen, die Upstream-Links und die Lizenzangaben bleiben erhalten.

## Release-Signierung

Der Fork verwendet die vorhandene Upstream-Signierung. [Ticket 004](tickets/004-release-signierung.md#lokale-einrichtung) dokumentiert die Übernahme der bisherigen Signierdaten in die vier Upstream-Properties oder ihre Umgebungsvariablen. `.gitignore` schließt `jellyfin-release-key.jks` aus. Für ein Update wird der bisherige JoHennifin-Release-Keystore benötigt. Die lokale Einrichtung und der Nachweis der passenden Signatur stehen aus.

## Wiedergabe im offiziellen Release

- 0.20.0 bringt Media3 1.11.1, Jellyfin-Media 1.9.0+1 und Mindest-API 23 mit. Die alten Versions-Pins und die eigene API-Anhebung entfallen.
- Upstream trennt die HLS-Profile für MPEG-TS und fMP4 und bringt automatische Audio-Passthrough-Erkennung, Audio- und HDR-Overrides sowie den Shield-Fix für Dolby Vision Profil 7 mit.
- TrueHD bleibt im fMP4-HLS-Profil deaktiviert. Upstream entfernte die Deklaration in `d1877e73d` wegen Wiedergabeproblemen mit fragmentiertem MP4.
- Das frühere DTS-Profilmapping für DTS:X und DTS-HD fehlt als identischer Patch. Im untersuchten Stand führt der Formatkonverter zum Supportbericht `supportsStream`; die Quellcodesuche fand dafür keine aufrufende Stelle. Tatsächliche DTS-Wiedergabe und Passthrough sind Teil der Geräteabnahme.
- Die früheren pauschalen Dolby-Vision- und HDR-Annahmen wurden nicht übernommen. Die alte Funktion für Profil 8 verwendete dieselbe allgemeine Dolby-Vision-Prüfung wie Profil 5. Die HEVC-Level-Absenkung, die Main10-als-HDR10-Annahme und der 1080p-Fallback bleiben zurückgestellt, bis ein konkretes Fehlerbild ihren Bedarf belegt.

## Offene Prüfungen und optionale Arbeiten

- [Ticket 006](tickets/006-build-und-abnahme.md) erfasst die APKs, die Release-Signatur, die Update-Kompatibilität und die Geräteprüfung auf der Nvidia Shield TV Pro (2019) am Sony Bravia A80J (2021). Wiedergabe, Passthrough und HDR-Ausgabe sind auf dem Gerät noch nicht geprüft.
- Stream-Fallbacks, zusätzliche Null-Prüfungen, Fehlerweitergabe und zusätzliche Fehlerlogs sind in [Ticket 007](tickets/007-optionale-wiedergabe-robustheit.md) zurückgestellt. Die Umsetzung erfordert einen ausdrücklichen Auftrag.
