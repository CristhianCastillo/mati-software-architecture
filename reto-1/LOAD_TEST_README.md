# Load Testing Scripts for Matching Engine

This directory contains load testing scripts designed to test the matching engine's performance according to the specified requirements.

## Test Requirements

- **Phase 1**: 80 matches/second for 3 minutes
- **Phase 2**: 100 matches/second for 2 minutes  
- **Phase 3**: 80 matches/second for 1 minute

## Scripts

### 1. `load_test.sh` - Basic Load Test
Sequential order generation with precise timing control.

```bash
./load_test.sh
```

### 2. `load_test_optimized.sh` - Optimized Load Test (Recommended)
Uses parallel processing for better performance and more realistic load generation.

```bash
./load_test_optimized.sh
```

### 3. `monitor.sh` - System Monitor
Monitors the order book and quotes during testing.

```bash
./monitor.sh
```

## Prerequisites

1. **Matching Engine Running**: Ensure the matching engine is running on `http://localhost:8080`
2. **bc Calculator**: Install with `brew install bc` (for macOS)
3. **jq** (optional): For JSON formatting in monitor script: `brew install jq`

## Usage Instructions

1. **Start the Matching Engine**:
   ```bash
   cd matching-engine-ms
   ./gradlew bootRun
   ```

2. **Run Load Test** (in another terminal):
   ```bash
   cd /path/to/reto-1
   ./load_test_optimized.sh
   ```

3. **Monitor System** (optional, in third terminal):
   ```bash
   ./monitor.sh
   ```

## How It Works

### Order Generation Strategy
- Creates matching BUY/SELL order pairs
- BUY orders have higher prices than SELL orders to ensure matching
- Each order gets a unique ID (incremented automatically)
- Random price variations within reasonable ranges
- Random quantities between 1-10

### Matching Logic
Based on the `OrderAdapter.java` analysis:
- Orders match when BUY price >= SELL price
- Matching happens automatically when orders are added
- Each match processes one order pair
- Partial fills are supported

### Performance Optimization
The optimized script uses:
- Parallel request processing (up to 20 concurrent requests)
- Batch processing to maintain target rates
- Background processes for non-blocking execution

## Expected Results

- **Total Matches**: 26,400 (14,400 + 12,000 + 4,800)
- **Total Duration**: ~6 minutes
- **Total Orders Created**: ~52,800 orders

## Monitoring

The system logs will show:
- Order matching events
- Match timing information
- Order book state changes
- Performance metrics

## Troubleshooting

1. **Connection Refused**: Ensure matching engine is running on port 8080
2. **bc Command Not Found**: Install bc calculator
3. **Slow Performance**: Use the optimized script for better throughput
4. **Memory Issues**: Monitor system resources during high-load phases

## Test Validation

To verify the test is working correctly:
1. Check application logs for match events
2. Use the monitor script to see order book changes
3. Verify timing matches the expected phases
4. Confirm unique order IDs are being used
