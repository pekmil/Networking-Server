# Networking-Server

A Networking-Server projekt egy egyszerű socket illetve nio szerver mintakódot tartalmaz. A szerver futtatásához szükséges a fájlok fordítása és csomagolása. A fordítás során használni kell a Networking-Messages.jar csomagot. A csomagolás során pedig a MANIFEST.MF állományt.

1. Töltsük le a forrásfájlokat tartalmazó mappát (src) egy gyökérkönyvtárba (<i>root</i>)
2. Hozzuk létre a <i>root</i>/classes mappát
3. A <i>root</i> mappából adjuk ki a következő parancssori parancsot:<br /><code>javac src/server/nio/\*.java src/server/socket/\*.java src/server/socket/messageboard/\*.java -d classes -classpath <i>path/to/</i>Networking-Messages.jar</code>
4. Lépjünk be a <i>root</i>/classes mappába és adjuk ki a következő parancssori parancsot:<br /><code>jar cfm Networking-Server.jar MANIFEST.MF server/nio/\*.class server/socket/\*.class server/socket/messageboard/\*.class</code>

Az előállt Networking-Server.jar csomag futtatása az alábbi parancssori paranccsal lehetséges:<br /><code>java -jar Networking-Server.jar</code><br />A futtatáshoz szükséges, hogy a Networking-Messages.jar csomagot a Networking-server.jar csomaggal közös mappában helyezzük el. Jelenlegi konfigurációval a socket szerver indul el.