#!/bin/bash

# Load Test Script for Order Handler MS
# Test phases:
# 1. 100-200 orders/sec for 3 minutes
# 2. 250 orders/sec for 2 minutes  
# 3. 100-200 orders/sec for 1 minute

BASE_URL="http://localhost:8081"
ENDPOINT="/api/v1/order"

# Function to generate unique order ID
generate_order_id() {
    echo "order-$(date +%s%N)-$$-$RANDOM"
}

# Function to generate random product ID
generate_product_id() {
    echo "product-$((RANDOM % 1000 + 1))"
}

# Function to generate random product count
generate_product_count() {
    echo $((RANDOM % 10 + 1))
}

# Function to send order request
send_order() {
    local order_id=$(generate_order_id)
    local product_id=$(generate_product_id)
    local product_count=$(generate_product_count)
    
    curl -s -X POST "${BASE_URL}${ENDPOINT}" \
        -H "Content-Type: application/json" \
        -d "{\"id\":\"${order_id}\",\"productId\":\"${product_id}\",\"productCount\":${product_count}}" \
        > /dev/null 2>&1
}

# Function to run load test for specified duration and rate
run_load_test() {
    local duration=$1
    local rate=$2
    local interval=$(echo "scale=3; 1/$rate" | bc -l)
    
    echo "Starting load test: ${rate} requests/sec for ${duration} seconds"
    
    local end_time=$(($(date +%s) + duration))
    local count=0
    
    while [ $(date +%s) -lt $end_time ]; do
        send_order &
        count=$((count + 1))
        sleep $interval
        
        # Show progress every 50 requests
        if [ $((count % 50)) -eq 0 ]; then
            echo "Sent $count requests..."
        fi
    done
    
    wait
    echo "Completed phase: $count requests sent"
}

# Check if bc is available for calculations
if ! command -v bc &> /dev/null; then
    echo "Error: bc calculator is required but not installed"
    echo "Install with: brew install bc"
    exit 1
fi

echo "=== Order Handler Load Test ==="
echo "Target: ${BASE_URL}${ENDPOINT}"
echo "Starting load test..."

# Phase 1: 150 orders/sec for 3 minutes (180 seconds)
echo -e "\n--- Phase 1: 150 orders/sec for 3 minutes ---"
run_load_test 180 150

# Phase 2: 250 orders/sec for 2 minutes (120 seconds)  
echo -e "\n--- Phase 2: 250 orders/sec for 2 minutes ---"
run_load_test 120 250

# Phase 3: 150 orders/sec for 1 minute (60 seconds)
echo -e "\n--- Phase 3: 150 orders/sec for 1 minute ---"
run_load_test 60 150

echo -e "\n=== Load test completed ==="
