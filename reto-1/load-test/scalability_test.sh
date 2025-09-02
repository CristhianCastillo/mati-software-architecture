#!/bin/bash

# Scalability Test Script
# Phase 1: 17 matches/sec for 2 minutes
# Phase 2: 30 matches/sec for 1 minute  
# Phase 3: 17 matches/sec for 1 minute

BASE_URL="http://localhost:8080/api/v1"
ORDER_ID=1
REQUEST_COUNT=0

# Function to create matching order pair
create_order_pair() {
    local buy_price=$1
    local sell_price=$2
    local quantity=$3
    
    # Create BUY order
    curl -s -o /dev/null -X POST "$BASE_URL/order" \
        -H "Content-Type: application/json" \
        -d "{\"id\":$ORDER_ID,\"orderType\":\"BUY\",\"price\":$buy_price,\"quantity\":$quantity}" &
    
    ORDER_ID=$((ORDER_ID + 1))
    REQUEST_COUNT=$((REQUEST_COUNT + 1))
    
    # Create SELL order (lower price to ensure match)
    curl -s -o /dev/null -X POST "$BASE_URL/order" \
        -H "Content-Type: application/json" \
        -d "{\"id\":$ORDER_ID,\"orderType\":\"SELL\",\"price\":$sell_price,\"quantity\":$quantity}" &
    
    ORDER_ID=$((ORDER_ID + 1))
    REQUEST_COUNT=$((REQUEST_COUNT + 1))
}

# Function to run load phase
run_phase() {
    local matches_per_sec=$1
    local duration_sec=$2
    local phase_name=$3
    
    echo "Starting $phase_name: $matches_per_sec matches/sec for $duration_sec seconds"
    
    local interval=$(echo "scale=3; 1 / $matches_per_sec" | bc)
    local end_time=$(($(date +%s) + duration_sec))
    local phase_start_requests=$REQUEST_COUNT
    
    while [ $(date +%s) -lt $end_time ]; do
        # Random price between 95-105
        local base_price=$((95 + RANDOM % 11))
        local buy_price=$((base_price + 1))
        local sell_price=$base_price
        local quantity=$((1 + RANDOM % 10))
        
        create_order_pair $buy_price $sell_price $quantity
        
        # Show progress every 50 requests
        if [ $((REQUEST_COUNT % 50)) -eq 0 ]; then
            echo "Requests sent: $REQUEST_COUNT"
        fi
        
        sleep $interval
    done
    
    local phase_requests=$((REQUEST_COUNT - phase_start_requests))
    echo "Completed $phase_name - Sent $phase_requests requests"
}

echo "Starting Scalability Test..."
echo "Ensure matching engine is running on $BASE_URL"

# Phase 1: 17 matches/sec for 2 minutes (120 seconds)
run_phase 17 120 "Phase 1"

# Phase 2: 30 matches/sec for 1 minute (60 seconds)  
run_phase 30 60 "Phase 2"

# Phase 3: 17 matches/sec for 1 minute (60 seconds)
run_phase 17 60 "Phase 3"

# Wait for remaining background processes
wait

echo "Scalability test completed!"
echo "Total requests sent: $REQUEST_COUNT"
echo "Total expected matches: $((17*120 + 30*60 + 17*60)) = 4,050 matches"
