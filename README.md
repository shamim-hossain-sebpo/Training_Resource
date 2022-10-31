# Training_Resource

Data Structures and Java Collections

Part -1

1.Find all pairs of an integer array whose sum is equal to a given number.
2.Find the largest and smallest number in an unsorted integer array.
3.How to find the length of a singly linked list?
4.How to count a number of vowels and consonants in a String?
5.Search for a (value) in a hash-map, and if presents return its key. Else return null.
6.How do you find the missing number in a given integer array of 1 to 100?
7.How do you find duplicate numbers in an array if it contains multiple duplicates?
8.How do you reverse a linked list?
9.How do you count the occurrence of a given character in a string?
10.Search for a (key) in the hash-map, and if present return its value. Else return null.
[#Solution](https://github.com/shamim-hossain-sebpo/Training_Resource/blob/master/src/main/java/DSA_Collection/DSApart_1.java)


Part -2

i. Arrays (Linear/Circular and 2D-Arrays)
ii. List (ArrayList)
iii. Set (HashSet)
iv. Map (HashMap)
v. HashTable
vi. Stacks
vii. Queues
[#Practice](https://github.com/shamim-hossain-sebpo/Training_Resource/tree/master/src/main/java/DSA_Collection)


Part -3

Insturctions

Use proper variable and method naming. Naming an integer 'X' is improper and is NOT considered best practice.
Optimize code, as much as you can. Avoid using brute force approaches, which increases code complexity. If there is a method, for a specific Collection, try using that.
Given an Integer Array, let's say int[ ] arr = {10, 32, 1, 8, 32, 92, 41,71,34,64,99}
i) Find the maximum and minimum value of the given array, making sure the line below is in the code System.out.println("The Max value is: " + findMaxValue(arr) + "and Min Value is: " + findMinValue(arr). Basically, you have to complete those 2 unimplemented methods. Cannot use built-in functions

ii) Convert the array to a List, and then print the List using enhanced for loop. Do it in a function called 'convertingToListAndPrint(int[] arr)

Let's say this is an ArrayList:
groceryList = ["Eggs","Cheese","Chicken","Milk", "Beef", "Potato","Potato", "Carrot", "Eggs", "Eggs"]
Somehow, the user, noted down some elements twice
i) Implement the user groceryList in Java using ArrayList
ii) Remove the duplicate elements of the list, and print them using enhanced for loop.
iii) The user wants to make sure whether he wrote down Potato in his groceryList. Without using the brute force approach, create a method "isPotatoThere(...)" that returns true, if it exists, else false.
iv) The user realized he mistakenly wrote "Beef" instead of "Mutton". Update the shopping list. Make sure, you mention the line below in your code. System.out.println("The updated List are" + updatedArrayList(...))
[#Solution](https://github.com/shamim-hossain-sebpo/Training_Resource/blob/master/src/main/java/DSA_Collection/Part3.java)


2.Construct a dictionary (HashTable/HashMap any one of your wish), and print them. Each individual can have none, one or more than one address. Code for the given scenario, making the individual name as the unique key
-> Akib has 3 addresses = Mirpur, Dhanmondi, Shiddheshwari -> Sajeeb has 1 address = Lalmatia -> Niloy has 2 addresses = Puran Dhaka, Rajarbag -> Ratul lives abroad has no address

i) Construct the Dictionary, using the scenario above. ii) Print them such that the output is like this

Individual 1: Akib Address 1: Mirpur Address 2: Dhanmondi Address 3: Shiddheshwari

*The one with no address should show - "NO ADDRESS", but you cannot put 'NO ADDRESS' in the dictionary.
[#Solution](https://github.com/shamim-hossain-sebpo/Training_Resource/blob/master/src/main/java/DSA_Collection/Part3_2.java)


