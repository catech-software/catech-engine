#!/bin/sh

PDIR="$(pwd)"
DIR="$(dirname "$(realpath -q "$0")")"

cd "$DIR" || exit
./catech-engine -XstartOnFirstThread
cd "$PDIR" || exit
