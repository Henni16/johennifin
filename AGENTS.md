# Hinweise für die Arbeit an JoHennifin

- Die Basis von `custom-release-0.20.0` ist der offizielle Jellyfin-Android-TV-Tag `v0.20.0` auf `45c52925661ff51427cea46ebabcd3de656276cc`. Für spätere Updates vom jeweils verifizierten Release-Tag ausgehen. Der Ablauf steht in [NEW_RELEASE.md](NEW_RELEASE.md).
- JoHennifin verwendet `org.johennifin.androidtv` für Release und `org.johennifin.androidtv.debug` für Debug. Der Code-Namespace ist `org.jellyfin.androidtv`.
- Zielgerät ist die Nvidia Shield TV Pro (2019). Die App läuft auf der Shield, der angeschlossene Sony Bravia A80J (2021) dient als Bildschirm.
- Für 0.20.0 gilt die Nutzerentscheidung, die offizielle Wiedergabelogik zu verwenden. Alte DTS- und HDR-Patches bleiben zurückgestellt. Stream-Fallbacks, zusätzliche Null-Prüfungen, Fehlerweitergabe und zusätzliche Fehlerlogs erfordern einen ausdrücklichen Auftrag für [Ticket 007](tickets/007-optionale-wiedergabe-robustheit.md).
- Vor Änderungen die aktiven Anpassungen in [CUSTOM_DEVIATIONS.md](CUSTOM_DEVIATIONS.md) prüfen und die Datei bei jeder neuen Fork-Abweichung aktualisieren. Die [Ticketübersicht](tickets/README.md) enthält Reihenfolge und Freigabegrenzen. Installation, Push und Veröffentlichung brauchen einen entsprechenden Auftrag.
- Änderungen an diesem Fork auf dem vorgesehenen Release-Branch umsetzen. Bestehende Arbeit und bisherige Release-Branches erhalten.
