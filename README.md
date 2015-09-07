== Grools Checker genome properties integration ==

This tools aim to use genome properties information.
These information is used to check the consistency and the completeness of genome annotation.

== Schema ==

Genome Properties data are structured as follow

```
   -   ----------                ---------- 
G |   | Property |              | Property |
E |    ----------                ----------
N |       \        And         /
P |      -----------         -----------
R |     | component |       | component |
O |      -----------         -----------
P |          \    And       /
  |        ----------      ----------
  |       | evidence |    | evidence |
  |        ----------      ----------
   -            \   Or   /       \
   |            ----------        *
T  |           | Property |
I  |            ----------
G  |              \ And
E  |              -----------
R  |             | component |
F  |              -----------
A  |               /   Or   \  
M  |        ----------     ----------
   |       | evidence |   | evidence |
   |        ----------     ----------
   - 
```

Note:
Some Evidences from genome properties are note linked to Tigerfam's properties.
In a such case these evidences and their parents are not inserted into grools.