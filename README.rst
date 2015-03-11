*****************************************
Helper class for matrix calculation
*****************************************
This is a sample of helper classes for matrix calculation of Spark.

.. contents::
.. sectnum::

Feature
========
* Helper class of element-wise calculation (BasicDenseVectorRDD)

How to use
===============
You can compile and package this project.

::

 $ sbt clean assembly

As a result, you have target/scala-2.10/basicmatrixcalc.jar

Please execute spark-shell/spark-submit command with --jars option.

::

 $ spark-shell --jars target/scala-2.10/basicmatrixcalc.jar

Then, you can use BasicDenseVectorRDD class and it's helper methods.

::

 scala> import breeze.linalg.{DenseVector => BDV}
 scala> import net.dobachi.spark.BreezeDenseVectorRDDCalculator

 (snip)

.. vim: ft=rst tw=0
