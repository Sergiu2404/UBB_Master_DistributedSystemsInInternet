#!/bin/bash
config=$1
start=$2
end=$3

pids=()
for ((i=$start; i<=$end; i++))
do
    dotnet run --project . --no-build -- "$config" "$i" &
    pids+=($!)
done

echo "staarted PiDs: ${pids[@]}"
wait "${pids[@]}"
echo "finished"