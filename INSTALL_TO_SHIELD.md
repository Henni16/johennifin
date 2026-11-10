# JoHennifin 0.20.0 auf der Nvidia Shield aktualisieren

Diese Schritte gelten für die vorhandene JoHennifin-Installation mit der App-ID `org.johennifin.androidtv`. Ein signiertes Release-APK und der Abgleich mit der bisherigen Signatur stehen in [Ticket 006](tickets/006-build-und-abnahme.md) aus.

## macOS

Führe die Schritte der Reihe nach im selben Terminal aus. So bleiben die gesetzten Pfade und die Adresse der Shield für die nächsten Befehle verfügbar. Bei einem Fehler den jeweiligen Schritt korrigieren, bevor du weitergehst.

### 1. Release-Branch holen

Öffne den vorhandenen JoHennifin-Checkout auf dem MacBook im Terminal. `origin` soll auf `Henni16/johennifin` zeigen. Wenn `git status --short --branch` lokale Änderungen anzeigt, sichere diese vor dem Branchwechsel. Deine ignorierte `local.properties` enthält gegebenenfalls die bisherigen Signierdaten und wird für Schritt 4 gebraucht.

```sh
git status --short --branch
git remote get-url origin
git fetch origin
```

Beim ersten Wechsel auf diesen Release-Branch:

```sh
git switch --track origin/custom-release-0.20.0
```

Wenn der Branch bereits lokal existiert:

```sh
git switch custom-release-0.20.0 && git pull --ff-only origin custom-release-0.20.0
```

### 2. JDK und Android SDK prüfen

Der Release braucht JDK 21 und Android SDK für Compile SDK 37. Die Befehle verwenden den üblichen SDK-Pfad auf dem Mac. Falls dein SDK woanders liegt, setze `ANDROID_HOME` auf den SDK-Pfad aus Android Studio oder auf den Wert von `sdk.dir` in `local.properties`.

```sh
JAVA_HOME="$(/usr/libexec/java_home -v 21)" && export JAVA_HOME
"$JAVA_HOME/bin/java" -version

export ANDROID_HOME="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Library/Android/sdk}}"
adb="$ANDROID_HOME/platform-tools/adb"
apkanalyzer="$ANDROID_HOME/cmdline-tools/latest/bin/apkanalyzer"
apksigner="$ANDROID_HOME/build-tools/36.0.0/apksigner"
ls "$ANDROID_HOME/platforms"
ls -l "$adb" "$apkanalyzer" "$apksigner"
```

