#!/bin/bash

# Monitoring script for Matching Engine

BASE_URL="http://localhost:8080/api/v1"

echo "Matching Engine Monitor"
echo "======================"

while true; do
    echo ""
    echo "$(date): Checking system status..."
    
    # Get order book
    echo "Order Book:"
    curl -s -X GET "$BASE_URL/orders" | jq '.' 2>/dev/null || echo "Failed to get order book"
    
    echo ""
    echo "Best Quotes (Level 1):"
    curl -s -X GET "$BASE_URL/quota/level1" | jq '.' 2>/dev/null || echo "Failed to get quotes"
    
    echo "======================"
    sleep 5
done
