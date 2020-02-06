#!/bin/bash

set -x

HOSTNAME="node0"
NUM_CARS=100
NUM_ITER=10

java \
    -Xmx32G \
    -jar sim2apl-sumo/target/sim2apl-SUMO-simulation-1.0-SNAPSHOT-jar-with-dependencies.jar \
    -s /usr/bin/sumo \
    -c utrecht.passenger.sumo.cfg \
    --step-length 1 \
    --number-of-cars "$NUM_CARS" \
    --car-id-prefix "$HOSTNAME" \
    --use-matrix true \
    -i $NUM_ITER \
    --rich 25 \
    --medium 0 \
    --poor 75 \
    --speed-reduction 1.3 \
    --min-gap 1 \
    --full-statistics \
    --statistics-directory "./" \
    --route-statistics "./route-stats.log" \
    --summary-statistics "./summary-stats.log" \
