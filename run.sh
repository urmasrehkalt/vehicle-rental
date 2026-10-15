#!/usr/bin/env bash
# Kompileerib ja käivitab sõidukite rendisüsteemi.
# Kasutus projekti juurkaustast: ./run.sh
set -e
cd "$(dirname "$0")"

# macOS-is on /usr/bin/java olemas ka siis, kui JDK puudub, seega ei piisa
# "command -v java" kontrollist: katsetame, kas java päriselt käivitub.
if ! java -version >/dev/null 2>&1; then
    # Homebrew paigaldab JDK PATH-ist välja (Apple Silicon ja Intel asukohad)
    for dir in /opt/homebrew/opt/openjdk@21/bin /usr/local/opt/openjdk@21/bin; do
        if [ -x "$dir/java" ]; then
            export PATH="$dir:$PATH"
            break
        fi
    done
fi

if ! java -version >/dev/null 2>&1 || ! command -v javac >/dev/null 2>&1; then
    echo "Java (JDK) ei ole leitav. Paigalda JDK 17 või uuem, vt README jaotist 'JDK paigaldamine'." >&2
    exit 1
fi

rm -rf out
mkdir -p out
javac -encoding UTF-8 -d out src/rental/*.java
java -cp out rental.Main