Wenn `java_home` kein JDK 21 findet, installiere eines oder setze `JAVA_HOME` auf den bereits vorhandenen JDK-21-Pfad. Fehlende SDK-Pakete kannst du in Android Studio unter **Tools → SDK Manager** ergänzen. Unter **SDK Platforms** brauchst du API 37, unter **SDK Tools** die Platform-Tools, die Command-line Tools und die Build-Tools. Wenn `apkanalyzer` oder `apksigner` bereits in einem anderen Versionsordner installiert ist, passe den jeweiligen Pfad an. [Android SDK Manager](https://developer.android.com/studio/intro/update#sdk-manager)

### 3. Installierte Version und Referenz-APK von der Shield holen

Schalte in den Entwickleroptionen der Shield das Netzwerk-Debugging ein. Mac und Shield müssen im selben Netzwerk sein. Ersetze `<IP-Adresse>:<Port>` durch die Adresse, die die Shield anzeigt, und bestätige die ADB-Anfrage am Fernseher.

```sh
shield='<IP-Adresse>:<Port>'
app_id='org.johennifin.androidtv'
"$adb" connect "$shield"
"$adb" devices
```

Sobald der Eintrag für die Shield `device` anzeigt, lies die installierte Version aus.

```sh
"$adb" -s "$shield" shell dumpsys package "$app_id" | grep -E 'versionCode=|versionName='
```

**Nur weitergehen, wenn der installierte `versionCode` sichtbar und höchstens `200099` ist.** Die frühere Anleitung nannte `1.0.0` als Build-Beispiel. Falls diese Version installiert ist, steht dort `1000099` und das Update auf `200099` wird abgelehnt. In diesem Fall die vorhandene App auf der Shield belassen und den angezeigten Versionscode zurückmelden. [Android-Dokumentation zu App-Updates](https://developer.android.com/google/play/app-updates#how-android-handles-app-updates)

Hole jetzt die Basis-APK der installierten App als Referenz für den Zertifikatsvergleich. `adb pull` kopiert sie in ein temporäres Verzeichnis auf dem Mac.

```sh
installed_apk=$("$adb" -s "$shield" shell pm path "$app_id" | tr -d '\r' |
    sed -n 's|^package:\(.*/base\.apk\)$|\1|p')
reference_dir=$(mktemp -d "${TMPDIR:-/tmp}/johennifin-shield.XXXXXX")
previous_apk="$reference_dir/installed-base.apk"
test -n "$installed_apk" && test -n "$reference_dir" &&
    "$adb" -s "$shield" pull "$installed_apk" "$previous_apk"
```

Wenn keine APK kopiert wurde, prüfe die Ausgabe von `"$adb" -s "$shield" shell pm path "$app_id"`. Eine vorhandene, verlässlich der installierten Version zugeordnete Release-APK kannst du alternativ mit `previous_apk='/absoluter/Pfad/zur/bisherigen.apk'` als Referenz angeben. [ADB-Dateiübertragung](https://developer.android.com/tools/adb#copyfiles)

### 4. Vorhandene Signierdaten einmalig übernehmen

Der neue Branch liest die Signierdaten aus der persönlichen Gradle-Konfiguration. Öffne die Datei außerhalb des Repos. Wenn sie bereits existiert, ergänze oder aktualisiere die vier Einträge und erhalte die übrigen Einstellungen.

```sh
gradle_user_dir="${GRADLE_USER_HOME:-$HOME/.gradle}"
mkdir -p "$gradle_user_dir" &&
    (umask 077; touch "$gradle_user_dir/gradle.properties") &&
    chmod 600 "$gradle_user_dir/gradle.properties" &&
    nano "$gradle_user_dir/gradle.properties"
```

Übernimm die Werte aus der bisherigen `local.properties` nach dieser Zuordnung. Für `keystore.file` verwende den absoluten Pfad zu deinem bisherigen Keystore, etwa `/Users/deinname/Schluessel/jellyfin-release-key.jks`. Wenn `signing.storeFile` bisher relativ war, bezog sich der Pfad auf das `app/`-Verzeichnis des bisherigen Checkouts. Verwende denselben Schlüssel und speichere ihn außerhalb des Repos. Trage Passwörter nur im lokalen Editor ein. [Ticket 004](tickets/004-release-signierung.md#lokale-einrichtung)

| Bisher in `local.properties` | In der persönlichen `gradle.properties` |
| --- | --- |
| `signing.storeFile` | `keystore.file` |
| `signing.storePassword` | `keystore.password` |
| `signing.keyAlias` | `signing.key.alias` |
| `signing.keyPassword` | `signing.key.password` |

### 5. Signierten Release bauen

```sh
./gradlew --console=plain -Pjellyfin.version=0.20.0 :app:assembleRelease &&
    find app/build/outputs/apk/release -type f -name '*.apk' -print
```

Nach `BUILD SUCCESSFUL` den Pfad der erzeugten Release-APK aus der Ausgabe übernehmen. Der Pfad im nächsten Beispiel ist der erwartete Dateiname. Falls die Ausgabe abweicht, verwende den tatsächlich erzeugten Pfad.

### 6. Neue APK und Signatur prüfen

```sh
apk='app/build/outputs/apk/release/jellyfin-androidtv-v0.20.0-release.apk'
"$apkanalyzer" apk summary "$apk" &&
    "$apksigner" verify --print-certs "$apk" &&
    "$apksigner" verify --print-certs "$previous_apk"
```

Die erste Zeile muss `org.johennifin.androidtv 200099 0.20.0` lauten. Beide Signaturprüfungen müssen erfolgreich sein und die Zeile `Signer #1 certificate SHA-256 digest` muss für beide APKs denselben Fingerabdruck zeigen. Bei mehreren Signern müssen alle Fingerabdrücke übereinstimmen. Bei einer Abweichung hier stoppen und die vorhandene App auf der Shield belassen. [APK Analyzer](https://developer.android.com/tools/apkanalyzer#commands) · [apksigner](https://developer.android.com/tools/apksigner#verify-options)

Für die Release-Abnahme in Ticket 006 zusätzlich die App-Labels, Provider-Authorities und Suchressourcen prüfen. Das Standardlabel soll `JoHennifin` sein, die Authorities sollen mit `org.johennifin.androidtv.` beginnen und die Suchressourcen müssen den Werten für Release entsprechen. [Sollwerte aus Ticket 002](tickets/002-app-identitaet.md#statische-prüfung)

```sh
aapt2="$(dirname "$apksigner")/aapt2"
"$aapt2" dump badging "$apk" | grep '^application-label'
"$apkanalyzer" manifest print "$apk" | grep 'android:authorities='
"$aapt2" dump resources "$apk" | grep -A 2 -E 'string/(app_id|app_search_suggest_authority|app_search_suggest_intent_data)([[:space:]]|$)'
```

### 7. Bestehende App aktualisieren

Führe den Installationsbefehl erst nach den Versions- und Signaturprüfungen aus. `-r` erhält die App-Daten der vorhandenen Installation.

```sh
"$adb" -s "$shield" install -r "$apk"
```

Nach `Success` JoHennifin über den Launcher öffnen. Unter **Einstellungen → Über** soll `johennifin-androidtv 0.20.0 release` erscheinen. Prüfe, ob Anmeldung und Einstellungen erhalten sind. Halte den tatsächlichen APK-Pfad, das Ergebnis des Signaturvergleichs und die Geräteprüfung in [Ticket 006](tickets/006-build-und-abnahme.md) fest. [ADB-Installationsoptionen](https://developer.android.com/tools/adb#pm)

```sh
"$adb" disconnect "$shield"
```

## Windows (PowerShell)

### 1. Release lokal signieren und bauen

Im Repository-Stamm mit JDK 21, Android SDK für Compile SDK 37 und dem mitgelieferten Gradle-Wrapper arbeiten. Für die Prüfung und Installation müssen `apkanalyzer`, `apksigner` und `adb` aus dem Android SDK in PowerShell verfügbar sein.

Den bisherigen Release-Keystore außerhalb des Checkouts ablegen. Die vier Werte in der persönlichen `gradle.properties` unter `GRADLE_USER_HOME` setzen. Ohne eigene Einstellung liegt sie unter `$env:USERPROFILE\.gradle\gradle.properties`.

```properties
keystore.file=C:/Pfad/zum/bisherigen/jellyfin-release-key.jks
keystore.password=<bisheriges Keystore-Passwort>
signing.key.alias=<bisheriger Alias>
signing.key.password=<bisheriges Schlüsselpasswort>
```

Für `keystore.file` einen geprüften absoluten Pfad mit Schrägstrichen verwenden. Relative Pfade beziehen sich auf `app/`. Die alten `signing.*`-Werte aus `local.properties` nach der Zuordnung in [Ticket 004](tickets/004-release-signierung.md#ausgangslage-und-umsetzung) übernehmen. Alternativ die dort aufgeführten Umgebungsvariablen setzen. Gradle-Properties haben Vorrang.

```powershell
.\gradlew.bat '-Pjellyfin.version=0.20.0' :app:assembleRelease
if ($LASTEXITCODE -ne 0) { throw 'Release-Build fehlgeschlagen.' }
Get-ChildItem -LiteralPath 'app/build/outputs/apk/release' -Filter '*.apk' -File |
    Select-Object -ExpandProperty FullName
```

### 2. Tatsächlich erzeugtes APK prüfen

Den Pfad des in diesem Build erzeugten Release-APKs aus der Ausgabe oben oder dem Build-Ergebnis in [Ticket 006](tickets/006-build-und-abnahme.md#build-und-apk-prüfung) übernehmen. Der verifizierte Release-Pfad steht bis zum signierten Build aus.

```powershell
$apk = (Resolve-Path -LiteralPath '<Pfad zum erzeugten Release-APK>' -ErrorAction Stop).Path
$previousApk = (Resolve-Path -LiteralPath '<Pfad zur bisherigen Release-APK>' -ErrorAction Stop).Path

apkanalyzer apk summary $apk
if ($LASTEXITCODE -ne 0) { throw 'APK konnte nicht gelesen werden.' }

apksigner verify --print-certs $apk
if ($LASTEXITCODE -ne 0) { throw 'Release-Signatur ist ungültig oder fehlt.' }

apksigner verify --print-certs $previousApk
if ($LASTEXITCODE -ne 0) { throw 'Signatur der bisherigen APK konnte nicht bestätigt werden.' }
```

Die APK-Zusammenfassung muss `org.johennifin.androidtv 200099 0.20.0` ergeben. Vor dem Update den SHA-256-Zertifikatsfingerabdruck mit dem bisherigen Release oder einer gleichwertigen verlässlichen Referenz abgleichen. Bei Abweichung oder fehlender Referenz die Installation aussetzen. [APK Analyzer](https://developer.android.com/tools/apkanalyzer#commands), [apksigner](https://developer.android.com/tools/apksigner#verify-options).

### 3. Bestehende Installation aktualisieren

Netzwerk-Debugging in den Entwickleroptionen der Shield einschalten. IP-Adresse und Port vom Gerät übernehmen und die ADB-Verbindung auf der Shield bestätigen.

```powershell
$shield = '<IP-Adresse>:<Port>'
adb connect $shield
adb devices
```

Erst fortfahren, wenn der Eintrag für `$shield` als `device` erscheint. Für ein Update muss `pm path` einen Pfad für die vorhandene JoHennifin-App liefern.

```powershell
adb -s $shield shell pm path org.johennifin.androidtv
```

```powershell
adb -s $shield install -r $apk
```

`adb install -r` aktualisiert die vorhandene Installation unter Erhalt der App-Daten. Bei einem Signatur- oder Versionskonflikt die Ursache prüfen und die vorhandene Installation behalten. Nach `Success` JoHennifin über den Launcher öffnen und Version, Anmeldung und Einstellungen prüfen. [Android Debug Bridge](https://developer.android.com/tools/adb#pm).

```powershell
adb disconnect $shield
```
