package Groovy_Documents

import groovy.transform.EqualsAndHashCode

class Opertor_02 {
    public static void main(String[] args) {

        //------ Arithmetic Operator -----------

        assert 1 + 2 == 3
        assert 4 - 3 == 1
        assert 3 * 5 == 15
        assert 3 / 2 == 1.5
        assert 10 % 3 == 1
        assert 2**3 == 8

        assert 17.intdiv(5) == 3       // intdiv() return integer value of division not double value.


        //------ Unary Operator --------
        assert +3 == 3
        assert -4 == 0 - 4

        assert -(-1) == 1

        def a = 2
        def b = a++ * 3

        assert a == 3 && b == 6

        def c = 3
        def d = c-- * 2

        assert c == 2 && d == 6

        def e = 1
        def f = ++e + 3

        assert e == 2 && f == 5

        def g = 4
        def h = --g + 1

        assert g == 3 && h == 4

        //-------- Assignment Operator  --------

        def num = 4
        num += 3

        assert num == 7

        def num2 = 5
        num2 -= 3

        assert num2 == 2

        def num3 = 5
        num3 *= 3

        assert num3 == 15

        def num4 = 10
        num4 /= 2

        assert num4 == 5

        def num5 = 10
        num5 %= 3

        assert num5 == 1

        def num6 = 3
        num6 **= 2

        assert num6 == 9


        //------- Relational Operator ----------
        assert 1 + 2 == 3
        assert 3 != 4

        assert -2 < 3
        assert 2 <= 2
        assert 3 <= 4

        assert 5 > 1
        assert 5 >= -2


        def cat = new String('lion');
        def lion = 'lion';

        assert cat.equals(lion) // Java logical equality
        assert cat == lion      // Groovy shorthand operator

        // assert cat.is('lion')  // Groovy identity

        //-------- support after Groovy version 3 --------
        //assert cat === 'lion'  // operator shorthand
        //assert cat !== 'lion'    // negated operator shorthand


        //------ Logical Operator --------
        //----- Precedence -----
//        1. ! is higher precedence than logical &&.
//        2. && operator is higher precedence than logical | operator.

        assert !false            // true
        assert true && true      // true
        assert true || false     // true

        assert (!false && false) == false
        assert true || true && false == false
        assert true && false || true == true

        // ------ Bitwise Operator --------

        int num7 =0b1010           // binary value.
        assert num7 == 10

        int num8 = 0b1000
        assert num8 == 8

        assert (num7 & num7) == 0b1010             // 1010 & 1010 == 1010 (when both value are 1 than 1 otherwise 0).
        assert (num7 & num8) == num8               // 1010 & 1000 == 1000                   ""
        assert (num7 | num8) == num7               // 1010 | 1000 == 1010 (When any value is 1 than 1 otherwise 0).
        assert (num7 | num7) == num7
        assert (num7 ^ num8) == 0b0010             // 1010 ^ 1000 == 0010 (When both value is different than 1 otherwise 0).

            // Note for Conditional Operator...
            // 1. Non empty string evaluating is true.
            // 2. empty string evaluating is false.



    }


}
