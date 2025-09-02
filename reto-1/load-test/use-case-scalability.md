Context: According with README.md file from matching-engine-ms folder, pls take in care the curl command for Create new orders (BUY/SELL) and OrderAdapter.java class with the Matching process done with BUY/SELL orders the method name is call matchOrders() and the restriction to validate while structure.

Take in care that id attribute need to be always different.

Task: Create a test scenario on .sh file with CURL command to add Orders (BUY/SELL).
Plan of Execution: 
    - Reach at least 17 matches per seconds. Not matter how many ORDERS are created to reach this metric. The throughput need it, could be ramdom and should be keep it for 2 minutes.
    - After 2 minutes, The throughput of matches per seconds need to increed to 30 matches per seconds. The throughput need it, could be ramdom and should be keep it for 1 minutes.
    - After 1 minute, The throughput of matches per seconds need to decrease to 17 matches per seconds. The throughput need it, could be ramdom and should be keep it for 1 minutes.