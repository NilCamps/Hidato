#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

SRCDIR="$ROOT_DIR/Fonts/src"
TESTDIR="$SCRIPT_DIR/tests"

cd "$SRCDIR" || exit 1

echo "=== Compilant el projecte ==="
javac -encoding UTF-8 main.java 2>&1
if [ $? -ne 0 ]; then
    echo "ERROR: No es pot compilar el projecte"
    exit 1
fi
echo "Compilació correcta"
echo ""

PASS=0
FAIL=0
TOTAL=0

for i in $(seq -w 1 10); do
    INPUT="$TESTDIR/test${i}_input.txt"
    EXPECTED="$TESTDIR/test${i}_output.txt"

    if [ ! -f "$INPUT" ]; then
        continue
    fi

    TOTAL=$((TOTAL + 1))

    echo "=== TEST $i ==="

    ACTUAL=$(java main < "$INPUT" 2>&1)
    EXIT_CODE=$?

    if [ ! -f "$EXPECTED" ]; then
        echo "  [INFO] No hi ha output esperat, generant..."
        echo "$ACTUAL" > "$EXPECTED"
        echo "  Output generat a $EXPECTED"
    else
        EXPECTED_CONTENT=$(cat "$EXPECTED")
        if [ "$ACTUAL" = "$EXPECTED_CONTENT" ]; then
            echo "  [PASS] Output coincideix"
            PASS=$((PASS + 1))
        else
            echo "  [FAIL] Output NO coincideix"
            echo "  --- Esperat ---"
            echo "$EXPECTED_CONTENT"
            echo "  --- Obtingut ---"
            echo "$ACTUAL"
            FAIL=$((FAIL + 1))
        fi
    fi

    if [ $EXIT_CODE -ne 0 ]; then
        echo "  [WARN] Exit code: $EXIT_CODE"
    fi

    echo ""
done

echo "================================"
echo "Resultats: $PASS/$TOTAL PASS, $FAIL FAIL"
echo "================================"
