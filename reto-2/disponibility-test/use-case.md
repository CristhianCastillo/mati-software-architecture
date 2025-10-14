Context: Read the README file on ../README.md.

Task:
    - Create a .sh script with the next flow
        1. Send ramdon time Curl request for Reserve Products during 3 minutes.
        2. Send Curl Change Env on Principal Node (Bad Update).
        3. Send Curl Refresh Envs on Principal Node.
        4. After these changes create send ramdon time Curl request for Reserve Products during 2 minutes
        5. Send Curl Change Env on Principal Node (Good Update).
        6. Send Curl Refresh Envs on Principal Node.
        7. finally, Send ramdon time Curl request for Reserve Products during 3 minutes