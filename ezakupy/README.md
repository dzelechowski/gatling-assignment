Gatling-ezakupy-assignment
=========================

This project contains a performance test for the e-commerce app.

Assignment steps:
1) Sign in as a client and create an order.
2) Sign in as an employee and accept the client's order.

Ids for products are generated randomly, therefore it is recommended to fetch products ids as an employee and
 assign them.

The test sends a given number of requests with a linear ramp over 5 seconds.

Maximum acceptable response Time is set to 5000 ms.
Successful requests percent is set to greater than 95%.

Run a simulation in "ezakupy" directory:

Start SBT
---------
```bash
$ sbt
```

Run a test
-----------------------

```bash
> gatling:testOnly ezakupy.CreateAcceptOrderTest
```


