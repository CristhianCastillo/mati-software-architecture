Context: According with README.md file from order-handler-ms, pls take in care the curl command for api/v1/order. Take in care that id attribute need to be always different.

Task: Create a test scenario on .sh file with CURL command to create orders for reach at least 100 to 200 created orders by second. The idea is to generate a TEST for 3 minutes of execution.

After 3 minutes, increase the test to 250 matches per second for 2 minutes more, then go back to 100 to 200 created orders per second for 1 minute more. 