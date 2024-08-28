<head>
    <title>Do's and Don'ts</title>
</head>

# Performance Tests Do's and Don'ts

### Follow core performance testing activities

1. **Identify Test environment**
    - CPU count
    - RAM capacity
    - Disk size
    - SUT configuration
2. **Identify Performance testing type**
    - Performance
    - Load
    - Stress
3. **Identify what needs to be measured**
    - Users
    - Volume
    - Response time
    - Throughput
    - Resource consumption etc.
4. **Plan and design tests**
5. **Execute tests**
6. **Analyze report, make improvements, retest.**

### Use proper tools for performance testing

It is very important to choose tools which don't introduce additional complexity and overhead to the system.

If your goal is to measure server API response time right choice will be to use HTTP or CLI tool.
If goal is to measure UI render time, Chrome dev tools can be a good choice.

### Diversify test data
Diversifying test data brings more realistic results.
This prevents frequent cache hits, covers different flow branches.

### Measure performance BEFORE optimizing code
In order to tell whether optimization was successful you need to compare results.
Comparison is impossible if initial measurement didn't take place.

### Use similar to production environment
Performance tests need to be carried out in production like
environment to identify maximum performance issues in testing phase.
When the environment is halved (CPU, Memory etc.), its capacity is not halved and
thus you can’t test the environment with the halved capacity since the response time results won’t be reliable.
