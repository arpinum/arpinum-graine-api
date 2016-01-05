#!/bin/bash
set -e

directory="$(dirname "$0")"
source $directory/variables.sh

echo "Building $registry/$app"

docker build -t $registry/$app .

echo "Saving image to $archive"
mkdir $build_dir
docker save $registry/$app:latest > $build_dir/$archive
