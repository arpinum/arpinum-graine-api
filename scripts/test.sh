#!/bin/bash
directory="$(dirname "$0")"
source $directory/variables.sh

if [ -e $build_dir/$archive ]; then
    echo "Loading image from file"
    docker load < $build_dir/$archive
fi

echo "Running test with $registry/$app"
mkdir -p $(pwd)/$build_dir/test-results
docker run --rm -v $(pwd)/$build_dir/test-results:/usr/src/build/test-results $registry/$app ./gradlew test
ret=$?
sudo chown -R $(whoami) $(pwd)/$build_dir

exit $ret

