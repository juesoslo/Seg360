#!/bin/sh
docker build . -t seg360
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run --net=host --rm -p 8080:8080 seg360"