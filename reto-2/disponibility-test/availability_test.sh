#!/bin/bash

echo "Starting availability test..."

# Check if services are running
echo "Checking if services are available..."
if ! curl --silent --fail http://localhost:8083/actuator/health > /dev/null; then
    echo "ERROR: Service on port 8083 is not available"
    exit 1
fi

if ! curl --silent --fail http://localhost:8081/actuator/health > /dev/null; then
    echo "ERROR: Service on port 8081 is not available"
    exit 1
fi

echo "Services are running. Starting test..."

# Test a single request first
echo "Testing single request..."
curl --location 'http://localhost:8083/api/v1/master/product/reserve' \
--header 'message-id: testRequest' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "name": "Papas Margarita",
    "count": 234,
    "clientId": 23
}'
echo ""

# Function to make Reserve Products request
reserve_product() {
    local response=$(curl --location 'http://localhost:8083/api/v1/master/product/reserve' \
    --header 'message-id: someImportantMessageId' \
    --header 'Content-Type: application/json' \
    --data '{
        "id": 1,
        "name": "Papas Margarita",
        "count": 234,
        "clientId": 23
    }' --write-out "%{http_code}" --silent --output /dev/null)
    
    if [ "$response" != "200" ]; then
        echo "Request failed with status: $response"
    fi
}

# Function to send requests for a duration with controlled load (80-100 req/min)
send_requests_for_duration() {
    local duration=$1
    local description=$2
    echo "$description for $duration minutes..."
    
    local end_time=$(($(date +%s) + duration * 60))
    while [ $(date +%s) -lt $end_time ]; do
        reserve_product
        echo -n "."
        # Sleep between 0.6-0.75 seconds (80-100 requests per minute)
        sleep $(echo "scale=2; 0.6 + ($RANDOM % 16) / 100" | bc)
    done
    echo " Done!"
}

# 1. Send random time requests for 3 minutes
send_requests_for_duration 10 "Sending Reserve Products requests"

# 2. Change Env on Principal Node (Bad Update)
echo "Applying bad configuration update..."
curl --location 'http://localhost:8081/api/v1/properties' \
--header 'Content-Type: application/json' \
--data '{
    "key": "infrastructure.adapters.db.status",
    "value": "ENABLEDs"
}'

# 3. Refresh Envs on Principal Node
echo "Refreshing environment..."
curl --location --request POST 'http://localhost:8081/actuator/refresh'

# 4. Send random time requests for 2 minutes after bad update
send_requests_for_duration 4 "Sending Reserve Products requests after bad update"

# 5. Change Env on Principal Node (Good Update)
echo "Applying good configuration update..."
curl --location 'http://localhost:8081/api/v1/properties' \
--header 'Content-Type: application/json' \
--data '{
    "key": "infrastructure.adapters.db.status",
    "value": "ENABLED"
}'

# 6. Refresh Envs on Principal Node
echo "Refreshing environment..."
curl --location --request POST 'http://localhost:8081/actuator/refresh'

# 7. Send random time requests for 3 minutes after good update
send_requests_for_duration 10 "Sending final Reserve Products requests"

echo "Availability test completed!"
