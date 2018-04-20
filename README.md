### For more info:
http://www.mycertnotes.com/az/valyuta-alqi-satqisindan-qazanc-elde-etmek-bellman-ford-alqoritmi/

***



This task is given in a book _“Introduction to Algorithms”_ (MIT Press), 3rd edition, page 679:

> ### 24-3   Arbitrage
> Arbitrage is the use of discrepancies in currency exchange rates to transform one unit of a currency into more than one unit of the same currency. For example, suppose that `1` U.S. dollar buys `49` Indian rupees, `1` Indian rupee buys `2` Japanese yen, and `1` Japanese yen buys `0.0107` U.S. dollars. Then, by converting currencies, a trader can start with `1` U.S. dollar and buy `49 x 2 x 0.0107 = 1.0486` U.S. dollars, thus turning a proﬁt of `4.86` percent.

> Suppose that we are given n currencies `c1, c2, ..., cn`  and an `n x n` table `R` of exchange rates, such that one unit of currency `ci`  buys `R[i, j]` units of currency **`cj`**.

> **a.** Give an efﬁcient algorithm to determine whether or not there exists a sequence of currencies `{ci1, ci2, ..., cik}` such that
> `R[i1, i2] * R[i2, i3] ... R[ik-1, ik] * R[ik, i1] > 1.`
> Analyze the running time of your algorithm.

> **b.** Give an efﬁcient algorithm to print out such a sequence if one exists. Analyze the running time of your algorithm.



***

### Currency Rates of all Banks
![currency-rates-of-banks](http://www.mycertnotes.com/wp-content/uploads/2017/11/currency-rates-of-banks.jpg)



***

### Optimal Currency Rates:
![optimal-rates](http://www.mycertnotes.com/wp-content/uploads/2017/11/optimal-rates.jpg)


***

### Currency Graph:
* Source: “Algorithms (4th Edition)”,  Robert Sedgewick & Kevin Wayne, page 679
![arbitrage-graph](http://www.mycertnotes.com/wp-content/uploads/2017/11/arbitrage-graph.png)


***

### Bellman-Ford Algorithm
* The implementation of "Introduction to Algorithms" book:

![Bellman-Ford-Algorithm](http://www.mycertnotes.com/wp-content/uploads/2017/11/Bellman-Ford-Algorithm.png)



***

 
### Arbitrage Opportunity:                 
 
**Arbitrage 1: (profit - 32.99 AZN)**
* 1000.0000 AZN = 507.6142 EUR (Bank Melli İran)
*  507.6142 EUR = 1032.9949 AZN (AmrahBank)
 
**Arbitrage 2: (profit - 3.16 AZN)**
* 1000.0000 AZN =  451.2635 GBP (AccessBank)
*  451.2635 GBP = 1003.1588 AZN (Gunay Bank)
 
**Arbitrage 3: (profit - 28.07 AZN)** 
*  1000.0000 AZN = 35087.7193 RUB (AccessBank)
* 35087.7193 RUB =  1028.0702 AZN (Azərpoçt)
 
**Arbitrage 4: (profit - 30.57 AZN)**
* 1000.0000 AZN =  507.6142 EUR (Bank Melli İran)
*  507.6142 EUR =  607.2868 USD (AmrahBank)
*  607.2868 USD = 1030.5658 AZN (AmrahBank)
 
**Arbitrage 5: (profit - 0.21 AZN)** 
* 1000.0000 AZN =  451.2635 GBP (AccessBank)
*  451.2635 GBP =  589.4000 USD (Gunay Bank)
*  589.4000 USD = 1000.2118 AZN (AmrahBank)
 
**Arbitrage 6: (profit - 25.65 AZN)** 
* 1000.0000 AZN = 35087.7193 RUB (AccessBank)
* 35087.7193 RUB = 604.3916 USD (DəmirBank)
* 604.3916 USD = 1025.6526 AZN (AmrahBank)
 
....................................
  
 
**Arbitrage 56: (profit - 3.87 AZN)** 
* 1000.0000 AZN = 507.6142 EUR (Bank Melli İran)
* 507.6142 EUR = 2028.4325 TRY (PAŞA Bank)
* 2028.4325 TRY = 442.6328 GBP (PAŞA Bank)
* 442.6328 GBP = 578.1273 USD (Gunay Bank)
* 578.1273 USD = 34261.6497 RUB (AccessBank)
* 34261.6497 RUB = 1003.8663 AZN (Azərpoçt)
 
**Arbitrage 57: (profit - 5.46 AZN)** 
* 1000.0000 AZN = 35087.7193 RUB (AccessBank)
* 35087.7193 RUB = 461.0180 GBP (DəmirBank)
* 461.0180 GBP = 602.1405 USD (Gunay Bank)
* 602.1405 USD = 513.5005 EUR (Bank Melli İran)
* 513.5005 EUR = 2051.9541 TRY (PAŞA Bank)
* 2051.9541 TRY = 1005.4575 AZN (PAŞA Bank)
 
**Arbitrage 58: (profit - 13.69 AZN)** 
* 1000.0000 AZN = 507.6142 EUR (Bank Melli İran)
* 507.6142 EUR = 607.2868 USD (AmrahBank)
* 607.2868 USD = 35989.7362 RUB (AccessBank)
* 35989.7362 RUB = 472.8696 GBP (DəmirBank)
* 472.8696 GBP = 2068.7457 TRY (PAŞA Bank)
* 2068.7457 TRY = 1013.6854 AZN (PAŞA Bank)
 












