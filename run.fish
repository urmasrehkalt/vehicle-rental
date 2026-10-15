#!/usr/bin/env fish
# Kompileerib ja käivitab sõidukite rendisüsteemi (fish shelli versioon run.sh-ist).
# Kasutus projekti juurkaustast: ./run.fish   (või: fish run.fish)

# Liigu skripti enda kausta, et töötaks igast kaustast käivitades
cd (dirname (status filename))

# macOS-is on /usr/bin/java olemas ka siis, kui JDK puudub, seega ei piisa
# "type -q java" kontrollist: katsetame, kas java päriselt käivitub.
if not java -version >/dev/null 2>&1
    # Homebrew paigaldab JDK PATH-ist välja (Apple Silicon ja Intel asukohad)
    for dir in /opt/homebrew/opt/openjdk@21/bin /usr/local/opt/openjdk@21/bin
        if test -x $dir/java
            # set -x: muudab PATH-i ainult selle skripti jaoks, terminali seadeid ei puutu
            set -x PATH $dir $PATH
            break
        end
    end
end

if not java -version >/dev/null 2>&1; or not javac -version >/dev/null 2>&1
    echo "Java (JDK) ei ole leitav. Paigalda JDK 17 või uuem, vt README jaotist 'JDK paigaldamine'." >&2
    exit 1
end

rm -rf out
mkdir -p out
javac -encoding UTF-8 -d out src/rental/*.java; or exit 1
java -cp out rental.Main
