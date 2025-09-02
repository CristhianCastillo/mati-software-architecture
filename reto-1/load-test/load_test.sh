#!/bin/bash

# Load Test Script for Matching Engine
# Target: 18 matches/sec for 4 min, 84 matches/sec for 30 min, 18 matches/sec for 4 min

BASE_URL="http://localhost:8080/api/v1/order"
ORDER_ID=1

# Function to create a BUY order
create_buy_order() {
    local id=$1
    local price=$2
    local quantity=$3
    curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"id\": $id, \"orderType\": \"BUY\", \"price\": $price, \"quantity\": $quantity}" > /dev/null
}

# Function to create a SELL order
create_sell_order() {
    local id=$1
    local price=$2
    local quantity=$3
    curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"id\": $id, \"orderType\": \"SELL\", \"price\": $price, \"quantity\": $quantity}" > /dev/null
}

# Function to generate matching orders at specified rate
generate_orders() {
    local matches_per_second=$1
    local duration_seconds=$2
    local total_matches=$((matches_per_second * duration_seconds))
    local interval=$(echo "scale=3; 1.0 / $matches_per_second" | bc -l)
    
    echo "Generating $matches_per_second matches/sec for $duration_seconds seconds (Total: $total_matches request)"
    echo "Interval between matches: ${interval}s"
    
    for ((i=1; i<=total_matches; i++)); do
        # Create matching BUY and SELL orders
        # BUY at higher price, SELL at lower price to ensure matching
        local buy_price=$(echo "scale=2; 100 + ($RANDOM % 10)" | bc -l)
        local sell_price=$(echo "scale=2; $buy_price - ($RANDOM % 5)" | bc -l)
        local quantity=$((1 + $RANDOM % 10))
        
        # Create BUY order
        create_buy_order $ORDER_ID $buy_price $quantity &
        ORDER_ID=$((ORDER_ID + 1))
        
        # Create SELL order (will match with BUY)
        create_sell_order $ORDER_ID $sell_price $quantity &
        ORDER_ID=$((ORDER_ID + 1))
        
        # Progress indicator
        if ((i % 10 == 0)); then
            echo "Generated $i/$total_matches matches"
        fi
        
        # Sleep to maintain rate
        sleep $interval
    done
    
    # Wait for all background processes to complete
    wait
    echo "Completed $total_matches matches"
}

# Check if bc is available for calculations
if ! command -v bc &> /dev/null; then
    echo "Error: bc calculator is required but not installed"
    echo "Install with: brew install bc"
    exit 1
fi

echo "Starting Load Test for Matching Engine"
echo "======================================="
echo "Test Plan:"
echo "- Phase 1: 18 matches/sec for 4 minutes"
echo "- Phase 2: 84 matches/sec for 30 minutes"
echo "- Phase 3: 18 matches/sec for 4 minute"
echo "======================================="

# Phase 1: 18 matches/sec for 4 minutes (240 seconds)
echo ""
echo "PHASE 1: Starting 18 matches/sec for 4 minutes..."
start_time=$(date +%s)
generate_orders 18 240
phase1_end=$(date +%s)
echo "Phase 1 completed in $((phase1_end - start_time)) seconds"

# Phase 2: 84 matches/sec for 30 minutes (1800 seconds)
echo ""
echo "PHASE 2: Starting 84 matches/sec for 30 minutes..."
phase2_start=$(date +%s)
generate_orders 84 1800
phase2_end=$(date +%s)
echo "Phase 2 completed in $((phase2_end - phase2_start)) seconds"

# Phase 3: 18 matches/sec for 4 minute (240 seconds)
echo ""
echo "PHASE 3: Starting 18 matches/sec for 4 minute..."
phase3_start=$(date +%s)
generate_orders 18 240
phase3_end=$(date +%s)
echo "Phase 3 completed in $((phase3_end - phase3_start)) seconds"

total_end=$(date +%s)
total_duration=$((total_end - start_time))
total_matches=$((18*240 + 84*1800 + 18*240))

echo ""
echo "======================================="
echo "Load Test Completed!"
echo "Total Duration: $total_duration seconds"
echo "Total Request Generated: $total_matches"
echo "Average Rate: $(echo "scale=2; $total_matches / $total_duration" | bc -l) matches/sec"
echo "======================================="
