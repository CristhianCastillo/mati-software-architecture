# Availability Test Script

## What it does

This script tests system availability during configuration changes by simulating real-world load patterns and measuring service resilience.

**Test Flow:**
1. Sends random-timed Reserve Products requests for 3 minutes (baseline)
2. Applies bad configuration update (`ENABLEDs` typo)
3. Refreshes environment configuration
4. Sends random-timed Reserve Products requests for 2 minutes (degraded state)
5. Applies good configuration update (`ENABLED` correct)
6. Refreshes environment configuration
7. Sends random-timed Reserve Products requests for 3 minutes (recovery)

## Prerequisites

- Services running on localhost:8081 and localhost:8083
- Docker infrastructure started (see ../README.md)

## How to execute

```bash
chmod +x availability_test.sh
./availability_test.sh
```

The script will run for approximately 8 minutes total and output progress indicators for each phase.
