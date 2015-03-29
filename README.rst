*****************************************
Helper class for matrix calculation
*****************************************
This is a sample implementation of helper for matrix calculation of Spark.

.. contents::
.. sectnum::

Feature
========
* Helper class of element-wise calculation (BasicDenseVectorRDD)

Usage
===============

Basic
-------
You can compile and package this project.

::

 $ sbt clean assembly

As a result, you have target/scala-2.10/basicmatrixcalc.jar

Please execute spark-shell/spark-submit command with --jars option.

::

 $ spark-shell --jars target/scala-2.10/basicmatrixcalc.jar

First, we generate sample data with Spark function for this tutorial.

::

 scala> import org.apache.spark.SparkContext
 scala> import org.apache.spark.mllib.random.RandomRDDs._
 scala> val arrData1 = normalRDD(sc, 1000L, 10)
 scala> val arrData2 = normalRDD(sc, 1000L, 10)

You have obtained data1 and data2 as RDD[Array[Double]].

Then, you can convert this RDD to RDD of Breeze DenseVector.::

 scala> import breeze.linalg.{DenseVector => BDV}
 scala> val bdv1 = arrData1.map(p => BDV(p))
 scala> val bdv2 = arrData2.map(p => BDV(p))

(Option) If you want to generate test data of RDD[BDV[Double]] directly,
you can use Breeze random function.

::

 scala> val dummy1 = sc.parallelize(0 to 999)
 scala> val dummy2 = sc.parallelize(0 to 999)
 scala> val dbdv1 = dummy1.map(p => BDV.rand(1))
 scala> val dbdv2 = dummy2.map(p => BDV.rand(1))

Then, you can use BDVDoubleCalc to calculate Elementwise addition.

::

 scala> import net.dobachi.spark.BDVDoubleCalc
 scala> val bdvDoubleCalc1 = BDVDoubleCalc(bdv1)
 scala> val sum = bdvDoubleCalc1 :+ bdv2

Please access the member "rdd" to obtain RDD[BDV[Double]] from BDVDoubleCalc::

 scala> val result1 = sum.rdd

When you have two BDVDoubleCalc instances
--------------------------------------------
When you have two BDVDoubleCalc instances,
you can pass second instance to first one's methods.::

 scala> val bdvDoubleCalc2 = BDVDoubleCalc(bdv2)
 scala> val anotherSum = bdvDoubleCalc1 :+ bdvDoubleCalc2

You can access raw RDD of the result::

 scala> val result2 = anotherSum.rdd

Helper method to calculate results directly
----------------------------------------------------
BDVDoubleCalc has static methods to obtain results of element-wise calculation.::

 scala> val result3 = BDVDoubleCalc.elementWiseAdd(bdv1, bdv2)

You obtain a result as RDD[BDV[Double]] instance directly.

Functions
==============

basic functions
-------------------

========= =============================
function  description
--------- -----------------------------
:+        element-wise addition
:-        element-wise subtraction
:/        element-wise division
:*        element-wise multiplication
========= =============================

helper method of BDVDoubleCalc
---------------------------------

================= =============================
function          description
----------------- -----------------------------
elementWiseAdd    element-wise addition
elementWiseSub    element-wise subtraction
elementWiseDiv    element-wise division
elementWiseMult   element-wise multiplication
================= =============================

.. vim: ft=rst tw=0
