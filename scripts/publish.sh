#!/usr/bin/env bash
set -e
if (( $# < 1 )); then
  echo "Usage: publish.sh <VERSION>"
  exit 1
fi

version=$1
directory="$(dirname "$0")"
source $directory/variables.sh

if [ -e $build_dir/$archive ]; then
    echo "Loading image from file"
    docker load < $build_dir/$archive
fi

echo "Tagging $registry/$app with $version"
docker tag $registry/$app $registry/$app:$version
echo "Now pushing to $registry"
docker push $registry/$app:$version
docker push $registry/$app:latest
