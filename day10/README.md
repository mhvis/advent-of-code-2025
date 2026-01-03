
## Sketch

*This sketch is out of date with the code.*

Coefficients:
```
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        a    b    c    d     e     f
```

Linear system:

* $e + f = 3$
* $b + f = 5$
* $c + d + e = 4$
* $a + b + d = 7$

We also have that all coefficient must be a positive integer.

Determine the upper bounds:

* a <= 7
* b <= 5 (min(5, 7))
* c <= 4
* d <= 4
* e <= 3
* f <= 3

Determine the lower bounds:

* $b \geq 2$, conjunction of the following terms:
    * $ b \geq 0$
  * $b=5-f \geq 5-3=2 $ (maximizing f, to minimize b)
  * $b=7-a-d \geq 7-7-4 = -4$
* $a \geq 0$, conjunction of:
  * $a \geq 0$
  * $a \geq 7 - \max{b} - \max{d} =-2$
* $c \geq 0$
* $d \geq 0$
* $e \geq 0$
* $f \geq 0$


MinimizeILP(coefficients $x_1$ to $x_n$):

1. Fill trivial unknowns in $x$
2. No unknowns left? Return $\sum _{i=1} ^n (x_i)$
3. Pick an unknown coefficient $x_i$
4. Compute $u_i$ and $l_i$ as respectively the upper and lower bound of $x_i$
5. Let $R = \infty$
6. For $g \in [l_i, u_i]$:
   1. Guess $x_i = g$
   2. $R = \min{(R, \text{MinimizeILP}(x))}$
7. (Reset the guess of $x_i$; reset the filled trivial unknowns)
8. Return $R$


In action:

* Guess a=0, then update the lower bounds: $b \geq 3$
  * Guess b=2, then f=3, e=0, d=5. Contradiction with $d \leq 4$
  * Guess b=3, then 
  * Guess b=0, then d=7, f=5, e=-2. Contradiction
  * Guess b=1, then f=4, e=-1. Contradiction



## Implementation sketch

We can use the following matrix for computing the upper and lower bounds:

```
a b c d e f
0 0 0 0 1 1   3
0 1 0 0 0 1 = 5
0 0 1 1 1 0   4
1 1 0 1 0 0   7
```
