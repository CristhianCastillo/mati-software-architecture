Context: According with README.md file from matching-engine-ms, pls take in care the curl command for Create new orders (BUY/SELL) and OrderAdapter.java class with the Matching process done with BUY/SELL orders. Take in care that id attribute need to be always different.

Task: Create a test scenario on .sh file with CURL command to add Orders (BUY/SELL) for reach at least 80 matches per second. The idea is to generate a TEST for 3 minutes of execution.

After 3 minutes, increase the test to 100 matches per second for 2 minutes more, then go back to 80 matches per second for 1 minute more. 