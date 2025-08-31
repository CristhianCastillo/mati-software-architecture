#!/bin/bash

# Optimized Load Test Script for Matching Engine
# Uses parallel processing for better performance

BASE_URL="http://localhost:8080/api/v1/order"
ORDER_ID=1
MAX_PARALLEL=20

# Function to create matching order pair
create_matching_pair() {
    local base_id=$1
    local buy_price=$(echo "scale=2; 100 + ($RANDOM % 10)" | bc -l)
    local sell_price=$(echo "scale=2; $buy_price - 1 - ($RANDOM % 3)" | bc -l)
    local quantity=$((1 + $RANDOM % 10))
    
    # Create BUY order
    curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"id\": $base_id, \"orderType\": \"BUY\", \"price\": $buy_price, \"quantity\": $quantity}" > /dev/null
    
    # Create SELL order
    curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"id\": $((base_id + 1)), \"orderType\": \"SELL\", \"price\": $sell_price, \"quantity\": $quantity}" > /dev/null
}

# Function to run load test phase
run_phase() {
    local matches_per_second=$1
    local duration_seconds=$2
    local phase_name=$3
    
    echo "Starting $phase_name: $matches_per_second matches/sec for $duration_seconds seconds"
    
    local total_matches=$((matches_per_second * duration_seconds))
    local batch_size=$MAX_PARALLEL
    local batches=$((total_matches / batch_size))
    local remaining=$((total_matches % batch_size))
    local batch_interval=$(echo "scale=3; $batch_size / $matches_per_second" | bc -l)
    
    echo "Total matches: $total_matches, Batches: $batches, Batch interval: ${batch_interval}s"
    
    local start_time=$(date +%s)
    
    # Process full batches
    for ((batch=1; batch<=batches; batch++)); do
        # Launch batch of parallel requests
        for ((i=1; i<=batch_size; i++)); do
            create_matching_pair $ORDER_ID &
            ORDER_ID=$((ORDER_ID + 2))
        done
        
        # Wait for batch to complete
        wait
        
        # Progress update
        local completed=$((batch * batch_size))
        echo "Completed $completed/$total_matches matches"
        
        # Sleep to maintain rate (except for last batch)
        if ((batch < batches)); then
            sleep $batch_interval
        fi
    done
    
    # Process remaining matches
    if ((remaining > 0)); then
        for ((i=1; i<=remaining; i++)); do
            create_matching_pair $ORDER_ID &
            ORDER_ID=$((ORDER_ID + 2))
        done
        wait
    fi
    
    local end_time=$(date +%s)
    local actual_duration=$((end_time - start_time))
    local actual_rate=$(echo "scale=2; $total_matches / $actual_duration" | bc -l)
    
    echo "$phase_name completed: $total_matches matches in ${actual_duration}s (${actual_rate} matches/sec)"
}

# Check dependencies
if ! command -v bc &> /dev/null; then
    echo "Error: bc calculator is required. Install with: brew install bc"
    exit 1
fi

echo "Optimized Load Test for Matching Engine"
echo "======================================"
echo "Configuration:"
echo "- Max parallel requests: $MAX_PARALLEL"
echo "- Base URL: $BASE_URL"
echo "======================================"

# Test phases
run_phase 80 180 "PHASE 1"
run_phase 100 120 "PHASE 2"  
run_phase 80 60 "PHASE 3"

echo "======================================"
echo "Load test completed successfully!"
echo "Total orders created: $((ORDER_ID - 1))"
echo "======================================"
