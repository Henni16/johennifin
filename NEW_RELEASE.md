# JoHennifin auf einen neuen Release-Stand bringen

## 1. Offiziellen Tag prüfen

Im Repository-Stamm den Arbeitsstand mit `git status --short --branch` prüfen und vorhandene Änderungen im bisherigen Checkout erhalten. Für einen Branchwechsel einen sauberen Checkout verwenden. `upstream` muss auf das offizielle Repository `https://github.com/jellyfin/jellyfin-androidtv.git` zeigen.

Vor dem Abruf Version und Commit aus dem offiziellen Release bestimmen. Die folgenden Werte zeigen die bereits geprüfte 0.20.0-Basis. Für den nächsten Release beide Werte ersetzen.

```powershell
$version = '0.20.0'
$expectedCommit = '45c52925661ff51427cea46ebabcd3de656276cc'
$tag = "v$version"
$branch = "custom-release-$version"

if ((git remote get-url upstream) -cne 'https://github.com/jellyfin/jellyfin-androidtv.git') {
    throw 'Der Upstream-Remote stimmt nicht mit dem offiziellen Repository überein.'
}
git fetch --no-tags upstream "refs/tags/${tag}:refs/tags/${tag}"
if ($LASTEXITCODE -ne 0) { throw "Abruf von $tag fehlgeschlagen." }

$actualCommit = git rev-parse "refs/tags/${tag}^{commit}"
if ($LASTEXITCODE -ne 0 -or $actualCommit -ne $expectedCommit) {
    throw "Commit des Tags $tag stimmt nicht mit der geprüften Basis überein."
}
```

## 2. Release-Branch anlegen und Änderungen übertragen

Bei einem neuen Branch auf den geprüften Commit wechseln.

```powershell
git switch --create $branch $actualCommit
```

Existiert der Branch bereits, zuerst mit `git merge-base --is-ancestor $actualCommit $branch` seine Basis prüfen und die Arbeit dort fortsetzen. Der bisherige Custom-Branch dient als Vergleichsquelle und behält seine Historie.

Die aktiven Änderungen aus [CUSTOM_DEVIATIONS.md](CUSTOM_DEVIATIONS.md) einzeln prüfen und auf die neue Architektur übertragen. Für den Übergang auf 0.20.0 zeigt `git show 442603b3b` die Änderungen des früheren Forks. Der Commit enthält alte Wiedergabe-Patches, daher nur die für App-ID, Branding und lokale Signierung benötigten Änderungen übernehmen. Anschließend die Abweichungsdokumentation aktualisieren.

## 3. Bauen und abnehmen

JDK, Android SDK und Gradle-Wrapper gegen die neue Release-Basis prüfen. Die Versions-Property explizit setzen und für Release den bisherigen Keystore über die vorhandene Signierlogik konfigurieren. Die Schritte stehen in der [Shield-Anleitung](INSTALL_TO_SHIELD.md).

```powershell
.\gradlew.bat "-Pjellyfin.version=$version" :app:assembleDebug :app:assembleRelease
```

Für 0.20.0 stehen der erwartete Versionscode, die APK-Prüfungen und die Geräteabnahme in [Ticket 006](tickets/006-build-und-abnahme.md). Den tatsächlichen APK-Pfad, den Zertifikatsvergleich und den Status der Geräteprüfungen dort dokumentieren. Push und Veröffentlichung erfolgen auf gesonderten Auftrag.
